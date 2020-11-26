#!/bin/sh
export BASE_PATH=`pwd`
cd $BASE_PATH/deploy
kubectl delete -f account-web.yaml
docker rmi -f account-web:latest
docker rmi -f account-web:1.0
cd $BASE_PATH/account/account-web/target/docker/
docker build . -t account-web:1.0
docker tag account-web:1.0 account-web:latest
cd $BASE_PATH/deploy
kubectl apply -f account-web.yaml
#kubectl logs -f account-web