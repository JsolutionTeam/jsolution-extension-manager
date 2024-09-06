plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "jsolution-extension-manager"
include("common")
include("file")
include("api")
include("extension")
include("auth")
include("user")
