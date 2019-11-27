resolvers += Resolver.url("hmrc-sbt-plugin-releases",
  url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "1.20.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "0.20.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-stamp" % "5.8.0-SNAPSHOT")
