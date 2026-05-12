pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Turcanu_Iuliana_ActivitateDMC2026"
include(":Laborator2")
include(":Laborator2:laborator3")
include(":Laborator2:laborator4")
include(":Laborator2:laborator4:laborator4")
include(":Laborator2:laborator8")
include(":Laborator2:laborator9")
include(":Laborator2:laborator10")
include(":proiect")
include(":Laborator2:laborator11")
