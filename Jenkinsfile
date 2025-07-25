// jenkinsfile
pipeline {
     agent {
        label 'Java21'
     }

    tools {
        maven 'Maven 3.9.10'
        jdk 'JDK 21'
    }

    parameters {
        string(name: 'GCP_PROJECT_ID', defaultValue: 'java-arena', description: 'GCP project ID')
        string(name: 'GCR_IMAGE_NAME', defaultValue: 'java-arena-auth-service', description: 'GCR image name')
        string(name: 'APP_VERSION', defaultValue: '1.0.0', description: 'Application/Docker image version')
        string(name: 'GKE_CLUSTER_NAME', defaultValue: 'javaarena', description: 'GKE cluster name')
        string(name: 'GKE_CLUSTER_ZONE', defaultValue: 'us-central1', description: 'GKE cluster zone')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                git branch: 'master', url: 'https://github.com/Fernandiotkd/javaarena-auth-service'
            }
        }

        stage('Build Java App') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Code Analysis') {
            steps {
                // Add your code analysis steps here, e.g.,
                echo "Skipping code analysis for now."
                // sh "sonar-scanner ..."
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def gcrImage = "gcr.io/${params.GCP_PROJECT_ID}/${params.GCR_IMAGE_NAME}:${params.APP_VERSION}"
                    echo "Building Docker image: ${gcrImage}"
                    sh "docker build -t ${gcrImage} ."
                }
            }
        }

        stage('Push Docker Image to GCR') {
            steps {
                script {
                    def gcrImage = "gcr.io/${params.GCP_PROJECT_ID}/${params.GCR_IMAGE_NAME}:${params.APP_VERSION}"
                    echo "Pushing Docker image to GCR: ${gcrImage}"
                    withCredentials([file(credentialsId: 'gcp-service-account-json', variable: 'GCP_KEY_FILE')]) {
                        // Activar la cuenta de servicio antes de configurar Docker
                        sh "gcloud auth activate-service-account --key-file=${GCP_KEY_FILE}"
                        sh "gcloud auth configure-docker"
                        sh "docker push ${gcrImage}"
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo "Deploying to Kubernetes..."
                    sh "gcloud container clusters get-credentials ${params.GKE_CLUSTER_NAME} --zone ${params.GKE_CLUSTER_ZONE} --project ${params.GCP_PROJECT_ID}"
                    sh "sed -i 's|gcr.io/${params.GCP_PROJECT_ID}/${params.GCR_IMAGE_NAME}:1.0|gcr.io/${params.GCP_PROJECT_ID}/${params.GCR_IMAGE_NAME}:${params.APP_VERSION}|g' deployment.yaml"
                    sh "kubectl apply -f deployment.yaml"
                    sh "kubectl apply -f service.yaml"
                    echo "Deployment completed. Waiting for external IP of the service..."
                    def externalIp = ""
                    timeout(time: 5, unit: 'MINUTES') {
                        while (externalIp == null || externalIp == "") {
                            sleep 10
                            try {
                                externalIp = sh(script: "kubectl get service ${params.GCR_IMAGE_NAME} -o jsonpath='{.status.loadBalancer.ingress[0].ip}'", returnStdout: true).trim()
                            } catch (Exception e) {
                                echo "Error getting external IP, retrying... (${e.message})"
                            }
                        }
                    }
                    echo "Application accessible at http://${externalIp}"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs for more details.'
        }
    }
}