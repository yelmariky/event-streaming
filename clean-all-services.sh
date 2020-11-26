#!/bin/sh
export BASE_PATH=`pwd`
#clean vs and gateway 
kubectl delete -f istio-destination-rules.yaml
kubectl delete -f istio-gateways.yaml
kubectl delete -f istio-vs-account.yaml
kubectl delete -f istio-vs-order.yaml
kubectl delete -f istio-vs-payment.yaml
kubectl delete -f istio-vs-warehouse.yaml

cd $BASE_PATH/deploy
kubectl delete -f .

#account
docker rmi -f account-web:latest
docker rmi -f account-web:1.0
docker rmi -f account-worker:latest
docker rmi -f account-worker:1.0
#warehouse
docker rmi -f warehouse-web:latest
docker rmi -f warehouse-web:1.0
docker rmi -f warehouse-worker:1.0
docker rmi -f warehouse-worker:latest
#order
docker rmi -f order-web:latest
docker rmi -f order-web:1.0
docker rmi -f order-worker:1.0
docker rmi -f order-worker:latest
#payment
docker rmi -f payment-web:latest
docker rmi -f payment-web:1.0
docker rmi -f payment-worker:1.0
docker rmi -f payment-worker:latest
