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
                if [ $(docker ps -a -q -f name=express) ]; then
                    echo "Removing container 'express'..."
                    docker rm -f 'express'
                else
                    echo "Container 'express' does not exist."
                fi
                
                if [ $(docker images -q express:latest) ]; then
                    echo "Removing image 'express:latest'..."
                    docker rmi -f express:latest
                else
                    echo "Image 'express:latest' does not exist."
                fi
            '''
        } else {
            bat '''
                docker ps -a -q -f name=express > nul
                if %ERRORLEVEL% == 0 (
                    echo "Removing container 'express'..."
                    docker rm -f express
                ) else (
                    echo "Container 'express' does not exist."
                )
                
                docker images -q express:latest > nul
                if %ERRORLEVEL% == 0 (
                    echo "Removing image 'express:latest'..."
                    docker rmi -f express:latest
                ) else (
                    echo "Image 'express:latest' does not exist."
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
                cd deviceapi
                
                docker build -t express:latest .
                
                docker run -d \
                -p 3000:3000 \
                --name express \
                --network tac_default \
                -e MONGODB_URI=mongodb://mongo:27017/device_api \
                express:latest
                
                docker ps
              '''
          } else {
              bat '''
                  cd deviceapi
                
                  docker build -t express:latest .
                  
                  docker run -d ^
                  -p 3000:3000 ^
                  --name express ^
                  --network tac_default ^
                  -e MONGODB_URI=mongodb://mongo:27017/device_api ^
                  express:latest
                  
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
