#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
kubectl delete -f order-web.yaml
docker rmi -f order-web:latest
docker rmi -f order-web:1.0
cd $BASE_PATH/order/order-web/target/docker/
docker build . -t order-web:1.0
docker tag order-web:1.0 order-web:latest
cd $BASE_PATH/deploy
kubectl apply -f order-web.yaml
#kubectl logs -f order-web