/*
 * Copyright 2012 M3, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.m3.promisedcache

import org.joda.time.DateTime
import org.scala_tools.time.Imports._
import redis.clients.jedis._
import java.util.ArrayList
import java.lang.String
import util.control.Exception._

/**
 * Cache store using Redis
 *
 * @see http://redis.io/
 */
case class CacheStoreOnRedis(addresses: Seq[String]) extends CacheStore {

  private val shards = new ArrayList[JedisShardInfo]
  addresses foreach {
    address =>
      {
        val host :: port :: Nil = address.split(":").toList
        shards.add(new JedisShardInfo(host, port.toInt))
      }
  }
  private val redisPool = new ShardedJedisPool(new JedisPoolConfig(), shards)

  override protected def justGet[K, String](k: K): Option[String] = {
    val v = redisPool.getResource.get(k.toString).asInstanceOf[String]
    onGetSuccess(k, Option(v))
    Option(v)
  }

  override protected def justSet[K, String](k: K, expiration: DateTime, v: => String): Unit = {
    if (expiration.isAfter(new DateTime + 1.year)) {
      redisPool.getResource.set(k.toString, v.asInstanceOf[java.lang.String])
    } else {
      val secondsToExpire: Int = ((expiration.millis - new DateTime().millis) / 1000).toInt
      redisPool.getResource.setex(k.toString, secondsToExpire, v.asInstanceOf[java.lang.String])
    }
  }

}
