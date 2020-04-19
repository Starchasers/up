def branch = BRANCH_NAME

node('master') {
    stage('Prepare') {
        checkout scm
        sh "./gradlew clean"
    }

    stage('Build') {
        sh "./gradlew assemble"
        sh "mv build/libs/*.jar ./"
        archiveArtifacts '*.jar'
    }

    stage('Test') {
        try {
            sh "./gradlew test"
        } catch(err) {
            currentBuild.result = 'FAILURE'
            error('Tests failed.')
        } finally {
            junit "build/test-results/test/*.xml"
        }
    }

    stage('Document') {
        sh "./gradlew asciidoctor"
        sh "mv build/generated-docs/html5/index.html restDocs.html"
        archiveArtifacts "restDocs.html"
    }
}