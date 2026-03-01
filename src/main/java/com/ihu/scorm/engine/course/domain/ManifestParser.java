package com.ihu.scorm.engine.course.domain;

import com.ihu.scorm.engine.common.constant.DetailKey;
import com.ihu.scorm.engine.common.error.ErrorCode;
import com.ihu.scorm.engine.common.error.ErrorMessages;
import com.ihu.scorm.engine.common.error.ValidationException;
import com.ihu.scorm.engine.standard.LearningStandard;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ManifestParser {

  public CourseManifestData parse(byte[] zipBytes) {
    byte[] manifestBytes = extractManifest(zipBytes);
    if (manifestBytes == null) {
      throw new ValidationException(
          ErrorCode.MANIFEST_NOT_FOUND,
          ErrorMessages.SCORM_MANIFEST_NOT_FOUND,
          Map.of());
    }

    String manifestXml = new String(manifestBytes, StandardCharsets.UTF_8);
    LearningStandard standard = detectStandard(manifestXml);

    Document document = parseXml(manifestBytes);
    XPath xpath = XPathFactory.newInstance().newXPath();

    Map<String, String> resourcesById = readResources(xpath, document);
    if (resourcesById.isEmpty()) {
      throw new ValidationException(
          ErrorCode.MANIFEST_INVALID,
          ErrorMessages.MANIFEST_RESOURCES_ARE_EMPTY,
          Map.of());
    }

    String title = firstNonBlank(
        xPathText(xpath, document, "(//*[local-name()='metadata']//*[local-name()='title'])[1]"),
        xPathText(xpath, document, "(//*[local-name()='title'])[1]"),
        "Untitled Course");

    String description = firstNonBlank(
        xPathText(xpath, document, "(//*[local-name()='metadata']//*[local-name()='description'])[1]"),
        xPathText(xpath, document, "(//*[local-name()='description'])[1]"),
        null);

    String defaultOrg = xPathText(xpath, document, "(/*[local-name()='manifest']/*[local-name()='organizations']/@default)[1]");
    String identifierRef = null;
    if (defaultOrg != null && !defaultOrg.isBlank()) {
      identifierRef = xPathText(xpath, document,
          "(//*[local-name()='organizations']/*[local-name()='organization'][@identifier='" + defaultOrg
              + "']/*[local-name()='item'][1]/@identifierref)[1]");
    }
    if (identifierRef == null || identifierRef.isBlank()) {
      identifierRef = xPathText(xpath, document, "(//*[local-name()='organizations']//*[local-name()='item'][1]/@identifierref)[1]");
    }

    String entrypoint = identifierRef == null ? null : resourcesById.get(identifierRef);
    if (entrypoint == null || entrypoint.isBlank()) {
      entrypoint = resourcesById.values().stream().findFirst().orElse(null);
    }
    if (entrypoint == null || entrypoint.isBlank()) {
      throw new ValidationException(
          ErrorCode.ENTRYPOINT_NOT_FOUND,
          ErrorMessages.COULD_NOT_RESOLVE_COURSE_ENTRYPOINT,
          Map.of());
    }

    int organizationsCount = xPathNodeCount(xpath, document, "//*[local-name()='organization']");
    Map<String, Object> metadata = new LinkedHashMap<>();
    metadata.put("organizationsCount", organizationsCount);
    metadata.put("resourcesCount", resourcesById.size());
    metadata.put("defaultOrganization", defaultOrg);
    metadata.put("resourceKeys", resourcesById.keySet());

    return new CourseManifestData(title, description, entrypoint, standard, metadata);
  }

  private byte[] extractManifest(byte[] zipBytes) {
    try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
      ZipEntry entry;
      while ((entry = zipInputStream.getNextEntry()) != null) {
        if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith("imsmanifest.xml")) {
          ByteArrayOutputStream output = new ByteArrayOutputStream();
          zipInputStream.transferTo(output);
          return output.toByteArray();
        }
      }
      return null;
    } catch (Exception exception) {
      throw new ValidationException(
          ErrorCode.INVALID_ZIP,
          ErrorMessages.INVALID_SCORM_ZIP_FILE,
          Map.of(DetailKey.REASON, exception.getMessage()));
    }
  }

  private LearningStandard detectStandard(String manifestXml) {
    String lower = manifestXml.toLowerCase();
    if (lower.contains("adlcp_rootv1p3") || lower.contains("adlseq") || lower.contains("imsss") || lower.contains("2004")) {
      return LearningStandard.SCORM_2004;
    }
    if (lower.contains("adlcp_rootv1p2") || lower.contains("scorm_1.2") || lower.contains("scorm 1.2")) {
      return LearningStandard.SCORM_12;
    }
    throw new ValidationException(
        ErrorCode.UNSUPPORTED_STANDARD,
        ErrorMessages.UNABLE_TO_DETECT_LEARNING_STANDARD,
        Map.of());
  }

  private Document parseXml(byte[] xmlBytes) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      return factory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlBytes));
    } catch (Exception exception) {
      throw new ValidationException(
          ErrorCode.MANIFEST_PARSE_ERROR,
          ErrorMessages.FAILED_TO_PARSE_IMSMANIFEST_XML,
          Map.of(DetailKey.REASON, exception.getMessage()));
    }
  }

  private Map<String, String> readResources(XPath xpath, Document document) {
    try {
      NodeList nodes = (NodeList) xpath.evaluate("//*[local-name()='resource']", document, XPathConstants.NODESET);
      Map<String, String> resources = new HashMap<>();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = nodes.item(i);
        Node identifier = node.getAttributes() == null ? null : node.getAttributes().getNamedItem("identifier");
        Node href = node.getAttributes() == null ? null : node.getAttributes().getNamedItem("href");
        if (identifier != null && href != null && !href.getNodeValue().isBlank()) {
          resources.put(identifier.getNodeValue(), href.getNodeValue());
        }
      }
      return resources;
    } catch (Exception exception) {
      throw new ValidationException(
          ErrorCode.MANIFEST_PARSE_ERROR,
          ErrorMessages.FAILED_TO_PARSE_RESOURCES,
          Map.of(DetailKey.REASON, exception.getMessage()));
    }
  }

  private String xPathText(XPath xpath, Document document, String expression) {
    try {
      String value = (String) xpath.evaluate(expression, document, XPathConstants.STRING);
      if (value == null || value.isBlank()) {
        return null;
      }
      return value.trim();
    } catch (Exception exception) {
      return null;
    }
  }

  private int xPathNodeCount(XPath xpath, Document document, String expression) {
    try {
      NodeList nodes = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
      return nodes.getLength();
    } catch (Exception exception) {
      return 0;
    }
  }

  private String firstNonBlank(String first, String second, String fallback) {
    if (first != null && !first.isBlank()) {
      return first;
    }
    if (second != null && !second.isBlank()) {
      return second;
    }
    return fallback;
  }
}
