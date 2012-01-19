package example

import com.m3.promisedcache.{CacheStoreOnMemcached, CacheStore}

import com.m3.promisedcache.Imports._
import org.scala_tools.time.Imports._

object Examples {

  implicit val cacheStore: CacheStore = new CacheStoreOnMemcached(Seq("memcached:11211"))

  def main(args: Array[String]) = {

    val key = "kkkk" + new DateTime
    val res1 = withCache(key, new DateTime + 10.minutes) {
      Thread.sleep(1000L)
      "cached"
    }
    Thread.sleep(2000L)
    val res2 = withCache(key, new DateTime + 10.minutes) {
      Thread.sleep(4000L)
      "not cached"
    }
    res1.join(res2)() match {
      case (v1, v2) => println("v1:" + v1 + ",v2:" + v2)
    }

  }

}