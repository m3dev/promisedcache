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

import sff4s._
import org.joda.time.DateTime
import org.scala_tools.time.Imports._

/**
 * Provides caching block
 */
object WithCache {

  implicit val futureFactory = sff4s.impl.ActorsFuture

  /**
   * Caching block
   *
   * For example:
   * {{{
   * import com.m3.promisedcache.Imports._
   * val future: Future[String] = withCache("key") { "cached forever" }
   * }}}
   */
  def withCache[K, V](k: K)(v: => V)(implicit store: CacheStore): Future[V] = {
    withCache(k, new DateTime + 10.years)(v)
  }

  /**
   * Caching block
   *
   * For example:
   * {{{
   * import com.m3.promisedcache.Imports._
   * import org.scala_tools.time.Imports._
   * import org.joda.time.DateTime
   * val future: Future[String] = withCache("key", new DateTime + 1.day) { "1 day cached" }
   * }}}
   */
  def withCache[K, V](k: K, expiration: DateTime)(v: => V)(implicit store: CacheStore): Future[V] = {
    futureFactory future {
      store.get[K, V](k) getOrElse {
        store.set(k, expiration, v)
        v
      }
    }
  }

}


