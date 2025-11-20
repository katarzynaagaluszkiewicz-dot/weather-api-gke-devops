# Klaster GKE
resource "google_container_cluster" "primary" {
  name     = var.gke_cluster_name
  location = var.region
  deletion_protection = false

  network    = google_compute_network.main.id
  subnetwork = google_compute_subnetwork.main.name

  remove_default_node_pool = true
  initial_node_count       = 1

  ip_allocation_policy {}
}

# Node pool
resource "google_container_node_pool" "primary_nodes" {
  name       = var.gke_node_pool_name
  location   = google_container_cluster.primary.location
  cluster    = google_container_cluster.primary.name
  node_count = var.gke_node_count

  node_config {
    machine_type = "e2-micro"
    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform",
    ]
  }
}