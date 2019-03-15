import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    kotlin("jvm") version "1.3.20"
    id("org.jetbrains.dokka") version "0.9.17"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

group = "com.github.glwithu06.semver"
version = "1.0.0"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.0")
}

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

jacoco {
    toolVersion = "0.8.3"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
        csv.isEnabled = false
    }
}

detekt {
    config = files("$rootDir/lint/detekt-config.yml")
    filters = ".*/resources/.*,.*/build/.*"
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

artifacts {
    add("archives", sourcesJar)
    add("archives", javadocJar)
}

val publicationName = "SemverPublication"
publishing {
    publications {
        create<MavenPublication>(publicationName) {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    pkg(closureOf<BintrayExtension.PackageConfig> {
        userOrg = "glwithu06"
        repo = "maven"
        name = "semver"
        vcsUrl = "https://github.com/glwithu06/Semver.kt.git"
        websiteUrl = "https://github.com/glwithu06/Semver.kt"
        issueTrackerUrl = "https://github.com/glwithu06/Semver.kt/issues"
        setPublications(publicationName)
        version(closureOf<BintrayExtension.VersionConfig> {
            name = "${rootProject.version}"
            vcsTag = "${rootProject.version}"
        })
    })
}