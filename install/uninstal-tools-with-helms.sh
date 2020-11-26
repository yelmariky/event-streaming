#!/bin/sh
export BASE_PATH=`pwd`
ecport BASE_OUT=$BASE_PATH/target
cd $BASE_PATH/helm
echo 'uninstall kafka in progress ....'
helm uninstall mykafka-v1 > $BASE_OUT/uninstall-kafka.txt
echo 'uninstall kafka FIN ....'
echo 'uninstall consul in progress ....'
helm uninstall my-consul-v1 > $BASE_OUT/uninstall-consul.txt
echo 'uninstall consul FIN ....'
echo 'uninstall zipkin in progress ....'
helm uninstall my-zipkin > $BASE_OUT/uninstall-zipkin.txt
echo 'uninstall zipkin FIN ....'
#kubectl logs -f account-web
