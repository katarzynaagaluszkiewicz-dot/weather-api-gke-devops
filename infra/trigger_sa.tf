resource "google_service_account" "cloudbuild_trigger_sa" {
  account_id   = "cloudbuild-trigger-sa"
  display_name = "Cloud Build Trigger Service Account"
}

resource "google_project_iam_member" "trigger_sa_builds_editor" {
  project = var.project_id
  role    = "roles/cloudbuild.builds.editor"
  member  = "serviceAccount:${google_service_account.cloudbuild_trigger_sa.email}"
}

resource "google_project_iam_member" "trigger_sa_storage_admin" {
  project = var.project_id
  role    = "roles/storage.admin"
  member  = "serviceAccount:${google_service_account.cloudbuild_trigger_sa.email}"
}

resource "google_project_iam_member" "trigger_sa_ar_writer" {
  project = var.project_id
  role    = "roles/artifactregistry.writer"
  member  = "serviceAccount:${google_service_account.cloudbuild_trigger_sa.email}"
}

resource "google_project_iam_member" "trigger_sa_cluster_viewer" {
  project = var.project_id
  role    = "roles/container.clusterViewer"
  member  = "serviceAccount:${google_service_account.cloudbuild_trigger_sa.email}"
}

resource "google_project_iam_member" "trigger_container_developer" {
  project = var.project_id
  role    = "roles/container.developer"
  member  = "serviceAccount:${google_service_account.cloudbuild_trigger_sa.email}"
}