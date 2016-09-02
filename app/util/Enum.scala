package util

abstract class EnumClass

abstract class EnumObject[X, Y] {

  val value2keyMap: Map[X, Y]

  def fromValue(value: X): Option[Y] = {
    if (value2keyMap.contains(value)) Some(value2keyMap(value)) else None
  }
}
