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
import com.netflix.spinnaker.orca.oort.OortService
import org.springframework.beans.factory.annotation.Autowired

class DeleteAmazonLoadBalancerForceRefreshTask implements Task {
  static final String REFRESH_TYPE = "AmazonLoadBalancer"

  @Autowired
  OortService oort

  @Override
  TaskResult execute(TaskContext context) {
    String account = context.getInputs()."deleteAmazonLoadBalancer.credentials"
    String name = context.getInputs()."deleteAmazonLoadBalancer.loadBalancerName"
    List<String> regions = context.getInputs()."deleteAmazonLoadBalancer.regions"

    regions.each { region ->
      def model = [loadBalancerName: name, region: region, account: account, evict: true]
      oort.forceCacheUpdate(REFRESH_TYPE, model)
    }
    new DefaultTaskResult(PipelineStatus.SUCCEEDED)
  }
}