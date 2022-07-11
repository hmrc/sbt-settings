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
import de.heikoseeberger.sbtheader.AutomateHeaderPlugin.autoImport.automateHeaderSettings
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.headerSettings
import sbt.Keys._
import uk.gov.hmrc.gitstamp.GitStampPlugin

object DefaultBuildSettings {

  lazy val targetJvm = settingKey[String]("The version of the JVM the build targets")

  lazy val scalaSettings: Seq[Setting[_]] = {
    targetJvm := "jvm-1.8"

    def toLong(v: String): Long =
      v.split("\\.") match {
        case Array(maj, min, pat) => maj.toInt * 1000 + min.toInt * 1000 + pat.toInt
        case _                    => 0
      }

    val javaMajorVersion: Int =
      System.getProperty("java.version").split("\\.").toList match {
        case "1" :: major :: _ => major.toInt
        case major :: _        => major.toInt
        case _                 => throw new Exception("Unable to determine java version")
      }

    Seq(
      scalaVersion := "2.11.12",
      scalacOptions ++= Seq(
        "-unchecked",
        "-deprecation",
        "-Xlint",
        "-target:" + targetJvm.value,
        "-encoding", "UTF-8"
        ) ++
          (if (toLong(scalaVersion.value) < toLong("2.12.4"))
             Seq.empty
           else Seq("-Ywarn-macros:after") // this was default behaviour uptill 2.12.4. https://github.com/scala/bug/issues/10571
          ) ++
          (if (toLong(scalaVersion.value) < toLong("2.13.0"))
             Seq("-Xmax-classfile-name", "100") // https://github.com/scala/scala/pull/7497
           else Seq.empty
          ),

      javacOptions ++= (Seq(
        "-Xlint",
        "-encoding", "UTF-8"
      ) ++ (if (javaMajorVersion >= 9) {
        Seq(
          "--release", targetJvm.value.stripPrefix("jvm-").stripPrefix("1.")
        )
      } else {
        Seq(
          "-source", targetJvm.value.stripPrefix("jvm-"),
          "-target", targetJvm.value.stripPrefix("jvm-")
        )
      }))
    )
  }

  def defaultSettings(addScalaTestReports: Boolean = true): Seq[Setting[_]] =
    Seq(
      organization := "uk.gov.hmrc",
      parallelExecution in Test := false,
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
