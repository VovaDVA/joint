pipeline{
    agent any
    tools {
        nodejs '20.11.1'
    }
    stages{
        stage("Installing reqirements"){
            steps{
                dir("chat"){
                    sh 'npm install'
                }
                dir("notifications"){
                    sh 'npm install'
                }
                dir("feed"){
                    sh 'npm install'
                }
            }
        }
        stage("Run tests for the chat service"){
            steps {
                dir("chat"){
                    sh 'npm test'
                }
            }
        }
        stage("Run tests for the notifications service"){
            steps {
                dir("notifications"){
                    sh 'npm test'
                }
            }
        }
        stage("Run tests for the auth service"){
            steps {
                dir("auth"){
                    sh 'mvn clean test'
                }
            }
        }
        
    }
}