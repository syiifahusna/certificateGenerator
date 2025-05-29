pipeline {
    agent any

    environment {
        EC2_USER = "ec2-user"
        EC2_HOST = "3.106.224.210"
        PEM_KEY  = "/tmp/aws_jenkins_pem.pem"
        REPO     = "https://github.com/syiifahusna/certificateGenerator.git"
        BRANCH   = "testjenkins"
        PROJECT  = "certificateGenerator"
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
                    sudo docker rmi -f certificate-generator:latest || true &&
                    sudo docker build -t certificate-generator:latest .
                '
                """
            }
        }
    }
}
