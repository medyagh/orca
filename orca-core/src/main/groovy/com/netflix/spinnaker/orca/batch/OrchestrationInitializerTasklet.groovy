/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.orca.batch

import com.netflix.spinnaker.orca.pipeline.Stage
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import rx.subjects.ReplaySubject


import static org.springframework.batch.repeat.RepeatStatus.FINISHED

@CompileStatic
@TupleConstructor(includeFields = true)
class OrchestrationInitializerTasklet implements Tasklet {

  static Step createTasklet(StepBuilderFactory steps, List<Stage> stages, ReplaySubject subject) {
    steps.get("orca-init-step")
      .tasklet(new OrchestrationInitializerTasklet(stages, subject))
      .build()
  }

  private final List<Stage> stages
  private final ReplaySubject subject

  @Override
  RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    chunkContext.stepContext.stepExecution.jobExecution.with {
      for (stage in stages) {
        executionContext.put(stage.type, stage)
      }
      subject.onNext(it.id)
      subject.onCompleted()
    }
    return FINISHED
  }
}