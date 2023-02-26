# Imperio Instances

### Aim

Imperio Instances is service meant to process and manage instances object related tasks. It manages gateway-forwarded requests about Instances, manages its database and procs terraform deployments in [Imperio Linode Services](../linode-services).

### Functional Testing
Since this is a web application, it is best tested in cloud. Testing locally is viable, but requires path parameters to be changed.

Tests are done via Skaffold.

1.) Run docker, and kubernetes cluster and have it configured in `~/.kube/config`. Rancher desktop is recommended as it runs both. Important note, is to have Traefik disabled here.

2.) Change [application.yml](src/main/resources/application.yml) to match current infrastructure.

3.) Look for istio gateway external ip. Get it by using use kubectl get `svc -n istio-system`
When IP is obtained, edit `/etc/hosts` and add ` <ip> 	instances.imperio ` in new line

3.) Hit 'skaffold dev' command and wait for spring app to start in pod

### Cloud Deployment
1.) Build docker image

2.) Ensure correct kubeconfig and proper profile is chosen by using `kubectl config get-context` and `kubectl config use-context <name>`

3.) Apply [imperio-instances](imperio-instances.yaml) file via `kubectl apply -f`
