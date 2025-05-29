pipeline {
    agent any

    environment {
        EC2_HOST = "ec2-user@3.106.224.210"
        SSH_KEY = "/tmp/aws_jenkins_pem.pem" // Adjust accordingly
        GIT_BRANCH = "testjenkins"
        GIT_REPO = "https://github.com/syiifahusna/certificateGenerator.git" // Use HTTPS or SSH
    }

    stages {
        stage('Clone and Build on EC2') {
            steps {
                sh """
                ssh -i ${SSH_KEY} -o StrictHostKeyChecking=no ${EC2_HOST} '
                    rm -rf certificateGenerator &&
                    git clone -b ${GIT_BRANCH} ${GIT_REPO} &&
                    cd certificateGenerator &&
                    docker rmi -f certificate-generator:latest || true &&
                    docker build -t certificate-generator:latest .
                '
                """
            }
        }
    }
}
