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

package com.netflix.spinnaker.orca.kato.tasks

import com.netflix.spinnaker.orca.*
import com.netflix.spinnaker.orca.mort.MortService
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by aglover on 9/29/14.
 */
public class SecurityGroupForceCacheRefreshTask implements Task {
  static final String REFRESH_TYPE = "AmazonSecurityGroup"

  @Autowired
  MortService mort

  @Override
  TaskResult execute(TaskContext context) {
    String account = context.getInputs()."upsert.account"
    String name = context.getInputs()."upsert.name"
    String region = context.getInputs()."upsert.region"

    mort.forceCacheUpdate(REFRESH_TYPE, [account: account, securityGroupName: name, region: region])

    new DefaultTaskResult(PipelineStatus.SUCCEEDED)
  }
}