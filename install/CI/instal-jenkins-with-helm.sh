#!/bin/sh
#instal jenkins with helm
export chart=bitnami/jenkins
#helm repo add bitnami https://charts.bitnami.com/bitnami
helm install jenkins -n jenkins -f jenkins.values $chart
##echo Username: user
echo Password: $(kubectl get secret --namespace jenkins jenkins -o jsonpath="{.data.jenkins-password}" | base64 --decode)

#kubectl --namespace jenkins port-forward svc/jenkins 8080:8080