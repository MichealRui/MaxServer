package util

import play.api.libs.json._

object MessageHelper {

  val kSendMobileMsgUrl = "https://api.jide.com/verify/phone/send"

  val verifyUrl = "https://api.jide.com/user/register-or-login"

  def sendMsg(phone_number: String, ip: String): String = {
    RequestHelper.requestPost(
      kSendMobileMsgUrl,
      Json.stringify(Json.obj(
        "source_type" -> "web",
        "source_id" -> ip,
        "phone" -> phone_number,
        "action" -> "REGISTER_OR_LOGIN"
      ))
    )
  }

  def registerVerify(phone_number: String, ip: String, password: String,
    verifyCode: String): String = {
    RequestHelper.requestPost(
      verifyUrl,
      Json.stringify(Json.obj(
        "source_type" -> "web",
        "source_id" -> ip,
        "account_name" -> phone_number,
        "verify_code" -> verifyCode,
        "password" -> password
      ))
    )
  }
}
