val pluginName = "sbt-settings"

lazy val sbtSettings = Project(pluginName, file("."))
  .enablePlugins(SbtGitVersioning, SbtArtifactory)
  .settings(
    majorVersion := 3,
    makePublicallyAvailableOnBintray := true,
    sbtPlugin := true,
    scalaVersion := "2.10.4",
    resolvers += Resolver.url("hmrc-sbt-plugin-releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
    addSbtPlugin("uk.gov.hmrc" % "sbt-git-stamp" % "5.3.0"),
    addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2"),
    publishArtifact := true,
    publishArtifact in Test := false,
    SbtBuildInfo(),
    DefaultBuildSettings.defaultSettings()
  )