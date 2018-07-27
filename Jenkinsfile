node {
    checkout scm
    docker.image('openjdk:8').withRun('-v /var/jenkins_home/.gradle:/root/.gradle') { c->
        sh './gradlew clean build'
    }
    docker.build('avaxo-client')
}