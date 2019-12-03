/*
 * Copyright 2012-2013 Stephane Godbillon (@sgodbillon)
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

import sbt.{Project, State}

import scala.sys.process.ProcessLogger

//Credit goes to the Reactivemongo gents for this utility
object ShellPrompt {

  object devnull extends ProcessLogger {
    def out(s: => String) {}

    def err(s: => String) {}

    def buffer[T](f: => T): T = f
  }

  import scala.sys.process._
  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
    )

  private def buildShellPrompt(buildVersion : String) = {
    (state: State) => {
      val currProject = Project.extract(state).currentProject.id
      "%s:%s:%s> ".format(
        currProject, currBranch, buildVersion
      )
    }
  }

  def apply(buildVersion : String) = buildShellPrompt(buildVersion)
}

