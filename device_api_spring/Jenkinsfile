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
          if [ \$(docker ps -a -q -f name=spring) ]; then
              echo "Removing container 'spring'..."
              docker rm -f 'spring'
          else
              echo "Container 'spring' does not exist."
          fi
          """
  
          // Check if the image exists
          sh """
          if [ \$(docker images -q spring:latest) ]; then
              echo "Removing image 'spring:latest'..."
              docker rmi -f spring:latest
          else
              echo "Image 'spring:latest' does not exist."
          fi
          """
      }
    }
    stage('Start container') {
      steps {
        sh '''
          cd deviceapi
          
          docker build -t spring:latest .
          
          docker run -d \
          -p 8080:8080 \
          --name spring \
          -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/device_api \
          -e SPRING_DATASOURCE_USERNAME=postgres \
          -e SPRING_DATASOURCE_PASSWORD=postgres \
          spring:latest
          
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
