package com.ihu.scorm.engine.course.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ihu.scorm.engine.standard.LearningStandard;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;

class ManifestParserTest {

  private final ManifestParser manifestParser = new ManifestParser();

  @Test
  void parsesScorm12Manifest() throws Exception {
    String manifest = """
        <manifest xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_rootv1p2\">
          <organizations default=\"org1\"><organization identifier=\"org1\"><title>Course 1.2</title><item identifierref=\"res1\"/></organization></organizations>
          <resources><resource identifier=\"res1\" href=\"index.html\"/></resources>
        </manifest>
        """;

    CourseManifestData data = manifestParser.parse(zipWithManifest(manifest));

    assertThat(data.standard()).isEqualTo(LearningStandard.SCORM_12);
    assertThat(data.entrypointPath()).isEqualTo("index.html");
    assertThat(data.title()).isEqualTo("Course 1.2");
  }

  @Test
  void parsesScorm2004Manifest() throws Exception {
    String manifest = """
        <manifest xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_rootv1p3\" xmlns:imsss=\"http://www.imsglobal.org/xsd/imsss\">
          <organizations default=\"org1\"><organization identifier=\"org1\"><title>Course 2004</title><item identifierref=\"res1\"/></organization></organizations>
          <resources><resource identifier=\"res1\" href=\"launch.html\"/></resources>
        </manifest>
        """;

    CourseManifestData data = manifestParser.parse(zipWithManifest(manifest));

    assertThat(data.standard()).isEqualTo(LearningStandard.SCORM_2004);
    assertThat(data.entrypointPath()).isEqualTo("launch.html");
  }

  private byte[] zipWithManifest(String manifestXml) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    try (ZipOutputStream zip = new ZipOutputStream(output, StandardCharsets.UTF_8)) {
      zip.putNextEntry(new ZipEntry("imsmanifest.xml"));
      zip.write(manifestXml.getBytes(StandardCharsets.UTF_8));
      zip.closeEntry();
      zip.putNextEntry(new ZipEntry("index.html"));
      zip.write("<html></html>".getBytes(StandardCharsets.UTF_8));
      zip.closeEntry();
    }
    return output.toByteArray();
  }
}
