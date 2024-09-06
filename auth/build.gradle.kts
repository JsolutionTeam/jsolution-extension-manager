import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

plugins {
    java
    `java-test-fixtures`
}

dependencies {
    implementation(project(":common"))
    implementation(project(":user"))

    testFixturesImplementation("io.github.serpro69:kotlin-faker:1.15.0") // kotlin-faker
    testFixturesImplementation(testFixtures(project(":common")))
    testFixturesImplementation(testFixtures(project(":user")))
}
