# 🌦️ Weather API

### Project Report - DevOps Workflow with GKE, Cloud Build, and GitOps

---
 
## Project description

Weather API is a simple REST application written in Java (Spring Boot) that exposes weather data.
The project demonstrates a complete DevOps pipeline on Google Cloud Platform (GCP) using: 

- 🏗️ **Terraform** - infrastructure as code (IaC)
- 🧊 **Google Kubernetes Engine (GKE)** for running the application in containers
- 🔧 **Cloud Build** for automated Docket image building and deployment
- 📦 **Artifact Registry** as the Docket image repository
- 🔁 **GitOps approach**, enabling automatic deployments after each push to the repository
- 📊 **Cloud Monitoring** (Logging i Metrics) for observing the application and infrastructure
- 🔐 IAM for controlling access to the cluster and CI/CD pipeline

---

## 💻 How to Run the Application locally

1. Clone the repository:

```bash
git clone https://github.com/joannawalach1/weather-api-gke-devops.git
```
---
## 🏗️ Infrastructure Provisioning
All infrastructure required for the project is created automatically using **Terraform**, stored in the **`infra/`** directory.
The code is modular, clear, and fully version-controlled.

### 📁 Terraform project structure
The Terraform configuration is divided into modular files:

- **artifact-registry.tf** – Artifact Registry repository for Docker images  
- **backend.tf** – configuration for Terraform **remote backend storing the state in a GCS bucket**
- **gke.tf** – GKE cluster + node pool  
- **iam.tf** – IAM roles
- **network.tf** – VPC, subnet
- **main.tf** – root config  
- **variables.tf** – variable definitions  
- **trigger_sa.tf** - service account for the Cloud Build Trigger
- **terraform.tfvars** – variable values that doesnt need to be hidden

### 🚫 Not Managed by Terraform
Some resources was created manually:

- **Cloud Build Trigger** - GitHub connection requires browser-based OAuth authorization
- **GCS Bucket for Terraform Backend** - The bucket used to store Terraform state remotely must exist before Terraform is initialized
  
---

## 🗂️ Artifact Registry

Artifact Registry is used as the container image repository for the project.  
Cloud Build pushes each newly built Docker image into the repository using the commit's `SHORT_SHA` as the tag.  
This ensures versioned, traceable images and enables smooth rollouts on GKE.

---

## ⚙️ Cloud Build – Continuous Integration & Deployment
Cloud Build is responsible for the complete CI/CD process of the Weather API project. It is automatically triggered by a **push trigger** connected to the main branch of the repository. The trigger watches for code changes and, whenever a new commit is pushed, it launches the pipeline responsible for building and deploying the application. The trigger executes builds using a custom Service Account (cloudbuild-trigger-sa), to ensure that only minimal permissions are granted.

### 🔄 Pipeline Overview
The pipeline performs the following steps:

1. **Builds the Java application** using Maven  
2. **Creates a Docker image** tagged with the commit SHA  
3. **Pushes the image** to Artifact Registry  
4. **Authenticates with the GKE cluster**  
5. **Applies Kubernetes manifests** from the `k8s/` directory  
6. **Updates the deployment** to use the newly built image  
7. **Triggers a rolling update** in GKE  

This provides a clean **GitOps workflow**:  
> **commit → build → push → deploy**

### 🔐 IAM Permissions for CI/CD
To allow Cloud Build to push images and deploy them to GKE, several IAM roles were assigned to the Cloud Build service account:

- **roles/artifactregistry.writer** – allows pushing Docker images to Artifact Registry  
- **roles/container.developer** – allows deploying workloads to the GKE cluster (`kubectl apply`, updating Deployments)  
- **roles/container.clusterViewer** (optional) – grants read access to cluster metadata when required  

---

## ⚙️ **Kubernetes Manifests**

### 📦 **Deployment (`weather-api`)**
The Deployment defines how the application runs inside the GKE cluster.  
It uses the container image built in Cloud Build and includes two key health checks:

- **Readiness Probe**  
  Ensures the pod is ready to receive traffic  
  (`/actuator/health/readiness`)

- **Liveness Probe**  
  Ensures the application stays healthy and restarts if needed  
  (`/actuator/health/liveness`)

### 🌐 **Service (`LoadBalancer`)**
A Service of type **LoadBalancer** exposes the application publicly.

- Maps external port **80** to container port **8081**
- Automatically provisions a Google Cloud external load balancer
- Routes traffic to pods labeled `app: weather-api`

This provides a stable public endpoint for accessing the Weather API.

---

## 📊 Cloud Monitoring

The Logging and Monitoring Services are configured alongside the main cluster configuration, using terraform. They collect data regarding system components and the api server, and allow for their easy access and consolidated view using multiple dashboards. These dashboards include GKE Cluster Monitoring, GKE Compute Resources - Cluster View, GKE Compute Resources - Node View, GKE Compute Resources - Workload View, GKE Nodes and Pods - Cluster View, Logs Dashboard - which show the collected data in a user-friendly, aggregated manner.

---

## Billing Report

Billing report is included in the BILLING_REPORT.md file.

---

## 👩‍💻 Authors

- Gałuszkiewicz Katarzyna
- Kwolek Emilia
- Morawska Katarzyna
- Wałach Joanna
