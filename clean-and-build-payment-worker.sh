#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
kubectl delete -f payment-worker.yaml
docker rmi -f payment-worker:latest
docker rmi -f payment-worker:1.0
cd $BASE_PATH/payment/payment-worker/target/docker/
docker build . -t payment-worker:1.0
docker tag payment-worker:1.0 payment-worker:latest
cd $BASE_PATH/deploy
kubectl apply -f payment-worker.yaml
#kubectl logs -f payment-worker