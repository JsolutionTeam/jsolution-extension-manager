import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
bootJar.archiveFileName.set("api.jar")
bootJar.archiveVersion.set("")
jar.enabled = false

// 입력 순번 오름차순으로 수정
dependencies {
    implementation(project(":auth"))
    implementation(project(":common"))
    implementation(project(":extension"))
    implementation(project(":file"))
    implementation(project(":user"))
}
