pipeline {
    agent any

    environment {
        EC2_USER = "ec2-user"
        EC2_HOST = "3.106.224.210" // 🚨 Make sure this is the correct and current IP
        PEM_KEY  = "/tmp/aws_jenkins_pem.pem"
        REPO     = "https://github.com/syiifahusna/certificateGenerator.git"
        BRANCH   = "testjenkins"
        PROJECT  = "certificateGenerator"
        IMAGE_NAME = "certificate-generator"
        CONTAINER_NAME = "certificate-generator-container"
        CONTAINER_PORT = "8081" // Added for clarity
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
                    # Stop the existing container if it is running
                    if [ \$(sudo docker ps -q -f name=${CONTAINER_NAME}) ]; then
                        echo "Stopping container ${CONTAINER_NAME}..."
                        sudo docker stop ${CONTAINER_NAME}
                    fi

                    # Remove the existing container if it exists
                    if [ \$(sudo docker ps -a -q -f name=${CONTAINER_NAME}) ]; then
                        echo "Removing container ${CONTAINER_NAME}..."
                        sudo docker rm -f ${CONTAINER_NAME}
                    fi

                    echo "Running new container ${CONTAINER_NAME} from image ${IMAGE_NAME}:latest on port ${CONTAINER_PORT}"
                    # Run new container, mapping the specified port
                    # The first ${CONTAINER_PORT} is the host port, the second is the container port
                    sudo docker run -d -p ${CONTAINER_PORT}:${CONTAINER_PORT} --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest
                '
                """
            }
        }
    }
}