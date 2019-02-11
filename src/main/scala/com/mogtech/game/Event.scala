package com.mogtech.game

import com.mogtech.player.Player

import scala.annotation.tailrec

sealed trait Event {

  def finalPosition: Int

  def message(player: Player, startFrom: Int): String = {
    val name = player.name
    buildMessage(name, startFrom, message(name))
  }

  def message(name: String): List[String]

  @tailrec
  final def buildMessage(name: String, from: Int, messages: List[String], result: String = ""): String =
    messages match {
      case Nil => result
      case head :: tail if result.isEmpty && from == 0 =>
        buildMessage(name, from, tail, s"$name moves from Start to $head")
      case head :: tail if result.isEmpty =>
        buildMessage(name, from, tail, s"$name moves from $from to $head")
      case head :: tail =>
        buildMessage(name, from, tail, s"$result $name moves again and goes to $head")
    }
}

case class Victory(endPosition: Int) extends Event {
  val finalPosition: Int = endPosition

  def message(name: String): List[String] =
    List(s"$endPosition. $name Wins!!")
}

case class Moved(endPosition: Int) extends Event {
  val finalPosition: Int = endPosition
  def message(name: String): List[String] =
    List(s"$endPosition.")
}

case class Bridge(endPosition: Int) extends Event {
  val finalPosition: Int = endPosition
  def message(name: String): List[String] =
    List(s"The Bridge. $name jumps to $endPosition.")
}

case class Goose(gooseMatch: Int, result: EventResult, move: Int) extends Event {
  val event: Event = result.event
  val finalPosition: Int = event.finalPosition

  def message(name: String): List[String] =
    s"$gooseMatch, The Goose." +:
      event.message(name)
}

case class OffBoard(endPosition: Int) extends Event {
  val finalPosition: Int = endPosition
  def message(name: String): List[String] =
    List(s"63. $name bounces! $name returns to $endPosition.")
}


case class Prank(startPosition: Int, endPosition: Int, otherPlayer: String) extends Event {
  val finalPosition: Int = endPosition
  def message(name: String): List[String] =
  List(s"$endPosition. On $endPosition there is $otherPlayer, who returns to $startPosition.")

}

