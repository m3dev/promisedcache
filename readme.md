# PromisedCache

`PromisedCache` is a caching library with sff4s.Future.


# Settings

Select your favorite cache.

## Memcached

```scala
resolvers += "couchbase.com" at "http://files.couchbase.com/maven2/"

libraryDependencies ++= Seq(
  "com.m3" %% "promisedcache"   % "0.2.0",
  "spy"    %  "spymemcached"    % "2.7.1"
)
```

## Redis

```scala
libraryDependencies ++= Seq(
  "com.m3"        %% "promisedcache"   % "0.2.0",
  "redis.clients" %  "jedis"           % "2.0.0"
)
```

## Ehcache

```scala
libraryDependencies ++= Seq(
  "com.m3"         %% "promisedcache"   % "0.2.0",
  "net.sf.ehcache" %  "ehcache"         % "1.5.0"
)
```

# ls.implicit.ly

`PromisedCache` itself is available @ ls.implicit.ly

http://ls.implicit.ly/m3dev/promisedcache


# Usage

## Memcached

```scala
import com.m3.promisedcache._
implicit val cacheStore: CacheStore = new CacheStoreOnMemcached(Seq("localhost:11211"))

import com.m3.promisedcache.Imports._
import org.scala_tools.time.Imports._

val key: String = "key"
val f1: sff4s.Future[String] = withCache(key) { "cached forever" }

val key: Int = 12345
val expiration: DateTime = DateTime.now + 30.minutes
val f2: sff4s.Future[HeavyOpsResult] = withCache(key, expiration) { 
  heavyOps.result
}
```

## Redis

```scala
import com.m3.promisedcache._
implicit val cacheStore: CacheStore = new CacheStoreOnRedis(Seq("localhost:6379"))

import com.m3.promisedcache.Imports._
import org.scala_tools.time.Imports._

val f: sff4s.Future[HeavyOpsResult] = withCache(12345, DateTime.now + 30.minutes) { 
  heavyOps.result
}
```

## Ehcache

```scala
import com.m3.promisedcache._
import net.sf.ehcache.Cache
val ehcache: Cache = new Cache("cacheName", 5000, false, false, 60, 60)
implicit val cacheStore: CacheStore = new CacheStoreOnEhcache("name")

import com.m3.promisedcache.Imports._
import org.scala_tools.time.Imports._

val f: sff4s.Future[HeavyOpsResult] = withCache(12345, DateTime.now + 30.minutes) { 
  heavyOps.result
}
```

## Collect Futures

```scala
val foo: Future[String] = withCache("foo", DateTime.now + 1.minute) {
  "Scala is a general purpose programming language"
}
val bar: Future[String] = withCache("bar", DateTime.now + 1.minute) {
  " designed to express common programming patterns"
}
val baz: Future[String] = withCache("baz", DateTime.now + 1.minute) {
  " in a concise, elegant, and type-safe way."
}

val msg: Future[String] = for {
  f <- foo
  b <- bar
  bz <- baz
} yield f + b + bz

println(msg())
```

## Initialize Cache instance

There is a choise between setting up with implicit parameters or initializing `Cache` instance.

```scala
import com.m3.promisedcache._
import org.scala_tools.time.Imports._

val cache: PromisedCache = PromisedCache(new CacheStoreOnMemcached(Seq("localhost:11211")))

val f: sff4s.Future[HeavyOpsResult] = cache.withCache(12345, DateTime.now + 30.minutes) { 
  heavyOps.result
}
```


# See also

## sff4s

 https://github.com/eed3si9n/sff4s

## scala-time

 https://github.com/jorgeortiz85/scala-time


# License

 Apache License, Version 2.0

 http://www.apache.org/licenses/LICENSE-2.0.html


