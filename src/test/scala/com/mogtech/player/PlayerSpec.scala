package com.mogtech.player

import org.scalatest.{FlatSpec, Matchers}
import Player.newPlayer
import com.mogtech.Result
import com.mogtech.utils.{InvalidDiceRoll, PlayerNotFound, UnableToFindOtherPlayer}

class PlayerSpec extends FlatSpec with Matchers {

  val kate: Player = newPlayer("Kate")
  val bob: Player = newPlayer("Bob")
  val playersBobAndKate = List(kate, bob)

  "addPlayerToList" should "add named player and update collection with only player if empty" in {
    val addBob = Player.addPlayerToList("Bob", Nil)
    addBob shouldBe PlayerUpdate("players: Bob", List(bob))
  }

  it should "add named player and update collection" in {
    val players = List(kate)
    val result = Player.addPlayerToList("Bob", players)
    result shouldBe PlayerUpdate("players: Bob, Kate", bob :: players)
  }

  it should "not add the same player twice" in {
    val players = List(kate)
    val result = Player.addPlayerToList("Kate", players)
    result shouldBe PlayerUpdate("Kate: already existing player.", players)
  }

  it should "only all 2 players" in {
    val result = Player.addPlayerToList("Zac", playersBobAndKate)
    result shouldBe PlayerUpdate("Game currently only supports 2 players.", playersBobAndKate)
  }

  "findPlayer" should "return the player if in list" in {
    val players = List(kate)
    Player.findPlayer("Kate", players) shouldBe Right(players.head)
  }
  
  it should "return error if not in list" in {
    val players = List(kate)
    Player.findPlayer("Bob", players) shouldBe Left(PlayerNotFound("Bob"))
  }

  "findOtherPlayer" should "return the other player if name found" in {
    Player.findOtherPlayer("Bob", playersBobAndKate) shouldBe Right(kate)
  }

  it should "error if player not found" in {
    Player.findOtherPlayer("Zac", playersBobAndKate) shouldBe Left(UnableToFindOtherPlayer("Zac"))
  }

  "movePlayer" should "return message in function if set up is correct" in {
    val function: (Player, DiceRoll, Player) => Result = (player1, b, player2) => Result(player1 :: player2 :: Nil, "Message")

    val result = Player.movePlayer("Bob", Right(DiceRoll(1, 1)), playersBobAndKate, function)

    result.message shouldBe "Message"
  }

  it should "return error if dice roll is wrong" in {
    val function: (Player, DiceRoll, Player) => Result = (player1, b, player2) => Result(player1 :: player2 :: Nil, "Message")

    val result = Player.movePlayer("Bob", Left(InvalidDiceRoll(7)), playersBobAndKate, function)

    result.message shouldBe "Invalid dice roll of 7."
  }
}
