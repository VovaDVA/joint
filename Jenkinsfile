pipeline{
    agent any
    stages{
        stage("Build java services"){
            steps{
                dir("auth"){
                    sh 'mvn clean package -DskipTests'
                }
                dir("profile"){
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage("Run docker containers"){
            steps{
                sh 'docker compose up -d --renew-anon-volumes'
            }
        }
    }
}