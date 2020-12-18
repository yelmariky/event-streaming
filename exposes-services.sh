#!/bin/sh
### account-web
kubectl port-forward svc/account-web 9987:80
### order-web
kubectl port-forward svc/order-web 8899:80
### payment-web to webhook
kubectl port-forward svc/payment-web 8886:80
