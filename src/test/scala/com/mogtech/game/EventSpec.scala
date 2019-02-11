package com.mogtech.game

import org.scalatest.{FlatSpec, Matchers}
import com.mogtech.player.Player

class EventSpec extends FlatSpec with Matchers {

  val newPippo: Player = Player.newPlayer("Pippo")
  val pluto: Player = {
    val player = Player.newPlayer("Pluto")
    Player.updatePlayer(player, 30)
  }

  "Completed" should "return winner text" in {
    val pippo: Player = Player.updatePlayer(newPippo, 63)
    Victory(63).message(pippo, 61) shouldBe "Pippo moves from 61 to 63. Pippo Wins!!"
  }

  "Moved" should "return move text with start instead of 0" in {
    val pippo: Player = Player.updatePlayer(newPippo, 4)
    Moved(4).message(pippo, 0) shouldBe "Pippo moves from Start to 4."
  }

  "Moved" should "return move text" in {
    val pippo: Player = Player.updatePlayer(newPippo, 7)
    Moved(7).message(pippo, 4) shouldBe "Pippo moves from 4 to 7."
  }

  "Bridge" should "return move text" in {
    val pippo: Player = Player.updatePlayer(newPippo, 12)
    Bridge(12).message(pippo, 4) shouldBe "Pippo moves from 4 to The Bridge. Pippo jumps to 12."
  }

  "OffBoard" should "give a message to show it bounced off the end of the board" in {
    val pippo: Player = Player.updatePlayer(newPippo, 61)
    OffBoard(61).message(pippo, 60) shouldBe "Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 61."
  }

  "Goose" should "return single jump text if one match" in {
    val pippo: Player = Player.updatePlayer(newPippo, 7)
    val logicResult: EventResult = EventResult(Moved(7), pluto)
    val result = Goose(5, logicResult, 2).message(pippo, 3)
    result shouldBe "Pippo moves from 3 to 5, The Goose. Pippo moves again and goes to 7."
  }

  it should "return a message with all multi jumps if two matches" in {
    val pippo: Player = Player.updatePlayer(newPippo, 22)
    val logicResult: EventResult = EventResult(Moved(22), pluto)
    val gooseAt18 = Goose(18, logicResult, 4)
    val gooseLogicResult = EventResult(Goose(18, logicResult, 4), pluto)
    val result = Goose(14, gooseLogicResult, 4).message(pippo, 10)
    result shouldBe "Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22."
  }

  it should "work" in {
    val pippo: Player = Player.updatePlayer(newPippo, 10)
    val actual: EventResult = EventResult(Goose(14, EventResult(Goose(18, EventResult(Moved(22), pluto), 4), pluto), 4), pluto)
    actual.event.message(pippo, 10) shouldBe  "Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22."
  }

  "Prank" should "return message explaining the 2 players have swapped positions" in {
    val pippo: Player = Player.updatePlayer(newPippo, 17)
    val result = Prank(15, 17, "Pluto").message(pippo, 15)
    result shouldBe "Pippo moves from 15 to 17. On 17 there is Pluto, who returns to 15."
  }
}
