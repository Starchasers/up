import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.1.0"
    id("org.jetbrains.dokka") version "0.9.18"

    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
    kotlin("plugin.jpa") version "1.3.71"
}

group = "pl.starchasers"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

configurations {
    "jsondoclet"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("commons-fileupload:commons-fileupload:1.3.3")
    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly(project(":gatsby-app"))

    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("no.skatteetaten.aurora:mockmvc-extensions-kotlin:1.0.10")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("capital.scalable:spring-auto-restdocs-core:2.0.8")

    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
//    "jsondoclet"("capital.scalable:spring-auto-restdocs-json-doclet:2.0.7")//was jsondoclet()
    testImplementation("capital.scalable:spring-auto-restdocs-json-doclet-jdk9:2.0.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}



tasks {
    val dokka by getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
        outputFormat = "auto-restdocs-json"
        includeNonPublic = true
        dokkaFatJar = "capital.scalable:spring-auto-restdocs-dokka-json:2.0.7"
    }

    asciidoctor {
        attributes["source-highlighter"] = "highlightjs"

        dependsOn(test)
    }

    jar {
        dependsOn(asciidoctor)
    }

    test {
        useJUnitPlatform()
        dependsOn(dokka)
    }
}