#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
echo ' deploy account worker in progress ..... '
kubectl delete -f account-worker.yaml
docker rmi -f account-worker:latest
docker rmi -f account-worker:1.0
cd $BASE_PATH/account/account-worker/target/docker/
docker build . -t account-worker:1.0
docker tag account-worker:1.0 account-worker:latest
cd $BASE_PATH/deploy
kubectl apply -f account-worker.yaml
#kubectl logs -f account-worker