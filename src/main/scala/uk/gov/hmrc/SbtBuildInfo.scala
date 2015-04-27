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

import sbt.Keys._
import sbt._
import sbtbuildinfo.Plugin
import SbtGitInfo.gitInfo

object SbtBuildInfo {

  import sbtbuildinfo.Plugin._

  def apply() = buildInfoSettings ++
    Seq(
      buildInfo <<= (sourceManaged in Compile,
        buildInfoObject, buildInfoPackage, buildInfoKeys, thisProjectRef, state, streams) map {
        (dir, obj, pkg, keys, ref, state, taskStreams) =>
          Seq(BuildInfo(dir / "sbt-buildinfo", obj, pkg, keys, ref, state, taskStreams.cacheDirectory))
      },
      sourceGenerators in Compile <+= buildInfo,
      buildInfoPackage := organization.value,
      buildInfoKeys := Seq[BuildInfoKey](
        name,
        version,
        scalaVersion,
        sbtVersion,
        libraryDependencies,
        BuildInfoKey.action("builtAt") {now}) ++ gitInfo.map {toBuildInfo}
    )

  private def now: String = {
    val dtf = new java.text.SimpleDateFormat("yyyy-MM-dd")
    dtf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"))
    dtf.format(new java.util.Date())
  }

  private def toBuildInfo: ((String, String)) => Plugin.BuildInfoKey.Entry[String] = {
    t =>
      BuildInfoKey.action(t._1.replaceAll("-", "")) {
        t._2
      }
  }
}
