#!/bin/sh
kubectl create -f istio-destination-rules.yaml
kubectl create -f istio-gateways.yaml
kubectl create -f istio-vs-account.yaml
kubectl create -f istio-vs-order.yaml
kubectl create -f istio-vs-payment.yaml
kubectl create -f istio-vs-warehouse.yaml
##curl -v http://localhost/v1/orders/1 -H "Host: order.dev"


