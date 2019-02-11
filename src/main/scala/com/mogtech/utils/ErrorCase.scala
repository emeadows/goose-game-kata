package com.mogtech.utils

sealed trait ErrorCase {
  def message: String
}
case class InvalidDiceRoll(value: Int) extends ErrorCase {
  def message: String = s"Invalid dice roll of $value."
}
case class InvalidDiceRollTwice(d1: Int, d2: Int) extends ErrorCase {
  def message: String = s"Invalid dice roll of $d1 and $d2."
}
case class PlayerNotFound(name: String) extends ErrorCase {
  def message: String = s"No player of the name $name found."
}
case object TooManyPlayers extends ErrorCase {
  def message: String = "Game currently only supports 2 players."
}
case object InsufficientPlayers extends ErrorCase {
  def message: String = "2 players are required. Please add players."
}
case class UnableToFindOtherPlayer(name: String) extends ErrorCase {
  def message: String = s"Unable to find the other player to $name"
}
case class UnableToParseRoll(d: String) extends ErrorCase {
  def message: String = s"$d cannot be converted to an Int."
}
case class UnexpectedMessage(msg: String) extends ErrorCase {
  def message: String = s"Input of '$msg' not recognised."
}