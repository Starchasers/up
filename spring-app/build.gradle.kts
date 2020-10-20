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
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("commons-fileupload:commons-fileupload:1.3.3")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    runtimeOnly("com.h2database:h2:1.4.200")
    implementation("com.ibm.icu:icu4j:67.1")
    runtimeOnly("com.h2database:h2:1.4.200")
    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly(files("../gatsby-app/build/artifact/gatsby-app.jar"))

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

val snippetsDir = file("build/generated-snippets")

ext {
    set("snippetsDir", snippetsDir)
    set("javadocJsonDir", file("$buildDir/generated-javadoc-json"))
}

tasks {
    val dokka by getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
        outputDirectory = file("$buildDir/generated-javadoc-json").toString()
        outputFormat = "auto-restdocs-json"
        includeNonPublic = true
        dokkaFatJar = "capital.scalable:spring-auto-restdocs-dokka-json:2.0.7"
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        setOutputDir(file("$buildDir/generated-docs"))

        options["backend"] = "html"
        options["doctype"] = "book"

        attributes["source-highlighter"] = "highlightjs"
        attributes["snippets"] = snippetsDir

        dependsOn(test)
        dependsOn(dokka)
    }
    jar {
        dependsOn(asciidoctor)
    }

    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        archiveBaseName.set("up")
    }

    test {
        useJUnitPlatform()
        dependsOn(dokka)
    }

    register<org.springframework.boot.gradle.tasks.run.BootRun>("bootRunDev") {
        dependsOn("bootJar")
        group = "Application"
        val bootJar by getting(org.springframework.boot.gradle.tasks.bundling.BootJar::class)
        doFirst {
            main = bootJar.mainClassName
            classpath = sourceSets["main"].runtimeClasspath
            systemProperty("UP_DEV_CORS", "true")
            systemProperty("UP_JWT_SECRET", "devSecret")
        }
    }
}