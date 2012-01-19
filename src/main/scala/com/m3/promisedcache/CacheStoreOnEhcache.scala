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
import net.sf.ehcache.Element
import util.control.Exception._

/**
 * Cache store using Ehcache
 *
 * @see http://ehcache.org/
 */
case class CacheStoreOnEhcache(ehcache: net.sf.ehcache.Cache) extends CacheStore {

  override protected def justGet[K, V](k: K): Option[V] = {
    Option(ehcache.get(k) match {
      case null => null.asInstanceOf[V]
      case element: Element => element.getValue.asInstanceOf[V]
    })
  }

  override protected def justSet[K, V](k: K, expiration: DateTime, v: => V): Unit = {
    if (expiration.isAfter(new DateTime + 1.year)) {
      val element = new Element(k, v)
      element.setEternal(true)
      ehcache.put(element)
    } else {
      val element = new Element(k, v)
      val secondsToExpire: Int = ((expiration.millis - new DateTime().millis) / 1000).toInt
      element.setTimeToLive(secondsToExpire)
      ehcache.put(element)
    }
  }

}
