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

  val pluginName = "sbt-utils"

  lazy val sbtUtils = Project(pluginName, file("."), settings = Seq(
      sbtPlugin := true,
      organization := "uk.gov.hmrc",
      scalaVersion := "2.10.4",
      resolvers += Resolver.url("hmrc-sbt-plugin-releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
      addSbtPlugin("uk.gov.hmrc" % "sbt-git-stamp" % "5.1.0"),
      addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.3.2"),
      publishArtifact := true,
      publishArtifact in Test := false
  ) ++ SbtBuildInfo() ++ DefaultBuildSettings.defaultSettings() ++ BuildDescriptionSettings()
  ).enablePlugins(SbtGitVersioning)
}

object SbtGitInfo {

  lazy val gitInfo:Seq[(String, String)] = gitStamp.toSeq
}


//object SbtBuildInfo {
//
//  import sbtbuildinfo.Plugin._
//
//  def apply() = buildInfoSettings ++
//    Seq(
//      buildInfo <<= (sourceManaged in Compile,
//        buildInfoObject, buildInfoPackage, buildInfoKeys, thisProjectRef, state, streams) map {
//        (dir, obj, pkg, keys, ref, state, taskStreams) =>
//          Seq(BuildInfo(dir / "sbt-buildinfo", obj, pkg, keys, ref, state, taskStreams.cacheDirectory))
//      },
//      sourceGenerators in Compile <+= buildInfo,
//      buildInfoPackage := organization.value,
//      buildInfoKeys := Seq[BuildInfoKey](
//        name,
//        version,
//        scalaVersion,
//        sbtVersion,
//        libraryDependencies,
//        BuildInfoKey.action("builtAt") {now}) ++ SbtGitInfo.gitInfo.map {toBuildInfo}
//    )
//
//  private def now: String = {
//    val dtf = new java.text.SimpleDateFormat("yyyy-MM-dd")
//    dtf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"))
//    dtf.format(new java.util.Date())
//  }
//
//  private def toBuildInfo: ((String, String)) => Plugin.BuildInfoKey.Entry[String] = {
//    t =>
//      BuildInfoKey.action(t._1.replaceAll("-", "")) {
//        t._2
//      }
//  }
//}

//object DefaultBuildSettings {
//
//  lazy val targetJvm = settingKey[String]("The version of the JVM the build targets")
//
//  lazy val scalaSettings : Seq[Setting[_]] = {
//    targetJvm := "jvm-1.8"
//
//    Seq(
//      scalaVersion := "2.11.6",
//      scalacOptions ++= Seq(
//        "-unchecked",
//        "-deprecation",
//        "-Xlint",
//        "-language:_",
//        "-target:" + targetJvm.value,
//        "-Xmax-classfile-name", "100",
//        "-encoding", "UTF-8"
//      )
//    )
//  }
//
//  def defaultSettings(addScalaTestReports: Boolean = true) : Seq[Setting[_]] = {
//    val ds = Seq(
//      organization := "uk.gov.hmrc",
//      initialCommands in console := "import " + organization + "._",
//      parallelExecution in Test := false,
//      fork in Test := false,
//      isSnapshot := version.value.matches("([\\w]+\\-SNAPSHOT)|([\\.\\w]+)\\-([\\d]+)\\-([\\w]+)")
//    ) ++ gitStampInfo
//
//    if (addScalaTestReports) ds ++ addTestReportOption(Test) else ds
//  }
//
//  def addTestReportOption(conf: Configuration, directory: String = "test-reports") = {
//    val testResultDir = "target/" + directory
//    testOptions in conf += Tests.Argument("-o", "-u", testResultDir, "-h", testResultDir + "/html-report")
//  }
//
//  private def gitStampInfo() = {
//    Seq(packageOptions <+= (packageOptions in Compile, packageOptions in packageBin) map {(a, b) =>
//      ManifestAttributes(gitStamp.toSeq: _*)})
//  }
//}



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
        <connection>scm:git@github.com:hmrc/sbt-utils.git</connection>
        <developerConnection>scm:git@github.com:hmrc/sbt-utils.git</developerConnection>
        <url>git@github.com:hmrc/sbt-utils.git</url>
      </scm>
      <developers>
        <developer>
          <id>duncancrawford</id>
          <name>Duncan Crawford</name>
          <url>http://www.equalexperts.com</url>
        </developer>
      </developers>
}
