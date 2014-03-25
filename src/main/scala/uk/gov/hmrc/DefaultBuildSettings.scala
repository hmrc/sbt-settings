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
  import net.virtualvoid.sbt.graph.Plugin.graphSettings
  
  def apply(appName: String,
                     appVersion: String,
                     organizationPackage : String = "uk.gov.hmrc",
                     targetJvm : String = "jvm-1.7")
                    (builtShellPrompt : (State) => String = ShellPrompt.buildShellPrompt(appVersion)) = {
    Defaults.defaultSettings ++
      Seq(
        organization := organizationPackage,
        name := appName,
        version := appVersion,
        scalaVersion := "2.10.3",
        scalacOptions ++= Seq(
          "-unchecked",
          "-deprecation",
          "-Xlint",
          "-language:_",
          "-target:" + targetJvm,
          "-Xmax-classfile-name", "100",
          "-encoding", "UTF-8"
        ),
        retrieveManaged := true,
        initialCommands in console := "import " + organizationPackage + "._",
        shellPrompt := builtShellPrompt,
        parallelExecution in Test := false,
        addTestReportOption(Test),
        fork in Test := false
        ) ++ gitStampSettings ++ graphSettings
  }

  def addTestReportOption(conf: Configuration, directory: String = "test-reports") = {
    val testResultDir = "target/" + directory
    testOptions in conf += Tests.Argument("-o", "-u", testResultDir, "-h", testResultDir + "/html-report")
  }
}

