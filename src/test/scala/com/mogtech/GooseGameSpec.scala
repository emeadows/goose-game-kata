package com.mogtech

import com.mogtech.game.GameEvent
import com.mogtech.player.{DiceRoll, Player}
import com.mogtech.utils.ExampleConstants
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

// acceptance tests
class GooseGameSpec extends FeatureSpec with GivenWhenThen with Matchers {

  val game = new GooseGame(new GameEvent(ExampleConstants))

  feature("Move a player") {

    scenario("From Start") {

      Given("there are two participants 'Pippo' and 'Pluto' on space 'Start'")
      val pippo = createPippo
      val pluto = createPluto

      When("a dice roll of (4, 3) is returned for Pippo")
      val diceRoll = DiceRoll(4 -> 3)

      Then("The message in the result should show the move to 7")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 4, 3. Pippo moves from Start to 7."
    }

    scenario("move other player") {

      Given("there are two participants 'Pippo' on the space '7' and 'Pluto' on space 'Start'")
      val pippo = updatePippo(7)
      val pluto = createPluto

      When("a dice roll of (2, 2) is returned for Pippo")
      val diceRoll = DiceRoll(2 -> 2)

      Then("The message in the result should show the move to 4")
      val result = game.applyRoll(pluto, diceRoll, pippo)

      result.message shouldBe "Pluto rolls 2, 2. Pluto moves from Start to 4."
    }

    scenario("move player") {

      Given("there are two participants 'Pippo' on the space '4' and 'Pluto'")
      val pippo = updatePippo(4)
      val pluto = createPluto

      When("a dice roll of (1, 2) is returned for Pippo")
      val diceRoll = DiceRoll(1 -> 2)

      Then("The message in the result should show the move to 7")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 1, 2. Pippo moves from 4 to 7."
    }
  }

  feature("Player Wins") {

    scenario("Player lands on space 63") {

      Given("there are two participants 'Pippo' on space 60 and 'Pluto'")
      val pippo = updatePippo(60)
      val pluto = updatePluto(50)

      When("a dice roll of (1, 2) is returned for Pippo")
      val diceRoll = DiceRoll(1 -> 2)

      Then("The message in the result should the player has won")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 1, 2. Pippo moves from 60 to 63. Pippo Wins!!"
    }

    scenario("no win if the exact dice miss final space") {

      Given("there are two participants 'Pippo' on space 60 and 'Pluto'")
      val pippo = updatePippo(60)
      val pluto = updatePluto(50)

      When("a dice roll of (3, 2) is returned for Pippo")
      val diceRoll = DiceRoll(3 -> 2)

      Then("The message in the result should the player has bounced")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 3, 2. Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 61."
    }
  }

  feature("The Bridge") {

    scenario("I get to the space 'The Bridge', I jump to the space '12'") {

      Given("there are two participants 'Pippo' on space 4 and 'Pluto'")
      val pippo = updatePippo(4)
      val pluto = createPluto

      When("a dice roll of (1, 1) is returned for Pippo")
      val diceRoll = DiceRoll(1 -> 1)

      Then("The message in the result should the player has jumped on the bridge")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 1, 1. Pippo moves from 4 to The Bridge. Pippo jumps to 12."
    }
  }

  feature("The Goose") {

    scenario("when I get to a space with a picture of 'The Goose', I move forward again by the sum of the two dice rolled before") {

      Given("there are two participants 'Pippo' on space 3 and 'Pluto'")
      val pippo = updatePippo(3)
      val pluto = createPluto

      When("a dice roll of (1, 1) is returned for Pippo")
      val diceRoll = DiceRoll(1 -> 1)

      Then("The message in the result should the player has jumped forward twice")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 1, 1. Pippo moves from 3 to 5, The Goose. Pippo moves again and goes to 7."
    }

    scenario("every space with a picture of 'The Goose' I land on, I move forward again by the number of goose spaces") {

      Given("there are two participants 'Pippo' on space 10 and 'Pluto'")
      val pippo = updatePippo(10)
      val pluto = createPluto

      When("a dice roll of (2, 2) is returned for Pippo")
      val diceRoll = DiceRoll(2 -> 2)

      Then("The message in the result should the player has jumped forward twice")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 2, 2. Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22."
    }
  }

  feature("Prank") {

    scenario("when I land on a space occupied by another player, I send him to my previous position") {

      Given("there are two participants 'Pippo' and 'Pluto' respectively on spaces '15' and '17'")
      val pippo = updatePippo(15)
      val pluto = updatePluto(17)

      When("a dice roll of (1, 1) is returned for Pippo")
      val diceRoll = DiceRoll(1 -> 1)

      Then("The message in the result should the player has switched places")
      val result = game.applyRoll(pippo, diceRoll, pluto)

      result.message shouldBe "Pippo rolls 1, 1. Pippo moves from 15 to 17. On 17 there is Pluto, who returns to 15."
    }
  }

  def createPippo: Player = Player.newPlayer("Pippo")

  def createPluto: Player = Player.newPlayer("Pluto")

  def updatePippo(moveTo: Int): Player = Player.updatePlayer(createPippo, moveTo)

  def updatePluto(moveTo: Int): Player = Player.updatePlayer(createPluto, moveTo)

}
