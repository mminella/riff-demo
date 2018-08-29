# From a fresh minikube install

Setup Minikube (https://projectriff.io/docs/getting-started-with-knative-riff-on-minikube/)
1.  Start minikube (delete before if re-installing)
    ```
    minikube start --memory=8192 --cpus=4 \
    --kubernetes-version=v1.10.5 \
    --vm-driver=hyperkit \
    --bootstrapper=kubeadm \
    --extra-config=controller-manager.cluster-signing-cert-file="/var/lib/localkube/certs/ca.crt" \
    --extra-config=controller-manager.cluster-signing-key-file="/var/lib/localkube/certs/ca.key" \
    --extra-config=apiserver.admission-control="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
    ```

2.  Uninstall riff (if previously installed)
    ```
    riff system uninstall --istio --force
    ```

3.  Install riff
    ```
    riff system install --node-port
    ```

4.  Apply DockerHub creds
    ```
    kubectl apply -f dockerhub-push-credentials.yaml
    ```

5.  Edit scale to zero threshold to be 30 seconds.

    5.1. Launch dashbaord
    ```
    minikube dashboard
    ```

    5.2. Select knative-serving namespace

    5.3. Select the config-autoscaler config map

    5.4. Update `scale-to-zero-threshold` to be `30s`.


# Restarting the demo


1.  Start minikube
    ```
    minikube start --memory=8192 --cpus=4 \
    --kubernetes-version=v1.10.5 \
    --vm-driver=hyperkit \
    --bootstrapper=kubeadm \
    --extra-config=controller-manager.cluster-signing-cert-file="/var/lib/localkube/certs/ca.crt" \
    --extra-config=controller-manager.cluster-signing-key-file="/var/lib/localkube/certs/ca.key" \
    --extra-config=apiserver.admission-control="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
    ```

2. Double check kubectl is pointing to that cluster
    ```
    kubectl config current-context
    ```

3.  Launch watch
    ```
    watch -n 1 kubectl get pod --all-namespaces
    ```
    
4. Export `DOCKER_ID`
    ```
    export DOCKER_ID=foobar	
	```

4.  Create Node function
    ```
    # First time (takes about 2-3 minutes)

    riff function create node square \
      --git-repo https://github.com/trisberg/node-fun-square.git \
      --artifact square.js \
      --image $DOCKER_ID/node-fun-square


    # Any time after that
    riff service create square --image mminella/node-fun-square
    ```

5. Export minikube IP
    ```
    export MINIKUBE_IP=$(minikube ip)
    ```

6.  Call Node function (should respond with 100)
    ```
    curl \
         -w '\n' \
         -H 'Host: square.default.example.com' \
         -H 'Content-Type: text/plain' \
         http://$MINIKUBE_IP:32380 \
         -d 10
    ```

7.  Delete Node function
    ```
    riff service delete square
    ```

8.  Create java function
    ```
    riff service create reverse --image mminella/riff-demo:0.0.1-SNAPSHOT
    ```

9.  Call java function
    ```
    curl \
         -w '\n' \
         -H 'Host: reverse.default.example.com' \
         -H 'Content-Type: text/plain' \
         http://$MINIKUBE_IP:32380 \
         -d '!ytinummoC gnirpS ,olleH'
    ```

10.  Delete java function
    ```
    riff service delete reverse
    ```
