package com.mogtech.utils

// in reality you'd pull this out in to a config file
sealed trait Constants {
  val winningSpace: Int = 63
  val bridgeFromSpace: Int = 6
  val bridgeToSpace: Int = 12
  val gooseSpaces: List[Int] = List(5, 9, 14, 18, 23, 27)
}

case object ExampleConstants extends Constants


