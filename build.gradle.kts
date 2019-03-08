import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.20"
    id("org.jetbrains.dokka") version "0.9.17"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14"
}

group = "com.github.glwithu06.semver"
version = "1.0.0"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xuse-experimental=kotlin.Experimental")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<DokkaTask> {
    reportUndocumented = false
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

detekt {
    config = files("$rootDir/lint/detekt-config.yml")
    filters = ".*/resources/.*,.*/build/.*"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
