plugins {
    id("com.github.node-gradle.node") version "2.2.3"
    id("base")
}

val npmResources: Configuration by configurations.creating

configurations {
    default {
        extendsFrom(configurations["npmResources"])
    }
}

tasks {
    node {
        download = true

        // Set the work directory for unpacking node
        workDir = file("${project.buildDir}/nodejs")

        // Set the work directory for NPM
        npmWorkDir = file("${project.buildDir}/npm")

        version = "12.16.2"
        npmVersion = "6.14.4"
    }

    register<com.moowork.gradle.node.npm.NpmTask>("npmDependencies") {
        description = "Installs all dependencies from package.json"
        setWorkingDir(file("${project.projectDir}"))

        inputs.file("package.json")
        inputs.file("package-lock.json")
        setArgs(listOf("install"))
    }

    register<com.moowork.gradle.node.npm.NpmTask>("npmBuild") {
        description = "Builds gatsby-app"

        setWorkingDir(file("${project.projectDir}"))
        setArgs(listOf("run", "build"))

        dependsOn("npmDependencies")
        inputs.files(fileTree("public"))
        inputs.files(fileTree("src"))
        inputs.file("package.json")
        inputs.file("package-lock.json")

        outputs.dir("public")
    }

    register<Zip>("packageGatsbyApp") {
        dependsOn("npmBuild")
        archiveBaseName.set("gatsby-app")
        entryCompression = org.gradle.api.tasks.bundling.ZipEntryCompression.STORED
        archiveExtension.set("jar")
        destinationDirectory.set(file("${project.projectDir}/build/artifact"))

        from("public") {
            into("static")
        }
    }

    register("exampleTask"){

    }

    assemble {
        dependsOn("packageGatsbyApp")
    }

    clean {
        delete(named<Zip>("packageGatsbyApp").get().archiveFile)
    }
}

artifacts {
    add("npmResources", tasks.named<Zip>("packageGatsbyApp").get().archiveFile) {
        builtBy(tasks.named<Zip>("packageGatsbyApp"))
        type = "jar"
    }
}
