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
package uk.gov.hmrc

import _root_.sbt._
import sbt.Keys._
import sbt.Configuration

object DefaultBuildSettings {

  import uk.gov.hmrc.GitStampPlugin._

  lazy val targetJvm = settingKey[String]("The version of the JVM the build targets")

  lazy val scalaSettings : Seq[Setting[_]] = {
    targetJvm := "jvm-1.8"

    Seq(
      scalaVersion := "2.11.2",
      scalacOptions ++= Seq(
        "-unchecked",
        "-deprecation",
        "-Xlint",
        "-language:_",
        "-target:" + targetJvm.value,
        "-Xmax-classfile-name", "100",
        "-encoding", "UTF-8"
      )
    )
  }

  def defaultSettings(addScalaTestReports: Boolean = true) : Seq[Setting[_]] = {
    val ds = Seq(
      organization := "uk.gov.hmrc",
      initialCommands in console := "import " + organization + "._",
      parallelExecution in Test := false,
      fork in Test := false,
      isSnapshot := version.value.contains("SNAPSHOT")
    ) ++ gitStampSettings

    if (addScalaTestReports) ds ++ addTestReportOption(Test) else ds
  }

  def addTestReportOption(conf: Configuration, directory: String = "test-reports") = {
    val testResultDir = "target/" + directory
    testOptions in conf += Tests.Argument("-o", "-u", testResultDir, "-h", testResultDir + "/html-report")
  }
}

