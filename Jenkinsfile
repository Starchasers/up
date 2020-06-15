def branch = BRANCH_NAME

node('master') {
    stage('Prepare') {
        checkout scm
        sh "./gradlew clean"
    }

    stage('Build') {
        sh "./gradlew bootjar"
        sh "mv spring-app/build/libs/*.jar ./"
        archiveArtifacts '*.jar'
    }

    stage('Test') {
        try {
            sh "./gradlew test"
        } catch(err) {
            currentBuild.result = 'FAILURE'
            error('Tests failed.')
        } finally {
            junit "spring-app/build/test-results/test/*.xml"
        }
    }

    stage('Document') {
        sh "./gradlew asciidoctor"
        sh "mv spring-app/build/generated-docs/index.html restDocs.html"
        archiveArtifacts "restDocs.html"
    }
}