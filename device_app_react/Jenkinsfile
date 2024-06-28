pipeline {
    agent any
    stages {
        stage("setup environment") {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            docker version
                            docker-compose version
                            docker info
                            curl --version
                        '''
                    } else {
                        bat '''
                            docker version
                            docker-compose version
                            docker info
                            curl --version
                        '''
                    }
                }
            }
        }
        stage('Prune Docker data') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            if [ $(docker ps -a -q -f name=react) ]; then
                                echo "Removing container 'react'..."
                                docker rm -f 'react'
                            else
                                echo "Container 'react' does not exist."
                            fi
                            
                            if [ $(docker images -q react:latest) ]; then
                                echo "Removing image 'react:latest'..."
                                docker rmi -f react:latest
                            else
                                echo "Image 'react:latest' does not exist."
                            fi
                        '''
                    } else {
                        bat '''
                            docker ps -a -q -f name=react > nul
                            if %ERRORLEVEL% == 0 (
                                echo "Removing container 'react'..."
                                docker rm -f react
                            ) else (
                                echo "Container 'react' does not exist."
                            )
                            
                            docker images -q react:latest > nul
                            if %ERRORLEVEL% == 0 (
                                echo "Removing image 'react:latest'..."
                                docker rmi -f react:latest
                            ) else (
                                echo "Image 'react:latest' does not exist."
                            )
                        '''
                    }
                }
            }
        }
        stage('Start container') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            cd device_app
                            
                            docker build -t react:latest .
                            
                            docker run -d \
                            -p 3001:3001 \
                            --name react \
                            --network tac_default \
                            -e REACT_APP_RABBITMQ_PORT=3002 \
                            -e PORT=3001 \
                            react:latest
                            
                            docker ps
                        '''
                    } else {
                        bat '''
                            cd device_app
                            
                            docker build -t react:latest .
                            
                            docker run -d ^
                            -p 3001:3001 ^
                            --name react ^
                            --network tac_default ^
                            -e REACT_APP_RABBITMQ_PORT=3002 ^
                            -e PORT=3001 ^
                            react:latest
                            
                            docker ps
                        '''
                    }
                }
            }
        }
    }
    post {
        success {
            emailext body: "Build ${currentBuild.fullDisplayName} succeeded",
                     subject: "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - Successful",
                     to: 'dronecraftbr10@gmail.com',
                     attachLog: true
        }
        failure {
            emailext body: "Build ${currentBuild.fullDisplayName} failed",
                     subject: "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - Failed",
                     to: 'dronecraftbr10@gmail.com',
                     attachLog: true
        }
        unstable {
            emailext body: "Build ${currentBuild.fullDisplayName} is unstable",
                     subject: "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - Unstable",
                     to: 'dronecraftbr10@gmail.com',
                     attachLog: true
        }
        always {
            emailext body: "Build ${currentBuild.fullDisplayName} has finished with status ${currentBuild.currentResult}",
                     subject: "${env.JOB_NAME} - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
                     to: 'dronecraftbr10@gmail.com',
                     attachLog: true
        }
    }
}
