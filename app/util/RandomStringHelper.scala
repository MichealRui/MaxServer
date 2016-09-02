/**
 * Created by ruibing on 12/18/15.
 */
package util

import scala.annotation.tailrec
import scala.util.Random


object RandomStringHelper {

  def main(args: Array[String]) {
    println("1:  " + randomString(10))
    println("2:  " + randomStringArray(10))
    println("3:  " + randomStringRecursive(10).mkString)
    println("3:  " + randomStringRecursive2(10).mkString)
    println("4:  " + randomStringTailRecursive(10, Nil).mkString)
    println("5:  " + randomStringRecursive2Wrapper(10))
    println("6:  " + randomAlphaNumericString(10))
    println("6:  " + randomAlphaNumericString(10))
    println("6:  " + randomAlphaNumericString(10))
    println("x2: " + x2(10, ('a' to 'z') ++ ('A' to 'Z')))
  }

  // 1 - a 'normal' java-esque approach
  def randomString(length: Int) = {
    val r = new scala.util.Random
    val sb = new StringBuilder
    for (i <- 1 to length) {
      sb.append(r.nextPrintableChar)
    }
    sb.toString
  }

  // 2 - similar to #1, but using an array
  def randomStringArray(length: Int) = {
    val r = new scala.util.Random
    val a = new Array[Char](length)
    val sb = new StringBuilder
    for (i <- 0 to length-1) {
      a(i) = r.nextPrintableChar
    }
    a.mkString
  }

  // 3 - recursive, but not tail-recursive
  def randomStringRecursive(n: Int): List[Char] = {
    n match {
      case 1 => List(Random.nextPrintableChar)
      case _ => List(Random.nextPrintableChar) ++ randomStringRecursive(n-1)
    }
  }

  // 3b - recursive, but not tail-recursive
  def randomStringRecursive2(n: Int): String = {
    n match {
      case 1 => Random.nextPrintableChar.toString
      case _ => Random.nextPrintableChar.toString ++ randomStringRecursive2(n-1).toString
    }
  }

  // 4 - tail recursive, no wrapper
  @tailrec
  def randomStringTailRecursive(n: Int, list: List[Char]):List[Char] = {
    if (n == 1) Random.nextPrintableChar :: list
    else randomStringTailRecursive(n-1, Random.nextPrintableChar :: list)
  }

  // 5 - a wrapper around the tail-recursive approach
  def randomStringRecursive2Wrapper(n: Int): String = {
    randomStringTailRecursive(n, Nil).mkString
  }

  // 6 - random alphanumeric
  def randomAlphaNumericString(length: Int): String = {
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    randomStringFromCharList(length, chars)
  }

  // 7 - random alpha
  def randomAlpha(length: Int): String = {
    val chars = ('a' to 'z') ++ ('A' to 'Z')
    randomStringFromCharList(length, chars)
  }

  // used by #6 and #7
  def randomStringFromCharList(length: Int, chars: Seq[Char]): String = {
    val sb = new StringBuilder
    for (i <- 1 to length) {
      val randomNum = Random.nextInt(chars.length)
      sb.append(chars(randomNum))
    }
    sb.toString
  }

  def x(length: Int, chars: Seq[Char]): String = {
    val list = List.range(1, length)
    val arr = new Array[Char](length)
    list.foreach{ e => arr(e) = chars(Random.nextInt(chars.length)) }
    list.mkString
  }

  // create a fake list so i can use map (or flatMap)
  def x2(length: Int, chars: Seq[Char]): String = {
    val tmpList = List.range(0, length)
    val charList = tmpList.map{ e => chars(Random.nextInt(chars.length)) }
    return charList.mkString
  }

}