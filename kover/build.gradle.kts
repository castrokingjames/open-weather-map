plugins {
  alias(libs.plugins.kover)
}

dependencies {
  kover(projects.model)
  kover(projects.domain)
  kover(projects.data)
  kover(projects.datasource.remote)
  kover(projects.datasource.local)
  kover(projects.feature.dashboard)
}

kover.reports {
  filters {
    excludes.classes(
      "io.github.castrokingjames.datasource.local.database.*",
      "*ComposableSingletons*",
      "*lambda*",
      "*inlined*",
    )
    excludes.annotatedBy(
      "kotlinx.serialization.Serializable",
    )
  }
}
