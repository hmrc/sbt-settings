package uk.gov.hmrc

import sbt._
import Keys._
import scala._

object SbtBuildInfo {

  import sbtbuildinfo.Plugin._
  import uk.gov.hmrc.GitStampPlugin.repoInfo

  def apply(packageInfo: String) = buildInfoSettings ++
    Seq(
      buildInfo <<= (sourceManaged in Compile,
        buildInfoObject, buildInfoPackage, buildInfoKeys, thisProjectRef, state, streams) map {
        (dir, obj, pkg, keys, ref, state, taskStreams) =>
          Seq(BuildInfo(dir / "sbt-buildinfo", obj, pkg, keys, ref, state, taskStreams.cacheDirectory))
      },
      sourceGenerators in Compile <+= buildInfo,
      buildInfoPackage := packageInfo,
      buildInfoKeys := Seq[BuildInfoKey](
        name,
        version,
        scalaVersion,
        sbtVersion,
        libraryDependencies,
        BuildInfoKey.action("builtAt") {
          val dtf = new java.text.SimpleDateFormat("yyyy-MM-dd")
          dtf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"))
          dtf.format(new java.util.Date())
        }) ++ gitInfo
    )

  val gitInfo = repoInfo.map {
    t =>
      BuildInfoKey.action(t._1.replaceAll("-", "")) {
        t._2
      }
  }.toSeq
}
