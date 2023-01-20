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
import sbt.Keys._
import sbt.{Configuration, _}
import uk.gov.hmrc.gitstamp.GitStampPlugin

object DefaultBuildSettings {

  lazy val targetJvm = settingKey[String]("The version of the JVM the build targets")

  lazy val scalaSettings : Seq[Setting[_]] = {
    targetJvm := "jvm-1.8"

    Seq(
      scalaVersion := "2.12.17",
      scalacOptions ++= Seq(
        "-unchecked",
        "-deprecation",
        "-Xlint",
        "-language:_",
        "-release", targetJvm.value.stripPrefix("jvm-").stripPrefix("1."),
        "-Xmax-classfile-name", "100",
        "-encoding", "UTF-8"
      )
    )
  }

  def defaultSettings(addScalaTestReports: Boolean = true) : Seq[Setting[_]] =
    Seq(
      organization := "uk.gov.hmrc",
      console / initialCommands := "import " + organization + "._",
      Test / parallelExecution := false,
      isSnapshot := version.value.matches("([\\w]+\\-SNAPSHOT)|([\\.\\w]+)\\-([\\d]+)\\-([\\w]+)")
    ) ++ GitStampPlugin.gitStampSettings ++
      (if (addScalaTestReports) addTestReportOption(Test) else Seq.empty)

  def addTestReportOption(conf: Configuration, directory: String = "test-reports") = {
    val testResultDir = "target/" + directory
    conf / testOptions += Tests.Argument("-o", "-u", testResultDir, "-h", testResultDir + "/html-report")
  }
}
