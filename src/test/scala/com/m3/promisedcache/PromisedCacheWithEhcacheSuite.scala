package com.m3.promisedcache

import org.scalatest._
import org.scalatest.matchers._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.joda.time.DateTime

import sff4s._
import org.scala_tools.time.Imports._
import net.sf.ehcache.{ CacheManager, Cache }

@RunWith(classOf[JUnitRunner])
class PromisedCacheWithEhcacheSuite extends FunSuite with ShouldMatchers {

  private val cacheName = "PromisedCacheWithEhcacheSuite_test_" + System.currentTimeMillis()
  private val ehcache = new Cache(cacheName, 5000, false, false, 60, 60)
  CacheManager.getInstance().addCache(ehcache)
  private val cache = PromisedCache(new CacheStoreOnEhcache(ehcache))

  test("caching forever") {
    val key = "cached_forever_" + System.currentTimeMillis()
    val f1: Future[String] = cache.withCache(key) {
      "expected"
    }
    Thread.sleep(3000L)
    val f2: Future[String] = cache.withCache(key) {
      "should be skipped"
    }
    f1.join(f2)() match {
      case (f1, f2) => f1 should equal(f2)
    }
  }

  test("cache expiration") {
    val key = "expiration_" + System.currentTimeMillis()
    val f1 = cache.withCache(key, new DateTime + 2.second) {
      "should be expired"
    }
    Thread.sleep(5000L)
    val f2 = cache.withCache(key, new DateTime + 2.second) {
      "expected"
    }
    f1.join(f2)() match {
      case (v1, v2) => {
        v1 should equal("should be expired")
        v2 should equal("expected")
      }
    }
  }

}
