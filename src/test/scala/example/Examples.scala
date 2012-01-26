package example

import com.m3.promisedcache._
import com.m3.promisedcache.Imports._
import org.scala_tools.time.Imports._
import sff4s.Future

object Examples {

  implicit val cacheStore: CacheStore = new CacheStoreOnMemcached(Seq("memcached:11211"))

  def main(args: Array[String]) = {

    // ----------------------------
    // caching

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

    // ----------------------------
    // collect cache values

    val foo: Future[String] = withCache("foo", new DateTime + 10.minutes) {
      "Scala is a general purpose programming language"
    }
    val bar: Future[String] = withCache("bar", new DateTime + 10.minutes) {
      " designed to express common programming patterns"
    }
    val baz: Future[String] = withCache("baz", new DateTime + 10.minutes) {
      " in a concise, elegant, and type-safe way."
    }

    val msg: Future[String] = for {
      f <- foo
      b <- bar
      bz <- baz
    } yield f + b + bz
    println(msg())

  }

}