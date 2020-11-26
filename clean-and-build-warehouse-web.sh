#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
kubectl delete -f warehouse-web.yaml
docker rmi -f warehouse-web:latest
docker rmi -f warehouse-web:1.0
cd $BASE_PATH/warehouse/warehouse-web/target/docker/
docker build . -t warehouse-web:1.0
docker tag warehouse-web:1.0 warehouse-web:latest
cd $BASE_PATH/deploy
kubectl apply -f warehouse-web.yaml
#kubectl logs -f warehouse-web