val pluginName = "sbt-settings"

lazy val sbtSettings = Project(pluginName, file("."))
  .enablePlugins(SbtGitVersioning, SbtArtifactory, BuildInfoPlugin)
  .settings(
    majorVersion := 4,
    makePublicallyAvailableOnBintray := true,
    sbtPlugin := true,
    scalaVersion := "2.12.10",
    crossSbtVersions := Vector("0.13.18", "1.3.4"),
    resolvers += Resolver.bintrayIvyRepo("hmrc", "sbt-plugin-releases"),
    addSbtPlugin("uk.gov.hmrc"       % "sbt-git-stamp" % "6.0.0"),
    addSbtPlugin("com.eed3si9n"      % "sbt-buildinfo" % "0.7.0"),
    addSbtPlugin("de.heikoseeberger" % "sbt-header"    % "4.1.0"),
    publishArtifact := true,
    publishArtifact in Test := false,
    SbtBuildInfo(),
    DefaultBuildSettings.defaultSettings()
  )
