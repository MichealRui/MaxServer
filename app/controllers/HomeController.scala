package controllers

import javax.inject._

import play.api._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    //Ok(views.html.index new ap("Yourplication is ready."))
    Ok(views.html.index.render())
  }

  def testItemAction: Action[JsValue] = Action(parse.tolerantJson) { request =>
    println("in action")
    println(request.body)
    Ok(Json.stringify(
      Json.obj(
        "status" -> "0",
        "item" -> Map(
          "productImg" -> "./components/ProductItem/ProductInfo/images/productImg.jpg",
          "productName" -> "Jingle Bells",
          "productDesc" -> "超级好吃的饼干",
          "productTaste" ->"经典盐焗味",
          "productCost" -> "12.5",
          "skuId" -> "0001",
          "count"-> "1"
        )
      )
    ))
  }

}
