pipeline {
   agent any

   tools {
      // Install the Maven version configured as "M3" and add it to the path.
     maven 'Maven 3.3.9'
     jdk 'jdk8'
   }

   stages {
      stage('Build') {
         steps {
            // Get some code from a GitHub repository
            //git 'https://github.com/yelmariky/event-streaming.git'
             echo 'hello build'
            // Run Maven on a Unix agent.
            sh 'mvn -Dmaven.test.failure.ignore=true clean package'

            // To run Maven on a Windows agent, use
            // bat "mvn -Dmaven.test.failure.ignore=true clean package"
         }

         post {
            // If Maven was able to run the tests, even if some of the test
            // failed, record the test results and archive the jar file.
            success {
             echo 'success'
              junit '**/**/target/surefire-reports/TEST-*.xml'
              archiveArtifacts '**/**/target/*.jar'

            }
         }
      }
       stage('SonarQube analysis') {
       agent any
       steps {
          withSonarQubeEnv(credentialsId: '658e0bee857f7a362be4b86dd0edf44674835659', installationName: 'http://localhost:32001') { // You can override the credential to be used
            sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
          }
        }
        }
   }
}
