#!/usr/bin/env bash
echo "Deploying microservices to Kubernetes through the YAMLs"

echo ""

echo "Deploying config-map"
cd ../quote-generator
kubectl create configmap app-config --from-file=src/main/resources/config.json

echo ""

echo "Deploying the microservices application"
cd ../yaml
kubectl apply -f configmap-global.yaml
kubectl apply -f deployment-quote-generator.yaml
sleep 20;
kubectl apply -f deployment-micro-trader-dashboard.yaml
sleep 20;
kubectl apply -f deployment-portfolio-service.yaml
sleep 20;
kubectl apply -f deployment-compulsive-traders.yaml
kubectl apply -f ingress-quote-generator.yaml
kubectl apply -f ingress-micro-trader-dashboard.yaml

echo ""

echo "Well done!"