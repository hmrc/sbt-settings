/*
 * Copyright 2014 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt.Configuration
import sbt.Package._
import sbt._
import Keys._
import uk.gov.hmrc.gitstamp.GitStamp._
import uk.gov.hmrc.versioning.SbtGitVersioning
import scala.collection.JavaConversions._
import sbtbuildinfo.Plugin


object PluginBuild extends Build {

  val pluginName = "sbt-settings"

  lazy val sbtSettings = Project(pluginName, file("."), settings = Seq(
      sbtPlugin := true,
      organization := "uk.gov.hmrc",
      scalaVersion := "2.10.4",
      resolvers += Resolver.url("hmrc-sbt-plugin-releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
      addSbtPlugin("uk.gov.hmrc" % "sbt-git-stamp" % "5.3.0"),
      addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2"),
      publishArtifact := true,
      publishArtifact in Test := false
  ) ++ SbtBuildInfo() ++ DefaultBuildSettings.defaultSettings() ++ BuildDescriptionSettings()
  ).enablePlugins(SbtGitVersioning)
}

object SbtGitInfo {

  lazy val gitInfo:Seq[(String, String)] = gitStamp.toSeq
}

object BuildDescriptionSettings {

  def apply() =
    pomExtra := <url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git@github.com:hmrc/sbt-settings.git</connection>
        <developerConnection>scm:git@github.com:hmrc/sbt-settings.git</developerConnection>
        <url>git@github.com:hmrc/sbt-settings.git</url>
      </scm>
      <developers>
        <developer>
          <id>duncancrawford</id>
          <name>Duncan Crawford</name>
          <url>http://www.equalexperts.com</url>
        </developer>
      </developers>
}
