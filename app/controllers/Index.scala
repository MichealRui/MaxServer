package controllers

import javax.inject._
import play.api.mvc.{Action, AnyContent, Controller}

/**
  * Created by ruibing on 16/8/31.
  */
@Singleton
class Index @Inject() extends Controller{
  def index: Action[AnyContent] = Action { request =>
    Ok("abc")
  }
  def health: Action[AnyContent] = Action { request =>
    Ok("")
  }
}
