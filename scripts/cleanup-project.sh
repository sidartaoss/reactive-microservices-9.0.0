#!/usr/bin/env bash
echo "Cleaning up target folder into the modules"
echo ""
cd ../
mvn clean

echo ""

echo "Cleaning up fat JARs"

echo ""

echo "Cleaning up fat JAR into quote-generator"
cd quote-generator
rm -rf quote-generator-1.0-SNAPSHOT.jar
echo "Ok"

echo ""

echo "Cleaning up fat JAR into micro-trader-dashboard"
cd ../micro-trader-dashboard
rm -rf micro-trader-dashboard-1.0-SNAPSHOT.jar
echo "Ok"

echo ""

echo "Cleaning up fat JAR into portfolio-service"
cd ../portfolio-service
rm -rf portfolio-service-1.0-SNAPSHOT.jar
echo "Ok"

echo ""

echo "Cleaning up fat JAR into compulsive-traders"
cd ../compulsive-traders
rm -rf compulsive-traders-1.0-SNAPSHOT.jar
echo "Ok"

echo ""

echo "Cleaning up current Kubernetes resources"

echo ""

echo "Cleaning up current Pods and Deployments"
kubectl delete deployment --all

echo ""

echo "Cleaning up current Ingresses"
kubectl delete ingress --all

echo ""

echo "Cleaning up current ConfigMaps"
kubectl delete configmap --all

echo ""

echo "Cleaning up current Services"
kubectl delete svc quote-generator micro-trader-dashboard portfolio-service compulsive-traders

echo ""

echo "Deleting Docker images"

echo ""

docker rmi sidartasilva/quote-generator:latest
docker rmi sidartasilva/micro-trader-dashboard:latest
docker rmi sidartasilva/portfolio-service:latest
docker rmi sidartasilva/compulsive-traders:latest

echo ""

docker images

echo ""

echo "Well done!"