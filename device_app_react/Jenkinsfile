pipeline {
  agent any
  stages {
    stage("setup environment") {
      steps {
        sh '''
          docker version
          docker-compose version
          docker info
          curl --version
        '''
      }
    }
    stage('Prune Docker data') {
      steps {
          // Check if the container exists
          sh """
          if [ \$(docker ps -a -q -f name=react) ]; then
              echo "Removing container 'react'..."
              docker rm -f 'react'
          else
              echo "Container 'react' does not exist."
          fi
          """
  
          // Check if the image exists
          sh """
          if [ \$(docker images -q react:latest) ]; then
              echo "Removing image 'react:latest'..."
              docker rmi -f react:latest
          else
              echo "Image 'react:latest' does not exist."
          fi
          """
      }
    }
    stage('Start container') {
      steps {
        sh '''
          cd deviceapi
          
          docker build -t react:latest .
          
          docker run -d \
          -p 3001:3000 \
          --name react \
          react:latest
          
          docker ps
        '''
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
