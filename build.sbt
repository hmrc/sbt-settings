lazy val sbtSettings = Project("sbt-settings", file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    majorVersion := 4,
    isPublicArtefact := true,
    sbtPlugin := true,
    scalaVersion := "2.12.18",
    resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns),
    addSbtPlugin("uk.gov.hmrc"       % "sbt-git-stamp" % "6.3.0"),
    addSbtPlugin("com.eed3si9n"      % "sbt-buildinfo" % "0.11.0"),
    addSbtPlugin("de.heikoseeberger" % "sbt-header"    % "5.10.0"),
    publishArtifact := true,
    SbtBuildInfo(),
    organization := "uk.gov.hmrc",
    console / initialCommands := "import " + organization + "._",
    isSnapshot := version.value.matches("([\\w]+\\-SNAPSHOT)|([\\.\\w]+)\\-([\\d]+)\\-([\\w]+)"),
    GitStampPlugin.gitStampSettings
  )
