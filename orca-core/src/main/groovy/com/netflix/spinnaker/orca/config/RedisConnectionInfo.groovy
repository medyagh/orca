/*
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package com.netflix.spinnaker.orca.config

import redis.clients.jedis.Protocol
import redis.clients.util.JedisURIHelper

class RedisConnectionInfo {

  String host
  int port
  int database
  String password

  boolean hasPassword() {
    password?.length() > 0
  }

  static RedisConnectionInfo parseConnectionUri(String connection) {

    URI redisConnection = URI.create(connection)

    String host = redisConnection.host
    int port = redisConnection.port == -1 ? Protocol.DEFAULT_PORT : redisConnection.port

    int database = JedisURIHelper.getDBIndex(redisConnection)

    String password = JedisURIHelper.getPassword(redisConnection)

    new RedisConnectionInfo([
        host: host,
        port: port,
        database: database,
        password: password
    ])
  }

}
