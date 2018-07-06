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

import _root_.sbt.{Configuration, _}
import sbt.Keys._
import sbt.Package._
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.gitstamp.GitStamp._

object DefaultBuildSettings {

  lazy val targetJvm = settingKey[String]("The version of the JVM the build targets")

  lazy val scalaSettings: Seq[Setting[_]] = {
    targetJvm := "jvm-1.8"

    Seq(
      scalaVersion := "2.11.11",
      scalacOptions ++= Seq(
        "-Xlint",
        "-target:" + targetJvm.value,
        "-Xmax-classfile-name", "100",
        "-encoding", "UTF-8"
      ),

      javacOptions ++= Seq(
        "-Xlint",
        "-source", targetJvm.value.stripPrefix("jvm-"),
        "-target", targetJvm.value.stripPrefix("jvm-"),
        "-encoding", "UTF-8"
      )
    )
  }

  def defaultSettings(addScalaTestReports: Boolean = true): Seq[Setting[_]] = {
    val ds = Seq(
      organization := "uk.gov.hmrc",
      parallelExecution in Test := false,
      fork in Test := false,
      isSnapshot := version.value.matches("([\\w]+\\-SNAPSHOT)|([\\.\\w]+)\\-([\\d]+)\\-([\\w]+)")
    ) ++ gitStampInfo

    if (addScalaTestReports) ds ++ addTestReportOption(Test) else ds
  }

  def integrationTestSettings(): Seq[Setting[_]] = inConfig(IntegrationTest)(Defaults.itSettings) ++ Seq(
    Keys.fork in IntegrationTest := false,
    unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest)(base => Seq(base / "it")).value,
    addTestReportOption(IntegrationTest, "int-test-reports"),
    testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
    parallelExecution in IntegrationTest := false
  )

  private def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] =
    tests map { test =>
      Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
    }

  def addTestReportOption(conf: Configuration, directory: String = "test-reports") = {
    val testResultDir = "target/" + directory
    testOptions in conf += Tests.Argument("-o", "-u", testResultDir, "-h", testResultDir + "/html-report")
  }

  private def gitStampInfo() = {
    Seq(packageOptions <+= (packageOptions in Compile, packageOptions in packageBin) map {(a, b) =>
      ManifestAttributes(gitStamp.toSeq: _*)})
  }
}

