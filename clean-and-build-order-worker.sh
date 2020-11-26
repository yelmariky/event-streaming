#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
kubectl delete -f order-worker.yaml
docker rmi -f order-worker:latest
docker rmi -f order-worker:1.0
cd $BASE_PATH/order/order-worker/target/docker/
docker build . -t order-worker:1.0
docker tag order-worker:1.0 order-worker:latest
cd $BASE_PATH/deploy
kubectl apply -f order-worker.yaml
#kubectl logs -f order-worker