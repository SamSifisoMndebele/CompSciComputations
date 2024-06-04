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
include(":core:database")
include(":feature:karnaugh_maps")
include(":feature:number_systems")
include(":feature:polish_expressions")
include(":feature:matrix_methods")
include(":pdf_viewer")
//include(":maths_lib")
include(":core:common")
