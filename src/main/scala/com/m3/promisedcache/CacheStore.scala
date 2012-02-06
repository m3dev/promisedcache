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
import com.weiglewilczek.slf4s.Logging
import util.control.Exception._

/**
 * Cache store
 */
trait CacheStore extends Logging {

  /**
   * Get value
   */
  def get[K, V](k: K): Option[V] = {
    allCatch withApply {
      t =>
        {
          onGetFailure(t, k)
          Option.empty
        }
    } apply {
      val v = justGet(k)
      onGetSuccess(k, v)
      v
    }
  }

  /**
   * Just get value
   */
  protected def justGet[K, V](k: K): Option[V]

  /**
   * Set value
   */
  def set[K, V](k: K, expiration: DateTime, v: => V): Unit = {
    allCatch withApply (t => onSetFailure(t, k, v)) apply {
      justSet(k, expiration, v)
      onSetSuccess(k, v)
    }
  }

  /**
   * Just set value
   */
  protected def justSet[K, V](k: K, expiration: DateTime, v: => V): Unit

  /**
   * Callback after get method success
   */
  def onGetSuccess[K, V](k: K, v: Option[V]): Unit = ()

  /**
   * Callback after get method failure
   */
  def onGetFailure[K](t: Throwable, k: K): Unit = {
    logger.info("Cache store failed to get " + k + " because " + t.getMessage)
    logger.debug("Cache store throwed the following exception.", t);
  }

  /**
   * Callback after set method success
   */
  def onSetSuccess[K, V](k: K, v: V): Unit = ()

  /**
   * Callback after set method failure
   */
  def onSetFailure[K, V](t: Throwable, k: K, v: V): Unit = {
    logger.info("Cache store failed to set (" + k + " -> " + v + ") because " + t.getMessage)
    logger.debug("Cache store throwed the following exception.", t);
  }

}