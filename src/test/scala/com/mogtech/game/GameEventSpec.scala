package com.mogtech.game

import org.scalatest.{FlatSpec, Matchers}
import com.mogtech.player.Player
import com.mogtech.utils.ExampleConstants

class GameEventSpec extends FlatSpec with Matchers {

  val game = new GameEvent(ExampleConstants)
  val otherPlayer: Player = {
    val player = Player.newPlayer("Bob")
    Player.updatePlayer(player, 9)
  }

  "logic" should "return victory if end on final Space" in {
    val result = game.logic(61, 2, otherPlayer)
    result shouldBe EventResult(Victory(63), otherPlayer)
  }

  it should "return moved if no additional events occur" in {
    val result = game.logic(0, 2, otherPlayer)
    result shouldBe EventResult(Moved(2), otherPlayer)
  }

  it should "return offBoard if no additional events occur" in {
    val result = game.logic(60, 5, otherPlayer)
    result shouldBe EventResult(OffBoard(61), otherPlayer)
  }

  it should "return bridge if lands on a 6" in {
    val result = game.logic(4, 2, otherPlayer)
    result shouldBe EventResult(Bridge(12), otherPlayer)
  }

  it should "return goose if a single goose Space" in {
    val result = game.logic(16, 2, otherPlayer)
    result shouldBe EventResult(Goose(18, EventResult(Moved(20), otherPlayer), 2), otherPlayer)
  }

  it should "return goose if a multi goose Space" in {
    val result = game.logic(10, 4, otherPlayer)
    result shouldBe EventResult(Goose(14, EventResult(Goose(18, EventResult(Moved(22), otherPlayer), 4), otherPlayer), 4), otherPlayer)
  }

  it should "return a prank event space occupied by another player" in {
    val updatedPlayer = Player.updatePlayer(otherPlayer, 7)
    val result = game.logic(7, 2, otherPlayer)
    result shouldBe EventResult(Prank(7, 9, otherPlayer.name), updatedPlayer)
  }

}
