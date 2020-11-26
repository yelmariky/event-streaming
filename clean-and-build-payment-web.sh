#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
kubectl delete -f payment-web.yaml
docker rmi -f payment-web:latest
docker rmi -f payment-web:1.0
cd $BASE_PATH/payment/payment-web/target/docker/
docker build . -t payment-web:1.0
docker tag payment-web:1.0 payment-web:latest
cd $BASE_PATH/deploy
kubectl apply -f payment-web.yaml
#kubectl logs -f payment-web