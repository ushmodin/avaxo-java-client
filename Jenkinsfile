pipeline {
    agent none
    states {
        stage('Build') {
            agent {
                docker {
                    image 'openjdk:8'
                    args '-v /var/jenkins_home/.gradle:/root/.gradle'
                }
            }
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Docker build') {
            agent any
            steps {
                sh 'docker build -t avaxo-client .'
                sh 'docker tag avaxo-client 192.168.1.240:8082/avaxo-client'
            }
        }
        stage('Docker push') {
            agent any
            steps {
                sh 'docker push 192.168.1.240:8082/avaxo-client'
            }
        }
    }
}