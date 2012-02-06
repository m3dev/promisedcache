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
import net.spy.memcached.MemcachedClient
import java.net.InetSocketAddress

import scala.collection.JavaConverters._
import util.control.Exception._

/**
 * Cache store using Memcached
 *
 * @see http://memcached.org/
 */
case class CacheStoreOnMemcached(addresses: Seq[String]) extends CacheStore {

  private val inetAddresses: Seq[InetSocketAddress] = addresses map {
    address =>
      {
        val host :: port :: Nil = address.split(":").toList
        new InetSocketAddress(host, port.toInt)
      }
  }
  private val memcached = new MemcachedClient(inetAddresses.asJava)

  override protected def justGet[K, V](k: K): Option[V] = Option(memcached.get(k.toString).asInstanceOf[V])

  override protected def justSet[K, V](k: K, expiration: DateTime, v: => V): Unit = {
    if (expiration.isAfter(new DateTime + 1.year)) {
      memcached.set(k.toString, 0, v)
    } else {
      val secondsToExpire: Int = ((expiration.millis - new DateTime().millis) / 1000).toInt
      memcached.set(k.toString, secondsToExpire, v)
    }
  }

}
