#!/bin/sh
docker system prune

export BASE_PATH=`pwd`
ecport BASE_OUT=$BASE_PATH/target
cd $BASE_PATH/helm
echo 'install kafka in progress ....'
helm install mykafka-v1 -f kafka.values bitnami/kafka > $BASE_OUT/install-kafka.txt
echo 'install kafka FIN ....'
echo 'install consul in progress ....'
helm -f consul.values install my-consul-v1 hashicorp/consul > $BASE_OUT/install-consul.txt
echo 'install consul FIN ....'
echo 'install zipkin in progress ....'
helm -f zipkin.values install my-zipkin carlosjgp/zipkin > $BASE_OUT/install-zipkin.txt
echo 'install zipkin FIN ....'

echo 'install istio in progress ....'
export PATH_ISTIO=/d/appli/istio-1.7.5
export VERSION_ISTIO="1.7.5"
kubectl create namespace istio-system
#helm install --namespace istio-system istio-base $PATH_ISTIO/base
#helm install --namespace istio-system istiod $PATH_ISTIO/istio-control/istio-discovery --set global.hub="docker.io/istio" --set global.tag="1.7.5"
#helm install --namespace istio-system istio-ingress $PATH_ISTIO/gateways/istio-ingress \
 #   --set global.hub="docker.io/istio" --set global.tag=$VERSION_ISTIO
 istioctl install --set profile=demo -y
 kubectl label namespace default istio-injection=enabled
export INGRESS_HOST=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].hostname}')
export INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="https")].port}')
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT
kubectl apply -f $PATH_ISTIO/samples/addons

echo 'install istio FIN ....'


