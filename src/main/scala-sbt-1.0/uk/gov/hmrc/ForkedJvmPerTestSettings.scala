/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc

import sbt.Tests.{Group, SubProcess}
import sbt._
import scala.language.postfixOps

object ForkedJvmPerTestSettings {
  def oneForkedJvmPerTest(tests: Seq[TestDefinition], forkJvmOptions: Seq[String] = Seq.empty): Seq[Group] = tests map { test =>
    Group(test.name, Seq(test), SubProcess(
        ForkOptions().withRunJVMOptions(forkJvmOptions.toVector ++ Vector("-Dtest.name=" + test.name))
    ))
  }
}