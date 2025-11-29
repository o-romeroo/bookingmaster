pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'bookingmaster'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    options {
        skipDefaultCheckout false
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
    }

    triggers {
        githubPush()
    }

    stages {

        /* ==========================================================
         *   ETAPA DE COMMIT (Commit Stage)
         *   - Checkout
         *   - Build
         *   - Testes Unitários
         *   - Testes de Integração
         *   - Package
         * ========================================================== */
        stage('Commit Stage') {
            stages {
                stage('Checkout') {
                    steps {
                        checkout scm
                        echo 'Código fonte baixado com sucesso'
                    }
                }

                stage('Build') {
                    steps {
                        sh 'chmod +x mvnw'
                        sh './mvnw -B clean compile'
                        echo 'Build compilado com sucesso'
                    }
                }

                stage('Unit Tests') {
                    steps {
                        sh './mvnw test -Dtest=!*IntegrationTest,!*AcceptanceTest'
                        echo 'Testes unitários executados'
                    }
                    post {
                        always {
                            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                        }
                    }
                }

                stage('Integration Tests') {
                    steps {
                        script {
                            // Inicia um container MariaDB para os testes de integração
                            // IMPORTANTE: O container é conectado à mesma rede do Jenkins
                            sh '''
                                docker rm -f mariadb-integration-test || true
                                docker run -d --name mariadb-integration-test \
                                    --network bookingmaster-network \
                                    -e MYSQL_ROOT_PASSWORD=test \
                                    -e MYSQL_DATABASE=bmdb_test \
                                    -e MYSQL_USER=test \
                                    -e MYSQL_PASSWORD=test \
                                    mariadb:11.2
                                
                                # Aguarda o MariaDB estar pronto
                                echo "Aguardando MariaDB iniciar..."
                                for i in $(seq 1 30); do
                                    if docker exec mariadb-integration-test mariadb -utest -ptest -e "SELECT 1" > /dev/null 2>&1; then
                                        echo "MariaDB está pronto!"
                                        break
                                    fi
                                    echo "Tentativa $i/30 - aguardando..."
                                    sleep 2
                                done
                            '''
                            
                            // Executa os testes de integração com o banco MariaDB externo
                            // Usa o nome do container como hostname (mesma rede Docker)
                            sh '''
                                ./mvnw failsafe:integration-test failsafe:verify \
                                    -Dit.test=*IntegrationTest \
                                    -Dspring.profiles.active=integration-test \
                                    -Dspring.datasource.url=jdbc:mariadb://mariadb-integration-test:3306/bmdb_test \
                                    -Dspring.datasource.username=test \
                                    -Dspring.datasource.password=test \
                                    -Dspring.datasource.driver-class-name=org.mariadb.jdbc.Driver \
                                    -Dspring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect \
                                    -Dspring.jpa.hibernate.ddl-auto=create-drop
                            '''
                        }
                        echo 'Testes de integração executados'
                    }
                    post {
                        always {
                            sh 'docker rm -f mariadb-integration-test || true'
                            junit testResults: 'target/failsafe-reports/*.xml', allowEmptyResults: true
                        }
                    }
                }

                stage('Package') {
                    steps {
                        sh './mvnw -B package -DskipTests'
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        echo 'Artefato JAR gerado e arquivado'
                    }
                }

                stage('Build Docker Image') {
                    steps {
                        script {
                            sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -t ${DOCKER_IMAGE}:latest -f Dockerfile ."
                            echo "Imagem Docker ${DOCKER_IMAGE}:${DOCKER_TAG} criada"
                        }
                    }
                }
            }
        }

        /* ==========================================================
         *   ETAPA DE TESTE DE ACEITAÇÃO (Acceptance Stage)
         *   - Deploy temporário para testes
         *   - Testes E2E / Aceitação
         *   - Cleanup do ambiente de teste
         * ========================================================== */
        stage('Acceptance Stage') {
            environment {
                APP_PORT = '8090'
                DB_PORT = '3308'
                SPRING_PROFILES_ACTIVE = 'test'
            }
            stages {
                stage('Start Test Environment') {
                    steps {
                        script {
                            // Para qualquer container anterior de teste
                            sh 'docker compose -f docker-compose-test.yml down --remove-orphans || true'
                            
                            // Inicia ambiente de teste isolado
                            sh '''
                                docker compose -f docker-compose-test.yml up -d
                                
                                # Aguarda aplicação estar pronta (health check)
                                # Usa host.docker.internal porque o Jenkins roda em container
                                echo "Aguardando aplicação iniciar..."
                                for i in $(seq 1 60); do
                                    if curl -s http://host.docker.internal:8090/actuator/health | grep -q "UP"; then
                                        echo "Aplicação está pronta!"
                                        break
                                    fi
                                    echo "Tentativa $i/60 - aguardando..."
                                    sleep 5
                                done
                            '''
                            echo 'Ambiente de teste iniciado'
                        }
                    }
                }

                stage('Acceptance Tests (E2E)') {
                    steps {
                        script {
                            // Usa host.docker.internal porque Jenkins roda em container Docker
                            sh './mvnw failsafe:integration-test failsafe:verify -Dit.test=*AcceptanceTest -Dtest.server.host=host.docker.internal -Dtest.server.port=8090'
                            echo 'Testes de aceitação E2E executados'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'target/failsafe-reports/*.xml', allowEmptyResults: true
                        }
                    }
                }

                stage('Cleanup Test Environment') {
                    steps {
                        sh 'docker compose -f docker-compose-test.yml down --remove-orphans --volumes || true'
                        echo '🧹 Ambiente de teste limpo'
                    }
                }
            }
        }

        /* ==========================================================
         *   ETAPA DE LANÇAMENTO (Release Stage)
         *   - Deploy em produção local (Docker Desktop)
         *   - Verificação de saúde pós-deploy
         * ========================================================== */
        stage('Release Stage') {
            stages {
                stage('Deploy to Production') {
                    steps {
                        script {
                            // Para aplicação anterior em produção
                            sh 'docker compose -f docker-compose.deploy.yml down --remove-orphans || true'
                            
                            // Deploy da nova versão
                            sh """
                                BUILD_NUMBER=${BUILD_NUMBER} docker compose -f docker-compose.deploy.yml up -d
                            """
                            echo "Deploy da versão ${DOCKER_TAG} em produção"
                        }
                    }
                }

                stage('Production Health Check') {
                    steps {
                        script {
                            // Usa host.docker.internal porque Jenkins roda em container
                            sh '''
                                echo "Verificando saúde da aplicação em produção..."
                                for i in $(seq 1 30); do
                                    if curl -s http://host.docker.internal:8080/actuator/health | grep -q "UP"; then
                                        echo "Aplicação em produção está saudável!"
                                        exit 0
                                    fi
                                    echo "Tentativa $i/30 - aguardando..."
                                    sleep 5
                                done
                                echo "Aplicação não respondeu ao health check"
                                exit 1
                            '''
                        }
                    }
                }

                stage('Tag Release') {
                    steps {
                        script {
                            sh """
                                docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:release-${DOCKER_TAG}
                                echo "Imagem taggeada como release-${DOCKER_TAG}"
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo '''
            ╔════════════════════════════════════════════════════════════╗
            ║  PIPELINE CONCLUÍDO COM SUCESSO!                           ║
            ║                                                            ║
            ║  Etapas executadas:                                        ║
            ║   - Commit Stage (Build + Unit + Integration Tests)        ║
            ║   - Acceptance Stage (E2E Tests)                           ║
            ║   - Release Stage (Deploy em Produção)                     ║
            ║                                                            ║
            ║  Aplicação disponível em: http://localhost:8080            ║
            ╚════════════════════════════════════════════════════════════╝
            '''
        }
        failure {
            echo '''
            ╔════════════════════════════════════════════════════════════╗
            ║  PIPELINE FALHOU!                                       ║
            ║                                                            ║
            ║  Verifique os logs para detalhes do erro.                 ║
            ╚════════════════════════════════════════════════════════════╝
            '''
            // Cleanup em caso de falha
            sh 'docker compose -f docker-compose-test.yml down --remove-orphans --volumes || true'
        }
        always {
            cleanWs()
        }
    }
}
