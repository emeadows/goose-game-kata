package com.mogtech

import com.mogtech.game.GameEvent
import com.mogtech.player.{DiceRoll, Player}

case class Result(players: List[Player], message: String)

class GooseGame(gameEvent: GameEvent) {

  def applyRoll(player: Player, roll: DiceRoll, otherPlayer: Player): Result = {
    val eventResult = gameEvent.logic(player.position, roll.total, otherPlayer)
    val movedPlayer = Player.updatePlayer(player, eventResult.finalPosition)
    val displayText = s"${player.name} rolls ${roll.values._1}, ${roll.values._2}. ${eventResult.event.message(movedPlayer, player.position)}"
    Result(movedPlayer :: eventResult.otherPlayer :: Nil, displayText)
  }

}
