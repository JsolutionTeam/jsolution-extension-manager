import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by System.getProperties()
val springBootVersion: String by System.getProperties()
val querydslVersion: String by System.getProperties()
val springdocVersion: String by System.getProperties()
val jwtVersion: String by System.getProperties()

buildscript {
    // 제거하면 gradle build 실패
    val kotlinVersion: String by System.getProperties()
    val springBootVersion: String by System.getProperties()

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("io.spring.gradle:dependency-management-plugin:1.0.13.RELEASE")
    }
}

plugins {
    val springBootVersion: String by System.getProperties()
    val kotlinVersion: String by System.getProperties()

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
//    id("io.gitlab.arturbosch.detekt") version "1.20.0"

    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
}

java.sourceCompatibility = JavaVersion.VERSION_11

//detekt {
//    config = files("$rootDir/detekt.yml")
//}

allprojects {
    group = "kr.co.jsol.jem"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}

subprojects {

    apply {
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
        plugin("org.jetbrains.kotlin.plugin.spring")

        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("kotlin-jpa")
        plugin("kotlin-kapt")
    }

    dependencies {
        // springboot
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-websocket")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-validation")
//      actuator에서 prometheus가 안먹히는 경우가 있음. 그럴때는 아래를 추가하면 됨.
//    implementation 'io.micrometer:micrometer-registry-prometheus:1.12.4'

        implementation("net.logstash.logback:logstash-logback-encoder:7.3")

        runtimeOnly("org.springframework.boot:spring-boot-devtools")
        runtimeOnly("org.springframework.boot:spring-boot-starter-tomcat")

        // jackson
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        // springdoc
        implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")
        implementation("org.springdoc:springdoc-openapi-kotlin:$springdocVersion")
        implementation("org.springdoc:springdoc-openapi-security:$springdocVersion")

        // jwt
        implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")

        runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

        // db
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.querydsl:querydsl-jpa:$querydslVersion")

        kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
        kapt("org.springframework.boot:spring-boot-configuration-processor")

        // mysql
        runtimeOnly("mysql:mysql-connector-java:8.0.33")

        //
        runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
        runtimeOnly("com.h2database:h2")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        annotationProcessor("com.querydsl:querydsl-apt:$querydslVersion:jpa")

        // use to springboot rest template patch method
//        implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
        implementation("org.apache.httpcomponents:httpclient:4.5.14")

        // okhttp
        implementation("com.squareup.okhttp3:okhttp:4.9.1")

        // minio
        implementation("io.minio:minio:8.5.2")

        // actuator
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        runtimeOnly("io.micrometer:micrometer-registry-prometheus")

        // sleuth
        implementation("org.springframework.cloud:spring-cloud-starter-sleuth:3.1.8") {
            exclude(group = "org.springframework.cloud", module = "spring-cloud-sleuth-brave")
        }
        // tracing (otel)
        implementation("org.springframework.cloud:spring-cloud-sleuth-otel-dependencies:1.1.3")
        implementation("org.springframework.cloud:spring-cloud-sleuth-otel-autoconfigure:1.1.3")
        implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.27.0")

        // gson
        implementation("com.google.code.gson:gson:2.8.9")

        // test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("com.ninja-squad:springmockk:3.1.2") // 4버전부터 springboot3, jdk17, 지원
        testImplementation("org.springframework.security:spring-security-test")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
        testImplementation("io.github.serpro69:kotlin-faker:1.15.0") // kotlin-faker
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        // springmockk 설정
        jvmArgs(
            "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
        )
    }

    sourceSets.getByName("main") {
        java {
            srcDirs("${layout.buildDirectory}/generated/source/kapt/main")
        }
    }
}
