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
import SbtGitInfo.gitInfo
import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoKey
import sbtbuildinfo.BuildInfoKeys._
import sbtbuildinfo.BuildInfoPlugin.autoImport.{BuildInfoOption, buildInfoOptions}

object SbtBuildInfo {

  // Add additional keys to the generated BuildInfo output, including git info pulled from sbt-git-stamp plugin
  def apply(): Seq[Def.Setting[_]] = Seq(
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      libraryDependencies,
      BuildInfoKey.action("builtAt") {now}) ++ gitInfo.map {toBuildInfo},
    buildInfoOptions += BuildInfoOption.ToMap
  )

  def now: String = {
    val dtf = new java.text.SimpleDateFormat("yyyy-MM-dd")
    dtf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"))
    dtf.format(new java.util.Date())
  }

  //Map extracted values from sbt-git-stamp plugin to build info keys, dropping hypens in the name
  private def toBuildInfo: ((String, String)) => BuildInfoKey.Entry[String] = {
    case (gitKey, value) => BuildInfoKey.action(gitKey.replaceAll("-", "")) {  value}
  }

}
