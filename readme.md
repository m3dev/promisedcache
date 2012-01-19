# PromisedCache

`PromisedCache` is a caching library with sff4s.Future.


# Settings

Select your favorite cache.

## Memcached

```scala
resolvers ++= Seq(
  "m3dev.github.com"   at "http://m3dev.github.com/mvn-repo/releases",
  "couchbase.com"      at "http://files.couchbase.com/maven2/"
)

libraryDependencies ++= Seq(
  "com.m3.promisedcache"    %% "promisedcache"   % "0.1",
  "spy"                     %  "spymemcached"    % "2.7.1"
)
```

## Redis

```scala
resolvers ++= Seq(
  "m3dev.github.com"   at "http://m3dev.github.com/mvn-repo/releases"
)

libraryDependencies ++= Seq(
  "com.m3.promisedcache"    %% "promisedcache"   % "0.1",
  "redis.clients"           %  "jedis"           % "2.0.0"
)
```

## Ehcache

```scala
resolvers ++= Seq(
  "m3dev.github.com"   at "http://m3dev.github.com/mvn-repo/releases"
)

libraryDependencies ++= Seq(
  "com.m3.promisedcache"    %% "promisedcache"   % "0.1",
  "net.sf.ehcache"          %  "ehcache"         % "1.5.0"
)
```


# Usage

## Memcached

```scala
import com.m3.promisedcache._
implicit val cacheStore: CacheStore = new CacheStoreOnMemcached(Seq("localhost:11211", "localhost:11212"))

import com.m3.promisedcache.Imports._
val f1: sff4s.Future[String] = withCache("key") { "cached forever" }
val f2: sff4s.Future[String] = withCache(12345, DateTime.now + 30.minutes) { "30 minutes cached" }
```

## Redis

```scala
import com.m3.promisedcache._
implicit val cacheStore: CacheStore = new CacheStoreOnRedis(Seq("localhost:6379", "localhost:6378"))

import com.m3.promisedcache.Imports._
Val f: sff4s.Future[String] = withCache(12345, DateTime.now + 30.minutes) { "30 minutes cached" }
```

## Ehcache

```scala
import com.m3.promisedcache._
import net.sf.ehcache.Cache
val ehcache: Cache = new Cache("cacheName", 5000, false, false, 60, 60)
implicit val cacheStore: CacheStore = new CacheStoreOnEhcache("name")

import com.m3.promisedcache.Imports._
Val f: sff4s.Future[String] = withCache(12345, DateTime.now + 30.minutes) { "30 minutes cached" }
```

There is a choise between setting up with implicit parameters or initializing `Cache` instance.

```scala
import com.m3.promisedcache._
val cache: PromisedCache = PromisedCache(new CacheStoreOnMemcached(Seq("localhost:11211")))
Val f: sff4s.Future[String] = cache.withCache(12345, DateTime.now + 30.minutes) { "30 minutes cached" }
```


# See also

## sff4s

 https://github.com/eed3si9n/sff4s

## scala-time

 https://github.com/jorgeortiz85/scala-time


# License

 Apache License, Version 2.0

 http://www.apache.org/licenses/LICENSE-2.0.html


# Contributers

* [Kazuhiro Sera](https://github.com/seratch)
* [Toshiyuki Takahashi](https://github.com/tototoshi)
* [Akira Ueda](https://github.com/akr4)
* Takayuki Murata