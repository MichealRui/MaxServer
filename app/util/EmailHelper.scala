package util

import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws._

import scala.collection.immutable.ListMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object EmailHelper {

  private val api_user = "shop_postuser"

  private val api_key = "PVWZE7TDlZ0qgoeR"

  private val from = "no-reply@jidemail.com"

  private val fromName = "JideService"

  /**
    * Sends an email
    */
  def sendMail(sendTo: String, subject: String, content: String): Boolean = {

    var requestForm = ListMap[String, Seq[String]]()
    requestForm += ("api_user" -> Seq(api_user))
    requestForm += ("api_key" -> Seq(api_key))
    requestForm += ("to" -> Seq(sendTo))
    requestForm += ("from" -> Seq(from))
    requestForm += ("fromname" -> Seq(fromName))
    requestForm += ("subject" -> Seq(subject))
    requestForm += ("html" -> Seq(content))
    var result = requestEmailPost(requestForm)
    if ((Json.parse(result) \ "message").as[String] == "success") {
      true
    } else {
      false
    }
  }

  def englishRegisterTpl(token: String): String = {
    "<html>" +
      "<head></head>" +
      "<body>" +
      "<p>Thanks for signing up for your Jide account!</p>" +
      "<p><a href=" + "https://shop.jide.com/registerVerify?Emailhash=" + token + " " +
      "target='_blank'>Please click here to confirm your email address, or you can copy and paste" +
      " the following link to your browser:</a></p>" +
      "<p>https://shop.jide.com/registerVerify?Emailhash=" + token + "</p>" +
      "<p>Thanks,</p>" +
      "<p>Jide Team and #RemixOn!</p>" +
      "</body>" +
      "</html>"
    //    "<html><head></head><body>" + "<p>Welcome to register Jide account<a href=https://shop
    // .jide.com/registerVerify?Emailhash="+ token +">Register Now! (https://shop.jide
    // .com/registerVerify?Emailhash=" + token + ")</a></p>" + "</body></html> "
  }

  def enShipmentConfirmTpl(shipmentInfo: Map[String, Any]): String = {
    val goodsName = shipmentInfo("goodsName")
    var goodsList = ""
    goodsName.asInstanceOf[JsArray].value foreach (goodName =>
      goodsList = goodsList + "<p><span>·</span>" + goodName.toString.replaceAll("\"", "") + "</p>"
      )
    val shipmentAddress = shipmentInfo("shipmentAddress").asInstanceOf[String].split(",")

    "<html>" +
      "<p>Hi " + shipmentInfo("userName") + ",</p>" +
      "<br/>" +
      "<p>Great news! Your Order has shipped!</p>" +
      "<p>It was sent via " + shipmentInfo(
      "shipmentCompany") + " with tracking number " + shipmentInfo(
      "shipmentNumber") + ". Please refer to the carrier's website for tracking details.</p>" +
      "<p>Your shipment details are as follows:</p>" +
      goodsList +
      "<br/>" +
      "<p>" + shipmentAddress.slice(0, 2).mkString(",") + "</p>" + //name
      "<p>" + shipmentAddress.slice(2, shipmentAddress.size - 1).mkString(",") + "</p>" + //address
      "<p>" + shipmentAddress.slice(shipmentAddress.size - 1, shipmentAddress.size)
      .mkString(",") + "</p>" + //phone
      "<br/>" +
      "<p>If you have any questions, please see our Online Store FAQ " +
      "<a href='http://support.jide.com/hc/en-us/sections/201159857-FAQ-for-Official-Online-Store-Purchase-'>here</a>" +
      "or feel free to write to us <a href='mailto:support@jidemail.com'>here</a>.</p>" +
      "<br/>" +
      "<p>Thanks,</p>" +
      "<p>-The Remix Team</p>" +
      "</html>"
  }

  def enOrderConfirmTpl(): String = {
    """
      |<html>
      |
      |<head></head>
      |
      |<body>
      |
      |   <p>Greetings from Jide,</p>
      |
      |   <br>
      |
      |   <p>Thank you! Your order has been placed successfully through the Jide Store.</p>
      |
      |   <p>Be on the look for a follow-up email with your tracking number once your order has
      |   been shipped. This will usually take 1-3 business days, but in rare cases, it may take
      |   longer. Once we've shipped your RemixOS device, we will send a follow-up email with a
      |   tracking number. </p>
      |
      |   <p>Should you have any further questions or issues, please checkout our Online Store FAQ
      |   <a href="http://support.jide.com/hc/en-us/sections/201159857-FAQ-for-Official-Online-Store-Purchase-">here</a>
      |   or feel free to write to us <a href="mailto:support@jidemail.com">here</a>.</p>
      |
      |   <p>Thanks again for your support and welcome to the Remix community!</p>
      |
      |</body>
      |
      |</html>
    """.stripMargin
  }

  def enOrderGuestConfirmTpl: String = {
    """
      |<html>
      |
      |<head></head>
      |
      |<body>
      |
      |   <p>Greetings from Jide,</p>
      |
      |   <br>
      |
      |   <p>Thank you! Your order has been placed successfully through the Jide Store.</p>
      |
      |   <p>Be on the look for a follow-up email with your tracking number once your order has
      |   been shipped. This will usually take 1-3 business days, but in rare cases, it may take
      |   longer. Once we've shipped your RemixOS device, we will send a follow-up email with a
      |   tracking number. </p>
      |
      |   <p>Should you have any further questions or issues, please checkout our Online Store FAQ
      |   <a href="http://support.jide.com/hc/en-us/sections/201159857-FAQ-for-Official-Online-Store-Purchase-">here</a>
      |   or feel free to write to us <a href="mailto:support@jidemail.com">here</a>.</p>
      |
      |   <p>Thanks again for your support and welcome to the Remix community!</p>
      |
      |</body>
      |
      |</html>
    """.stripMargin
  }

  def resetPasswordTpl: String = {
    """
      |<html>
      |
      |<head></head>
      |
      |<body>
      |
      |<p>You have requested a password reset. Please visit the below link to enter your new
      |password:</p>
      |
      |<p>https://shop.jide.com/en/resetPasswordVerify?Emailhash=%s</p>
      |
      |<p>This link will only be valid for the next 30 minutes. If you do not reset your password
      | within the next 30 minutes, you will need to restart the password reset process.</p>
      |
      |<p>Thank you!</p>
      |
      |<p>- The Remix Team</p>
      |
      |</body>
      |
      |</html>
    """.stripMargin


  }

  def requestEmailPost(requestForm: Map[String, Seq[String]]): String = {
    val future: Future[String] = WS.url("http://sendcloud.sohu.com/webapi/mail.send.json")
      .post(requestForm).map { response =>
      response.body
    }
    Await.result(future, 10 seconds)
  }
}
