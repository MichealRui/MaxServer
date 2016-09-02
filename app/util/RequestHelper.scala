package util

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.ws._
import play.api.mvc._
import play.utils.UriEncoding

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object RequestHelper {

  def getParam(request: play.api.mvc.Request[AnyContent], key: String, default: String): String = {
    if (request.queryString.contains(key)) {
      var querys = request.queryString(key)
      if (querys.nonEmpty) {
        querys.head
      } else {
        default
      }
    } else {
      default
    }
  }

  // return request response
  def requestGet(url: String): String = {
    val future: Future[String] = WS.url(url).get().map { response =>
      response.body
    }
    Await.result(future, 10 seconds)
  }

  def requestPost(url: String, content: String): String = {
    val future: Future[String] = WS.url(url).post(content).map { response =>
      response.body
    }
    Await.result(future, 10 seconds)
  }

  def requestPostForm(url: String, key: String, content: String): String = {
    val future: Future[String] =
      WS.url(url)
        .withHeaders("Content-Type" -> "application/x-www-form-urlencoded")
        .post(key + "=" + UriEncoding.encodePathSegment(content, "UTF-8"))
        .map { response =>
          response.body
        }
    Await.result(future, 10 seconds)
  }

  def requestPostJson(url: String, content: String) = {
    val future: Future[String] = WS.url(url).withHeaders("Content-Type" -> "application/json")
      .post(content).map { response =>
      response.body
    }
    Await.result(future, 10 seconds)
  }
}
