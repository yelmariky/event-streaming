#!/bin/sh
#instal jenkins with helm
export chart=bitnami/jenkins
#helm repo add bitnami https://charts.bitnami.com/bitnami
helm install jenkins -n jenkins -f jenkins.values $chart
##echo Username: user
echo Password: $(kubectl get secret --namespace jenkins jenkins -o jsonpath="{.data.jenkins-password}" | base64 --decode)

#kubectl --namespace jenkins port-forward svc/jenkins 8080:80

#instal sonarqube
#helm repo add oteemocharts https://oteemo.github.io/charts
helm install -f sonarqube-values.yaml sonarqube oteemocharts/sonarqube
export POD_NAME=$(kubectl get pods --namespace default -l "app=sonarqube,release=sonarqube" -o jsonpath="{.items[0].metadata.name}")
##kubectl port-forward $POD_NAME 9000:9000 -n default