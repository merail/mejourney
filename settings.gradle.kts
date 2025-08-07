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
        maven("https://jitpack.io")
    }
}

rootProject.name = "Mejourney"

include(":app")

include(":design")

include(":data")
include(":data:database")
include(":data:server")

include(":core")

include(":home")

include(":error")

include(":auth")
include(":auth:api")
include(":auth:impl")

include(":profiling")

include(":navigation")
include(":navigation:domain")
include(":navigation:graph")