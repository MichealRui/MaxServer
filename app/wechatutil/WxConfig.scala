package wechatutil

import java.net._

import scala.util.Random

object WxConfig {

  /*
  * WX Pay config
  * */
  private val appid = "wx1cc64cd8dc80eb77" // NEW

  private val secret = "9df2bc15e5b353508d2a9e44740054b8" // NEW

  private val mch_id = "1290825501" // NEW

  private val key = "SZc1bee7d0f3f0b05ab9886e32512dfb" // NEW

  private val ip = InetAddress.getLocalHost.getHostAddress

  //  private val notifyUrl = "http://foo.corp.jide.com/payment/notify"
//  private val notifyUrl = Play.configuration.getString("host").get + "payment/notify"


  def wx_getAppId(): String = {
    appid
  }

  def wx_getSecret(): String = {
    secret
  }

  def wx_getMchId(): String = {
    mch_id
  }

  def wx_getKey(): String = {
    key
  }

  def wx_getIp(): String = {
    ip
  }

  def wx_getNonceStr: String = {
    Random.alphanumeric.take(32).mkString
  }

//  def wx_getNofifyUrl: String = {
//    notifyUrl
//  }

}

trait WxConfig {
  val appid = WxConfig.wx_getAppId()
  val secret = WxConfig.wx_getSecret()
  val mch_id = WxConfig.wx_getMchId()
  val key = WxConfig.wx_getKey()
  val ip = WxConfig.wx_getIp()
  val nonce_str = WxConfig.wx_getNonceStr
//  val notify_url = WxConfig.wx_getNofifyUrl

  def getAppId = appid
  def getSecret = secret
  def getMchId = mch_id
  def getKey = key
  def getIp = ip
  def getNonceStr = nonce_str
//  def getNotifyUrl = notify_url
}
