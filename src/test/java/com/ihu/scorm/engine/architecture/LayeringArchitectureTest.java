package com.ihu.scorm.engine.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.ihu.scorm.engine", importOptions = ImportOption.DoNotIncludeTests.class)
class LayeringArchitectureTest {

  @ArchTest
  static final ArchRule apiShouldNotDependOnCrud = noClasses()
      .that().resideInAPackage("..api..")
      .should().dependOnClassesThat().resideInAPackage("..crud..");

  @ArchTest
  static final ArchRule domainShouldNotDependOnSpring = noClasses()
      .that().resideInAPackage("..domain..")
      .should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "jakarta.persistence..");

  @ArchTest
  static final ArchRule nonConstantClassesShouldNotExposePublicFields = noFields()
      .that().areNotStatic()
      .and().areDeclaredInClassesThat().haveSimpleNameNotEndingWith("Constants")
      .should().bePublic();
}
