#!/usr/bin/env bash
echo "Building microservice JARs through the modules"
echo ""
cd ../
mvn clean install

echo ""

echo "Copying fat JARs to be Dockerized"

echo ""

echo "Copying quote-generator fat JAR to project folder"
cd quote-generator
cp target/quote-generator-1.0-SNAPSHOT.jar .
echo "Ok"

echo ""

echo "Copying micro-trader-dashboard fat JAR to project folder"
cd ../micro-trader-dashboard
cp target/micro-trader-dashboard-1.0-SNAPSHOT.jar .
echo "Ok"

echo ""

echo "Copying portfolio-service fat JAR to project folder"
cd ../portfolio-service
cp target/portfolio-service-1.0-SNAPSHOT.jar .
echo "Ok"

echo ""

echo "Copying compulsive-traders fat JAR to project folder"
cd ../compulsive-traders
cp target/compulsive-traders-1.0-SNAPSHOT.jar .
echo "Ok"

echo ""

echo "Building Docker images"

echo ""

echo "Build docker image quote-generator"

echo ""

cd ../quote-generator
docker build -t sidartasilva/quote-generator:latest .
docker push sidartasilva/quote-generator

echo ""

echo "Build docker image micro-trader-dashboard"

echo ""

cd ../micro-trader-dashboard
docker build -t sidartasilva/micro-trader-dashboard:latest .
docker push sidartasilva/micro-trader-dashboard

echo ""

echo "Build docker image portfolio-service"

echo ""

cd ../portfolio-service
docker build -t sidartasilva/portfolio-service:latest .
docker push sidartasilva/portfolio-service

echo ""

echo "Build docker image compulsive-traders"

echo ""

cd ../compulsive-traders
docker build -t sidartasilva/compulsive-traders:latest .
docker push sidartasilva/compulsive-traders

echo ""

echo "Well done!"