package com.amrullah.cashback_reward_service.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.amrullah.cashback_reward_service",
        importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule domain_is_free_of_spring =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
                    .because("the domain must stay pure Java, with no framework dependencies");

    @ArchTest
    static final ArchRule domain_is_free_of_persistence =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage("jakarta.persistence..")
                    .because("persistence belongs in adapter/out/persistence, not in the domain");

    @ArchTest
    static final ArchRule domain_does_not_depend_on_adapters_or_application =
            noClasses()
                    .that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage("..adapter..", "..application..")
                    .because("dependencies flow inward: adapter -> application -> domain");

    @ArchTest
    static final ArchRule application_does_not_depend_on_adapters =
            noClasses()
                    .that().resideInAPackage("..application..")
                    .should().dependOnClassesThat().resideInAPackage("..adapter..")
                    .because("dependencies flow inward: adapter -> application -> domain");

    @ArchTest
    static final ArchRule layer_dependencies_are_respected =
            layeredArchitecture().consideringOnlyDependenciesInLayers()
                    .withOptionalLayers(true)
                    .layer("Adapter").definedBy("..adapter..")
                    .layer("Application").definedBy("..application..")
                    .layer("Domain").definedBy("..domain..")

                    .whereLayer("Adapter").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter")
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter");

    @ArchTest
    static final ArchRule inbound_and_outbound_adapters_are_independent =
            slices()
                    .matching("..adapter.(*)..")
                    .should().notDependOnEachOther()
                    .because("inbound and outbound adapters communicate only through the application ports");
}
