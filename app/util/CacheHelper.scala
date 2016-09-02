package util

import org.apache.commons.lang3.reflect.TypeUtils
import play.api.Application
import play.api.cache.Cache

import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

object CacheHelper{

  /**
   *  CacheHelper Wrapper play.lib.cache
   */
  private val cache = Cache


  /**
   * Set a value into the cache.
   *
   * @param key Item key.
   * @param value Item value.
   * @param expiration Expiration time in seconds (0 second means eternity).
   */
  def set(key: String, value: Any, expiration: Int = 0)(implicit app: Application): Unit = {
    cache.set(key, value, expiration)
  }

  /**
   * Set a value into the cache.
   *
   * @param key Item key.
   * @param value Item value.
   * @param expiration Expiration time as a [[scala.concurrent.duration.Duration]].
   */
  def set(key: String, value: Any, expiration: Duration)(implicit app: Application): Unit = {
    set(key, value, expiration.toSeconds.toInt)
  }

  /**
   * Retrieve a value from the cache.
   *
   * @param key Item key.
   */
  def get(key: String)(implicit app: Application): Option[Any] = {
    cache.get(key)
  }

  /**
   * Retrieve a value from the cache, or set it from a default function.
   *
   * @param key Item key.
   * @param expiration expiration period in seconds.
   * @param orElse The default function to invoke if the value was not found in cache.
   */
  def getOrElse[A](key: String, expiration: Int = 0)(orElse: => A)(implicit app: Application, ct: ClassTag[A]): A = {
    getAs[A](key).getOrElse {
      val value = orElse
      set(key, value, expiration)
      value
    }
  }

  /**
   * Retrieve a value from the cache for the given type
   *
   * @param key Item key.
   * @return result as Option[T]
   */
  def getAs[T](key: String)(implicit app: Application, ct: ClassTag[T]): Option[T] = {
    get(key)(app).map { item =>
      if (TypeUtils.isInstance(item, ct.runtimeClass)) Some(item.asInstanceOf[T]) else None
    }.getOrElse(None)
  }

  def remove(key: String)(implicit app: Application) {
    cache.remove(key)
  }
}