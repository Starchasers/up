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
    register<com.moowork.gradle.node.npm.NpmTask>("npmDependencies") {
        description = "Installs all dependencies from package.json"
        setWorkingDir(file("${project.projectDir}"))

        inputs.file("package.json")
        inputs.file("package-lock.json")
        setArgs(listOf("install"))
    }

    register<com.moowork.gradle.node.npm.NpmTask>("npmBuild") {
        description = "Builds react-admin"

        setWorkingDir(file("${project.projectDir}"))
        setArgs(listOf("run", "build"))

        dependsOn("npmDependencies")
        inputs.files(fileTree("public"))
        inputs.files(fileTree("src"))
        inputs.file("package.json")
        inputs.file("package-lock.json")

        outputs.dir("build")
    }

    register<Zip>("packageReactAdminApp") {
        dependsOn("npmBuild")
        archiveBaseName.set("react-admin")
        entryCompression = org.gradle.api.tasks.bundling.ZipEntryCompression.STORED
        archiveExtension.set("jar")
        destinationDirectory.set(file("${project.projectDir}/build/artifact"))

        from("output") {
            into("static/admin")
        }
    }

    register("exampleTask"){

    }

    assemble {
        dependsOn("packageReactAdminApp")
    }

    clean {
        delete(named<Zip>("packageReactAdminApp").get().archiveFile)
    }
}

artifacts {
    add("npmResources", tasks.named<Zip>("packageReactAdminApp").get().archiveFile) {
        builtBy(tasks.named<Zip>("packageReactAdminApp"))
        type = "jar"
    }
}
