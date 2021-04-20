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

import _root_.sbt.{Configuration, Def, _}
import sbt.Keys._
import sbt.Tests.Group
import uk.gov.hmrc.gitstamp.GitStampPlugin
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.headerSettings
import de.heikoseeberger.sbtheader.AutomateHeaderPlugin.autoImport.automateHeaderSettings

object DefaultBuildSettings {

  lazy val targetJvm = settingKey[String]("The version of the JVM the build targets")

  lazy val scalaSettings: Seq[Setting[_]] = {
    targetJvm := "jvm-1.8"

    Seq(
      scalaVersion := "2.11.12",
      scalacOptions ++= Seq(
        "-unchecked",
        "-deprecation",
        "-Xlint",
        "-target:" + targetJvm.value,
        "-Xmax-classfile-name", "100",
        "-encoding", "UTF-8"
        ) ++
          {def toLong(v: String): Long =
             v.split("\\.") match {
               case Array(maj, min, pat) => maj.toInt * 1000 + min.toInt * 1000 + pat.toInt
               case _                    => 0
             }
           if (toLong(scalaVersion.value) < toLong("2.12.4"))
             Seq.empty
           else Seq("-Ywarn-macros:after") // this was default behaviour uptill 2.12.4. https://github.com/scala/bug/issues/10571
          },

      javacOptions ++= Seq(
        "-Xlint",
        "-source", targetJvm.value.stripPrefix("jvm-"),
        "-target", targetJvm.value.stripPrefix("jvm-"),
        "-encoding", "UTF-8"
      )
    )
  }

  def defaultSettings(addScalaTestReports: Boolean = true): Seq[Setting[_]] =
    Seq(
      organization := "uk.gov.hmrc",
      parallelExecution in Test := false,
      fork in Test := false,
      isSnapshot := version.value.matches("([\\w\\.]+\\-SNAPSHOT)|([\\.\\w]+)\\-([\\d]+)\\-([\\w]+)")
    ) ++
    GitStampPlugin.gitStampSettings ++
    (if (addScalaTestReports) addTestReportOption(Test) else Seq.empty)

  def integrationTestSettings(enableLicenseHeaders: Boolean = true): Seq[Setting[_]] =
    inConfig(IntegrationTest)(Defaults.itSettings) ++
    Seq(
      Keys.fork in IntegrationTest := false,
      unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest)(base => Seq(base / "it")).value,
      addTestReportOption(IntegrationTest, "int-test-reports"),
      testGrouping in IntegrationTest := ForkedJvmPerTestSettings.oneForkedJvmPerTest(
        (definedTests in IntegrationTest).value,
        (javaOptions in IntegrationTest).value
      ),
      parallelExecution in IntegrationTest := false
    ) ++
    (if (enableLicenseHeaders) {
       headerSettings(IntegrationTest) ++
       automateHeaderSettings(IntegrationTest)
     } else Seq.empty
    )

  def addTestReportOption(conf: Configuration, directory: String = "test-reports") = {
    val testResultDir = "target/" + directory
    testOptions in conf += Tests.Argument("-o", "-u", testResultDir, "-h", testResultDir + "/html-report")
  }
}
