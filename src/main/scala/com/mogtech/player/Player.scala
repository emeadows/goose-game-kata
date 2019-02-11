package com.mogtech.player

import com.mogtech.Result
import com.mogtech.utils._

// used to prevent apply method so Players always start at 0
sealed abstract case class Player(name: String, position: Int)

case class PlayerUpdate(message: String, players: List[Player])

object Player {

  def newPlayer(name: String): Player =
    new Player(name, 0) {}

  def updatePlayer(player: Player, newPosition: Int): Player =
    new Player(player.name, newPosition) {}

  def addPlayerToList(name: String, players: List[Player]): PlayerUpdate =
    players.find(_.name == name) match {
      case Some(_) => PlayerUpdate(s"$name: already existing player.", players)
      case None if players.size < 2 =>
        val created = newPlayer(name)
        val updatedPlayersList: List[Player] = created :: players
        PlayerUpdate(s"players: ${updatedPlayersList.map(_.name).mkString(", ")}", updatedPlayersList)
      case None => PlayerUpdate(TooManyPlayers.message, players)
    }

  def findPlayer(name: String, players: List[Player]): Either[ErrorCase, Player] =
    players.find(_.name == name) match {
      case None => Left(PlayerNotFound(name))
      case Some(player) => Right(player)
    }

  def findOtherPlayer(name: String, players: List[Player]): Either[ErrorCase, Player] =
    (for {
      namedPlayer <- players.find(_.name == name)
      otherPlayer <- players.filterNot(_ == namedPlayer).headOption
    } yield otherPlayer) match {
      case None => Left(UnableToFindOtherPlayer(name))
      case Some(player) => Right(player)
    }

  def movePlayer(name: String,
                 die: Either[ErrorCase, DiceRoll],
                 players: List[Player],
                 function: (Player, DiceRoll, Player) => Result): PlayerUpdate = {
    val tryMove = for {
      player <- findPlayer(name, players)
      otherPlayer <- findOtherPlayer(name, players)
      diceRoll <- die
    } yield function(player, diceRoll, otherPlayer)
    tryMove match {
      case Left(err: ErrorCase) => PlayerUpdate(err.message, players)
      case Right(success) =>
        PlayerUpdate(success.message, success.players)
    }
  }
}

