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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CompSci Computations"
include(":app")
include(":maths_lib")
include(":features:karnaugh_maps")
include(":features:number_systems")
include(":features:polish_expressions")
include(":features:matrix_methods")
include(":pdf_viewer")
