#!/usr/bin/env groovy

// Declare the function for the Jenkins pipeline
def call(String githubRepoUrl, String branch, String dockerImage, String kubeNamespace) {
    pipeline {
        agent any
        
       environment {
    
           DOCKER_IMAGE = "${dockerImage}"
    
           GITHUB_REPO = "${githubRepoUrl}"
    
           GIT_BRANCH = "${branch}"
    
           K8S_NAMESPACE = "${kubeNamespace}"
           
                    }

        stages {
            stage('Checkout') {
                steps {
                    // Clone the GitHub repository
                    script {
                        echo "Cloning GitHub repository..."
                        checkout([$class: 'GitSCM', branches: [[name: "*/${GIT_BRANCH}"]], 
                            userRemoteConfigs: [[url: "${GITHUB_REPO}"]]])
                    }
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        echo "Building Docker image for Next.js..."
                        sh """
                            docker build -t ${DOCKER_IMAGE} .
                            docker push ${DOCKER_IMAGE}
                        """
                    }
                }
            }

            stage('Deploy to Kubernetes') {
                steps {
                    script {
                        echo "Deploying Next.js project to Kubernetes..."
                        // Call Ansible playbook for deployment
                        ansiblePlaybook(
                            playbook: 'ansible/deploy_nextjs.yml',
                            inventory: 'ansible/inventory.ini',  // Assuming inventory file is in the repo
                            extraVars: [
                                k8s_namespace: K8S_NAMESPACE,
                                docker_image: DOCKER_IMAGE
                            ]
                        )
                    }
                }
            }
        }
    }
}
