pipeline {
    agent any

    environment {
        EC2_USER = "ec2-user"
        EC2_HOST = "3.106.224.210" // 🚨 Make sure this is the correct and current IP
        PEM_KEY  = "/tmp/aws_jenkins_pem.pem"
        REPO     = "https://github.com/syiifahusna/certificateGenerator.git"
        BRANCH   = "testjenkins"
        PROJECT  = "certificateGenerator"
        IMAGE_NAME = "certificate-generator" // This is the image name used in 'Build Docker Image'
        CONTAINER_NAME = "certificate-generator-container"
    }

    stages {
        stage('Clone Repository') {
            steps {
                sh """
                ssh -i ${PEM_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                    rm -rf ${PROJECT} &&
                    git clone -b ${BRANCH} ${REPO}
                '
                """
            }
        }

        stage('Build with Maven') {
            steps {
                sh """
                ssh -i ${PEM_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                    cd ${PROJECT} &&
                    mvn clean package
                '
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                ssh -i ${PEM_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                    cd ${PROJECT} &&
                    sudo docker rmi -f ${IMAGE_NAME}:latest || true &&
                    sudo docker build -t ${IMAGE_NAME}:latest .
                '
                """
            }
        }

        stage('Run Docker Container') {
            steps {
                sh """
                ssh -i ${PEM_KEY} -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                    # Remove existing container if it exists
                    if [ \$(sudo docker ps -a -q -f name=${CONTAINER_NAME}) ]; then
                        sudo docker rm -f ${CONTAINER_NAME}
                    fi
                    # Run new container
                    # Add any necessary port mappings or environment variables here
                    # For example, to run in detached mode and map port 8080:
                    # sudo docker run -d -p 8081:8081 --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
                    # For this example, running in detached mode:
                    sudo docker run -d --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
                '
                """
            }
        }
    }
}