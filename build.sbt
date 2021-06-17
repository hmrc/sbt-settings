lazy val sbtSettings = Project("sbt-settings", file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    majorVersion := 4,
    isPublicArtefact := true,
    sbtPlugin := true,
    scalaVersion := "2.12.14",
    crossSbtVersions := Vector("0.13.18", "1.3.4"),
    resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns),
    addSbtPlugin("uk.gov.hmrc"       % "sbt-git-stamp" % "6.2.0"),
    addSbtPlugin("com.eed3si9n"      % "sbt-buildinfo" % "0.7.0"),
    addSbtPlugin("de.heikoseeberger" % "sbt-header"    % "4.1.0"),
    publishArtifact := true,
    publishArtifact in Test := false,
    SbtBuildInfo(),
    DefaultBuildSettings.defaultSettings()
  )
