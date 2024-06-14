pipeline{
    agent any
    
    environment {
        TOKEN = credentials('BOT_TOKEN')
        CHAT = credentials('CHAT_ID')
    }

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

        stage("Run test for the feed service"){
            steps {
                dir("feed"){
                    sh 'npm test'
                }
            }
        }     
    }
    post {
        success{
           sh '''curl -X POST -H "Content-Type: application/json" -d \'{"chat_id": '$CHAT', "text": "[SUCCES] all tests were successful", "disable_notification": false}\' "https://api.telegram.org/bot$TOKEN/sendMessage"'''
        }
        
        failure{
            sh '''curl -X POST -H "Content-Type: application/json" -d \'{"chat_id": '$CHAT', "text": "[FAIL] something went wrong", "disable_notification": false}\' "https://api.telegram.org/bot$TOKEN/sendMessage"'''
        }
    }
}