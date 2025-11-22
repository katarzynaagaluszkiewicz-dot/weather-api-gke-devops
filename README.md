# Weather API - GKE + CI/CD Pipeline

## Opis projektu

Weather API to prosta aplikacja REST w Javie (Spring Boot), która udostępnia dane pogodowe.  
Projekt demonstruje pełny pipeline DevOps na Google Cloud Platform (GCP) z wykorzystaniem:

- Google Kubernetes Engine (GKE) do uruchomienia aplikacji w kontenerach
- Cloud Build do automatycznego budowania obrazów Docker i deploymentu
- Artifact Registry jako repozytorium obrazów Docker
- GitOps: automatyczne wdrożenia po pushu do repozytorium
- Cloud Monitoring (Logging i Metrics) do monitorowania aplikacji
- IAM do kontroli dostępu

---

# Cluster GKE

This repository contains a Terraform configuration (main.tf), that provisions a basic Kubernetes environment on Google Cloud Platform (GCP) using Google Kubernetes Engine (GKE).

## The Cluster configuration:

- VPC network – custom VPC named 'gke-network' with automatic subnet creation disabled.
- Subnetwork – subnet 'gke-subnet' in region 'us-central1' with CIDR range '10.0.0.0/16', attached to the custom VPC.
- GKE cluster – regional Kubernetes cluster 'moj-gke-klaster' in 'us-central1', using the custom VPC and subnet.
- Node pool – node pool 'primary-nodes' with 'e2-small' instances, attached to the cluster.


## Jak uruchomić lokalnie

1. Sklonuj repozytorium:

```bash
git clone https://github.com/joannawalach1/weather-api-gke-devops.git




