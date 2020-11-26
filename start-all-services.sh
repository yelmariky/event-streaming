#!/bin/sh


#account
sh clean-and-build-account-web.sh
sh clean-and-build-account-worker.sh
#warehouse
sh clean-and-build-warehouse-web.sh
sh clean-and-build-warehouse-worker.sh
#order
sh clean-and-build-order-web.sh
sh clean-and-build-order-worker.sh
#payment
sh clean-and-build-payment-web.sh
sh clean-and-build-payment-worker.sh
