val pluginName = "sbt-settings"

lazy val sbtSettings = Project(pluginName, file("."))
  .enablePlugins(SbtGitVersioning, SbtArtifactory, BuildInfoPlugin)
  .settings(
    majorVersion := 4,
    makePublicallyAvailableOnBintray := true,
    sbtPlugin := true,
    scalaVersion := "2.10.7",
    crossSbtVersions := Vector("0.13.18", "1.3.4"),
    resolvers += Resolver.url("hmrc-sbt-plugin-releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
    addSbtPlugin("uk.gov.hmrc" % "sbt-git-stamp" % "6.0.0"),
    addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0"),
    publishArtifact := true,
    publishArtifact in Test := false,
    SbtBuildInfo(),
    DefaultBuildSettings.defaultSettings()
  )