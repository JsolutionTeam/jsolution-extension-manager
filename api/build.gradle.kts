plugins {
    java
    `java-test-fixtures`
}

// 입력 순번 오름차순으로 수정
dependencies {
    implementation(project(":auth"))
    implementation(project(":common"))
    implementation(project(":extension"))
    implementation(project(":file"))
    implementation(project(":user"))

    testFixturesImplementation("io.github.serpro69:kotlin-faker:1.15.0") // kotlin-faker
    testFixturesImplementation(testFixtures(project(":auth")))
    testFixturesImplementation(testFixtures(project(":common")))
    testFixturesImplementation(testFixtures(project(":extension")))
    testFixturesImplementation(testFixtures(project(":file")))
    testFixturesImplementation(testFixtures(project(":user")))
}
