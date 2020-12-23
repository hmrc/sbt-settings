resolvers += Resolver.bintrayIvyRepo("hmrc", "sbt-plugin-releases")
resolvers += Resolver.bintrayRepo("hmrc", "releases")

addSbtPlugin("uk.gov.hmrc"  % "sbt-git-versioning" % "2.2.0")
addSbtPlugin("uk.gov.hmrc"  % "sbt-artifactory"    % "1.11.0")
addSbtPlugin("uk.gov.hmrc"  % "sbt-git-stamp"      % "6.2.0")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo"      % "0.7.0")
