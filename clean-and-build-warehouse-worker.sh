#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
kubectl delete -f warehouse-worker.yaml
docker rmi -f warehouse-worker:latest
docker rmi -f warehouse-worker:1.0
cd $BASE_PATH/warehouse/warehouse-worker/target/docker/
docker build . -t warehouse-worker:1.0
docker tag warehouse-worker:1.0 warehouse-worker:latest
cd $BASE_PATH/deploy
kubectl apply -f warehouse-worker.yaml
#kubectl logs -f warehouse-worker