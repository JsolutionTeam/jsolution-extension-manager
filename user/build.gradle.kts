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

    testFixturesImplementation("io.github.serpro69:kotlin-faker:1.15.0")// 이게 없으면 testFixtures 경로에서 faker 사용 불가능
    testFixturesImplementation(testFixtures(project(":common")))
}
