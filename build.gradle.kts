val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "com.compscicomputations"
version = "0.0.1"

application {
    mainClass.set("com.compscicomputations.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    google()
}

ktor {
    fatJar {
        archiveFileName.set("ktor-server.jar")
    }
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "compsci-computations-ktor-server" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}

dependencies {
    implementation("org.apache.commons:commons-email:1.5")

    implementation("com.google.api-client:google-api-client:1.32.1")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.mindrot:jbcrypt:0.4")

    implementation("io.insert-koin:koin-ktor:3.5.6")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.6")

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-http-redirect-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-request-validation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-rate-limit-jvm")
    implementation("io.ktor:ktor-server-resources-jvm")
}
