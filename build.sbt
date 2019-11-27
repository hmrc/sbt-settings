val pluginName = "sbt-settings"

lazy val sbtSettings = Project(pluginName, file("."))
  .enablePlugins(SbtGitVersioning, SbtArtifactory, BuildInfoPlugin)
  .settings(
    majorVersion := 3,
    makePublicallyAvailableOnBintray := true,
    sbtPlugin := true,
    scalaVersion := "2.10.4",
    resolvers += Resolver.url("hmrc-sbt-plugin-releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
    addSbtPlugin("uk.gov.hmrc" % "sbt-git-stamp" % "5.7.0"),
    addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0"),
    publishArtifact := true,
    publishArtifact in Test := false,
    SbtBuildInfo(),
    DefaultBuildSettings.defaultSettings()
  )