package wechatutil

import WxConfig._
import play.api.libs.json.Json
import util.RequestHelper
/**
  * Created by ruibing on 16/9/1.
  */
object WxUtil {

  private val openIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?"

  def getOpenId(code: String): String = {
    val requestContent = openIdUrl + "appid=" + WxConfig
      .wx_getAppId + "&secret=" + WxConfig
      .wx_getSecret + "&code=" + code + "&grant_type=authorization_code"
    val res = RequestHelper.requestGet(requestContent)
    (Json.parse(res) \\ "openid").mkString
  }
}
