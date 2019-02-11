package com.mogtech.game

import com.mogtech.player.Player
import com.mogtech.utils.Constants


case class EventResult(event: Event, otherPlayer: Player) {
  val finalPosition: Int = event.finalPosition
}

class GameEvent(constants: Constants) {

  import constants._

  def logic(initalPosition: Int, move: Int, otherPlayer: Player): EventResult = {

    def isGooseSpace(Space: Int): Boolean = gooseSpaces.contains(Space)
    def offBoard(move: Int): Boolean = move > winningSpace

    initalPosition + move match {
      case prank if prank == otherPlayer.position =>
        val movePlayer = Player.updatePlayer(otherPlayer, initalPosition)
        EventResult(Prank(initalPosition, prank, otherPlayer.name), movePlayer)

      case win if win == winningSpace =>
        EventResult(Victory(winningSpace), otherPlayer)

      case bridge if bridge == bridgeFromSpace =>
        EventResult(Bridge(bridgeToSpace), otherPlayer)

      case goose if isGooseSpace(goose) =>
        EventResult(Goose(goose, logic(goose, move, otherPlayer), move), otherPlayer)

      case tooHigh if offBoard(tooHigh) =>
        val result = winningSpace - (tooHigh - winningSpace)
        EventResult(OffBoard(result), otherPlayer)

      case moved =>
        EventResult(Moved(moved), otherPlayer)

    }
  }


}
