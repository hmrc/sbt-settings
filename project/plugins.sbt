resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)

addSbtPlugin("uk.gov.hmrc"  % "sbt-git-versioning" % "2.4.0")
addSbtPlugin("uk.gov.hmrc"  % "sbt-git-stamp"      % "6.2.0")
addSbtPlugin("uk.gov.hmrc"  % "sbt-setting-keys"   % "0.3.0")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo"      % "0.7.0")
