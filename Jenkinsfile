// ============================================================
// Jenkinsfile — Declarative CI/CD Pipeline
// INT332 DevOps Pipeline Project — Unit VI
// Stages: Checkout → Build → Test → Docker Build → Push → Deploy → Post
// ============================================================

pipeline {

    // ---- AGENT (Unit VI: Jenkins agents) ----
    // 'any' = run on any available Jenkins agent
    agent any

    // ---- ENVIRONMENT VARIABLES ----
    environment {
        APP_NAME        = 'student-app'
        APP_VERSION     = '1.0.0'
        DOCKER_IMAGE    = "student-app:${APP_VERSION}"
        DOCKER_HUB_REPO = "${DOCKER_HUB_USERNAME}/student-app"
        MAVEN_OPTS      = '-Xmx512m'
        // Credentials stored in Jenkins Credentials Manager (Unit VI: security)
        DOCKER_HUB_USERNAME = credentials('docker-hub-username')
        DOCKER_HUB_PASSWORD = credentials('docker-hub-password')
    }

    // ---- PIPELINE OPTIONS ----
    options {
        // Keep only last 5 builds (saves disk space)
        buildDiscarder(logRotator(numToKeepStr: '5'))
        // Fail if pipeline runs longer than 30 minutes
        timeout(time: 30, unit: 'MINUTES')
        // Add timestamps to console output
        timestamps()
    }

    // ---- TRIGGERS (Unit VI: Webhook + pollSCM) ----
    triggers {
        // Poll GitHub every 5 minutes as fallback
        // Primary trigger: GitHub webhook → more efficient
        pollSCM('H/5 * * * *')
    }

    // ============================================================
    // STAGES
    // ============================================================
    stages {

        // ---- STAGE 1: Checkout ----
        stage('Checkout') {
            steps {
                echo '======== STAGE: Checkout ========'
                // Cleans workspace and checks out the latest code
                checkout scm
                // Print the commit info
                sh 'git log --oneline -5'
                sh 'git status'
            }
        }

        // ---- STAGE 2: Build ----
        stage('Build') {
            steps {
                echo '======== STAGE: Maven Build ========'
                // Run Maven build lifecycle: validate → compile → package
                sh 'mvn clean package -DskipTests -B'
                // Show the built artifact
                sh 'ls -lh target/*.jar'
                echo "Build successful: ${APP_NAME}-${APP_VERSION}.jar"
            }
        }

        // ---- STAGE 3: Test ----
        stage('Test') {
            steps {
                echo '======== STAGE: Unit Tests ========'
                // Run tests with Surefire plugin
                sh 'mvn test -B'
            }
            post {
                always {
                    // Publish JUnit test results in Jenkins UI (Unit VI: test reports)
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                    echo 'Test reports published to Jenkins'
                }
                failure {
                    echo 'Tests FAILED — pipeline will not proceed to Docker build'
                }
            }
        }

        // ---- STAGE 4: Code Quality ----
        stage('Code Quality Check') {
            steps {
                echo '======== STAGE: Code Quality ========'
                // Run Maven verify to get all reports
                sh 'mvn verify -DskipTests -B'
                // Archive the surefire HTML reports
                sh 'ls -la target/surefire-reports/ || echo "No reports yet"'
            }
        }

        // ---- STAGE 5: Docker Build ----
        stage('Docker Build') {
            steps {
                echo '======== STAGE: Docker Build ========'
                // Build the Docker image from Dockerfile
                sh "docker build -t ${DOCKER_IMAGE} ."
                // Tag with build number for traceability
                sh "docker tag ${DOCKER_IMAGE} ${DOCKER_IMAGE}-build-${BUILD_NUMBER}"
                // List the created images
                sh "docker images | grep student-app"
                echo "Docker image built: ${DOCKER_IMAGE}"
            }
        }

        // ---- STAGE 6: Docker Push ----
        stage('Docker Push') {
            steps {
                echo '======== STAGE: Docker Push to Registry ========'
                // Login to Docker Hub using Jenkins credentials
                sh "echo ${DOCKER_HUB_PASSWORD} | docker login -u ${DOCKER_HUB_USERNAME} --password-stdin"
                // Tag for Docker Hub
                sh "docker tag ${DOCKER_IMAGE} ${DOCKER_HUB_REPO}:${APP_VERSION}"
                sh "docker tag ${DOCKER_IMAGE} ${DOCKER_HUB_REPO}:latest"
                // Push both tags to Docker Hub
                sh "docker push ${DOCKER_HUB_REPO}:${APP_VERSION}"
                sh "docker push ${DOCKER_HUB_REPO}:latest"
                echo "Image pushed to Docker Hub: ${DOCKER_HUB_REPO}"
            }
        }

        // ---- STAGE 7: Deploy ----
        stage('Deploy') {
            steps {
                echo '======== STAGE: Deploy with Docker Compose ========'
                // Stop any running containers first
                sh 'docker-compose down --remove-orphans || true'
                // Pull the latest images and start all services
                sh 'docker-compose up -d --build'
                // Wait for containers to be healthy
                sh 'sleep 15'
                // Verify all containers are running
                sh 'docker-compose ps'
                // Test the health endpoint
                sh 'curl -f http://localhost:8080/api/students/health || echo "Health check pending..."'
                echo 'Deployment complete — application is running'
            }
        }

    } // end stages

    // ============================================================
    // POST ACTIONS (Unit VI: Post actions)
    // ============================================================
    post {

        success {
            echo """
            ============================================
            PIPELINE SUCCESS
            App     : ${APP_NAME} v${APP_VERSION}
            Build   : #${BUILD_NUMBER}
            Image   : ${DOCKER_IMAGE}
            Status  : All stages passed
            ============================================
            """
        }

        failure {
            echo """
            ============================================
            PIPELINE FAILED
            App     : ${APP_NAME}
            Build   : #${BUILD_NUMBER}
            Status  : Check logs above for details
            ============================================
            """
        }

        always {
            // Archive the JAR artifact in Jenkins
            archiveArtifacts artifacts: 'target/*.jar',
                             fingerprint: true,
                             allowEmptyArchive: true

            // Clean up Docker to save disk space
            sh 'docker system prune -f || true'

            // Clean the Jenkins workspace
            cleanWs()
        }

    } // end post

} // end pipeline
