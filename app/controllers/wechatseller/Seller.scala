package controllers.wechatseller

import javax.inject.{Inject, Singleton}

import play.api.Logger
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller}
import play.twirl.api.Html
import util.RequestHelper._
import wechatutil._

/**
  * Created by ruibing on 16/8/31.
  */
@Singleton
class Seller @Inject() extends Controller {

  private def isWeChat(ua: String): Boolean = {
    val weChatUa = "micromessenger"
    ua.toLowerCase.contains(weChatUa)
  }

  def checkIn: Action[AnyContent] = Action { request =>
    println(request.headers.get("User-Agent").exists(isWeChat))
    println(request.queryString.get("code").map(c => c.mkString))
    val isWechat = request.headers.get("User-Agent").exists(isWeChat)
    if(isWechat) {
      request.queryString.get("code")
        .map(c => fetchOpenId(c.mkString))
    }
    Ok(views.html.wechatseller.checkin(RemoteAssets = new controllers.RemoteAssets()))
  }

  def checkOut: Action[AnyContent] = Action { request =>
    Logger("/checkout" + request.headers.get("User-Agent"))
    val orderNumber = request.queryString.get("ordernumber").map(_.mkString)
    orderNumber.map(fetchOrderPayment)
      .map(
        order => Ok(views.html.wechatseller.checkout.render(
          RemoteAssets = new controllers.RemoteAssets(),
          order = Html.apply(order))
        )
      )
      .getOrElse(
        Ok(<h1>订单号出错</h1>).as(HTML)
      )
  }

  def sellerOrderList: Action[AnyContent] = Action { request =>
    val code = request.queryString.get("code").map(_.mkString)
    code.map(fetOrderList)
      .map(
        orderList => Ok(views.html.wechatseller.orderlist.render(
          RemoteAssets = new controllers.RemoteAssets(),
          orderList = Html.apply(orderList))
        )
      )
      .getOrElse(
        Ok(<h1>111</h1>).as(HTML)
      )
  }

  def sellerOrderDetail: Action[AnyContent] = Action { request =>
    val orderNumber = request.queryString.get("ordernumber").map(_.mkString)
    orderNumber.map(fetchOrderDetail)
      .map(
        order => Ok(views.html.wechatseller.orderdetail.render(
          RemoteAssets = new controllers.RemoteAssets(),
          orderDetail = Html.apply(order))
        )
      )
      .getOrElse(
        Ok(<h1>订单号出错</h1>).as(HTML)
      )
  }

  def fetchOpenId(code: String) = {
    WxUtil.getOpenId(code)
  }

  def fetchOrderPayment(order: String) = {
    requestPost("http://localhost:9000/getorderpayment", order)
  }

  def fetOrderList(code: String) = {
    requestPost("http://localhost:9000/getorderlist", code)
  }

  def fetchOrderDetail(order: String) = {
    requestPost("http://localhost:9000/getorderdetail", order)
  }

  def testItemAction: Action[JsValue] = Action(parse.tolerantJson) { request =>
    println("in item action")
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

  def testOrderAction: Action[JsValue] = Action(parse.tolerantJson) { request =>
    println("in order action")
    println(request.body)
    Ok(Json.stringify(
      Json.obj(
        "is_succ" -> true,
        "order_number" -> "201608317456238"
      )
    ))
  }

  def testOrderPayment: Action[JsValue] = Action(parse.tolerantJson) { request =>
    println("in pay action")
    println(request.body)
    Ok(Json.stringify(
      Json.obj(
        "order" -> Map(
          "shopName" -> "北京望京SOHO店",
          "orderNumber" -> "0000001",
          "totalCount" -> "152.00",
          "paymentUrl" -> "https://www.taobao.com"
        )
      )
    ))
  }

  def testOrderList: Action[JsValue] = Action(parse.tolerantJson) { request =>
    println("in order list action")
    Ok(Json.stringify(
        Json.parse(
          """
            {
                    "orderList" : [
                      {
                        "orderImg" : "./mycomponent/orderImg.png",
                        "orderNumber" : "43424234324",
                        "orderStatus" : "已完成",
                        "orderStatusClass" : "orderComplete",
                        "orderCost" : "152.00",
                        "orderDate" : "2016-06-12 15:30",
                        "productItemList" : [ "./mycomponent/productItemImg.png", "./mycomponent/productItemImg.png"]
                      },
                      {
                        "orderImg" : "./mycomponent/orderImg.png",
                        "orderNumber" : "asdasd",
                        "orderStatus" : "已完成",
                        "orderStatusClass" : "orderComplete",
                        "orderCost" : "152.00",
                        "orderDate" : "2016-06-12 15:30",
                        "productItemList" : [ "./mycomponent/productItemImg.png", "./mycomponent/productItemImg.png"]
                      },
                      {
                        "orderImg" : "./mycomponent/orderImg.png",
                        "orderNumber" : "1231243453",
                        "orderStatus" : "已完成",
                        "orderStatusClass" : "orderComplete",
                        "orderCost" : "152.00",
                        "orderDate" : "2016-06-12 15:30",
                        "productItemList" : [ "./mycomponent/productItemImg.png", "./mycomponent/productItemImg.png", "./mycomponent/productItemImg.png"]
                      }
                    ]
            }
          """
        )
    ))
  }

  def testOrderDetail: Action[JsValue] = Action(parse.tolerantJson) { request =>
    println("in action order detail")
    Ok(Json.stringify(
      Json.parse(
        """
          {
                  "orderDetail":{
                    "orderAddress":"北京地铁一号线国贸站机器A",
                    "orderStatus":"已完成",
                    "orderStatusClass":"orderComplete",
                    "orderNumber":"190019000",
                    "orderDate":"2016-06-12 15:30",
                    "orderLastDate":"2016-06-12 15:30",
                    "orderDetailProductList":[
                      {
                        "productImg":"./mycomponent/productItemImg.png",
                        "productName":"Gokuri",
                        "productDesc":"桃味果汁饮料500ml",
                        "productTaste":"番茄口味",
                        "unitPrice":"20",
                        "quantity":"1",
                        "amount":"20"
                      },
                      {
                        "productImg":"./mycomponent/productItemImg.png",
                        "productName":"Gokuri",
                        "productDesc":"桃味果汁饮料500ml",
                        "productTaste":"番茄口味",
                        "unitPrice":"20",
                        "quantity":"1",
                        "amount":"20"
                      },
                      {
                        "productImg":"./mycomponent/productItemImg.png",
                        "productName":"Gokuri",
                        "productDesc":"桃味果汁饮料500ml",
                        "productTaste":"番茄口味",
                        "unitPrice":"20",
                        "quantity":"1",
                        "amount":"20"
                      }
                    ],
                    "totalMoney":"172"
                  }
                }
        """
      )
    ))
  }

}
