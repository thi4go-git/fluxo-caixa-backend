 pipeline {

    agent any;

    tools {
        jdk 'JDK_25'
        maven 'MAVEN_3.9.16'
    }

    stages {
        stage('Criando .ENV') {
            steps {
                withCredentials([file(credentialsId: 'fluxo-caixa-back-env', variable: 'ENV_FILE')]) {
                    sh '''
                        cp "$ENV_FILE" .env
                        chmod 600 .env

                        echo "==== ARQUIVO .ENV ===="
                        cat .env
                        echo "======================="
                    '''
                }
            }
        }
        stage('Compilando') {
            steps {
                sh 'chmod +x ./mvnw'
                sh './mvnw clean package -DskipTests=true'
            }
        }
        stage('Testes unitarios'){
            steps {
                sh './mvnw verify'
            }
        }
       stage('Sonar Analise') {
           environment{
               scannerHome = tool 'SONAR_SCANNER_JDK25'
           }
           steps {
               withSonarQubeEnv('SONAR'){
                   sh """
                       ${scannerHome}/bin/sonar-scanner -e \
                       -Dsonar.projectKey=fluxo-caixa-backend \
                       -Dsonar.projectName='fluxo-caixa-backend' \
                       -Dsonar.host.url=http://sonarqube:9000 \
                       -Dsonar.token=sqp_30031bf2305bd7e16759e94c738cbaf457e8fe1e \
                       -Dsonar.java.binaries=target
                   """
               }
           }
       }
       stage('Sonar QualityGate') {
           steps {
               sleep(20)
               timeout(time: 1, unit: 'MINUTES'){
                   waitForQualityGate abortPipeline: true
               }
           }
       }
       stage('Deploy'){
           steps {
               sh 'docker-compose down'
               sh 'docker-compose build'
               sh 'docker-compose up -d'
           }
       }
       stage('Limpando Cache'){
           steps {
               sleep(10)
               sh 'docker system prune -f'
               sh 'docker ps'
           }
       }
    }

   post{
        always {
             junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
             script {
                 if (currentBuild.result == 'FAILURE') {
                      echo "Build Com erro(s)!"
                      emailext attachLog: true, body: 'LOG:', subject: "BUILD ${BUILD_NUMBER} fluxo-caixa-backend Executado com Erro(s)!", to: 'thi4go19+jenkins@gmail.com'
                 } else {
                      echo "Build bem-sucedido!"
                      emailext attachLog: true, body: 'LOG:', subject: "BUILD ${BUILD_NUMBER} fluxo-caixa-backend Executado com Sucesso!", to: 'thi4go19+jenkins@gmail.com'
                 }
             }
        }
   }

}