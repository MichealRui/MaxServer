package controllers

import javax.inject.Singleton
import play.api.Play
import play.api.Play.current

@Singleton
class RemoteAssets {

  val kAssetsVersion: String = "0";

  def versioned(file: String): String = {
    Play.configuration.getString("cdnurl") match {
      case Some(contentUrl) => contentUrl + file
      case None => "/assets/" + file
    }
  }

  def at(file: String): String = {
    this.versioned(file) + "?v=" + kAssetsVersion;
  }
}
