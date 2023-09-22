sbt-settings
=========

SBT plugin providing standard functions and configurations

### Sbt 1.x

Since major version 4, this plugin is cross compiled for sbt 1.x (specifically 1.3.4).

| Sbt version | Plugin version |
| ----------- | -------------- |
| `0.13.x`    | `any`          |
| `>= 1.x`    | `>= 4.x`       |


## Integration Tests

`IntegrationTest` scope has been deprecated since [Sbt 1.9.0](https://github.com/sbt/sbt/releases/tag/v1.9.0). Although this won't be removed until
sbt 2.x, since version `4.15.0` of `sbt-settings`, it is recommended to create an `it` subproject with `DefaultBuildSettings.itSettings`.

e.g.

Move sources from `it/` to `it/test` folder.

Then

```scala
lazy val microservice = ...
  .configs(IntegrationTest)
  .settings(DefaultBuildSettings.integrationTestSettings())
```

becomes

```scala
// move shared settings from `microservice` here
ThisBuild / majorVersion := ...
ThisBuild / scalaVersion := ...

lazy val microservice = ...

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(DefaultBuildSettings.itSettings)
  .settings(libraryDependencies ++= AppDependencies.itDependencies)
```
Do not aggregate over `it` - run explicitly as required.

Run with `sbt it/test` instead of `sbt it:test`. Review any scripts or build configs that may need updating.

The `itDependencies` will also change from
```scala
  val itDependencies = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-28" % bootstrapVersion % "test, it"
  )
```
to
```scala
  val itDependencies = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-28" % bootstrapVersion % Test
  )
```
However, using `test->test` means that `microservice` test dependencies are inherited, so `itDependencies` only needs to contain additional dependencies.



## License ##

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
