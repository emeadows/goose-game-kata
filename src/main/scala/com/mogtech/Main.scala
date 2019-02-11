package com.mogtech

import com.mogtech.game.GameEvent
import com.mogtech.player.{DiceRoll, Player}
import com.mogtech.utils._

import scala.io.StdIn

object Main extends App {
  var players: List[Player] = Nil
  val game = new GooseGame(new GameEvent(ExampleConstants))

  def processCommand(command: String): String = {
    command.split(' ').toList match {
      case "add" :: "player" :: name :: Nil => {
        val result = Player.addPlayerToList(name, players)
        players = result.players
        result.message
      }
      case _ if players.length != 2 => InsufficientPlayers.message
      case "move" :: name :: Nil =>
        val result = Player.movePlayer(name, DiceRoll.generate, players, game.applyRoll)
        players = result.players
        result.message
      case "move" :: name :: d1 :: d2 :: Nil if d1.length == 2 && d2.length == 1=>
        val result = Player.movePlayer(name, DiceRoll.convertInput(d1.head, d2.head), players, game.applyRoll)
        players = result.players
        result.message
      case _ => UnexpectedMessage(command).message
    }
  }

  println("Welcome to the Goose Game")
  println("Please add players using 'add player <name>'")
  println("Please move players using 'move <name>' for generated dice rolls or 'move <name> <diceRoll1>, <diceRoll2>'")

  while (!players.exists(_.position == 63)) {
    val command = StdIn.readLine()
    val response = processCommand(command)
    println(response)
  }

  println("Goose Game completed.")
}
