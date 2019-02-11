package com.mogtech.player

import java.util.Random

import org.scalatest.{Assertion, FlatSpec, Matchers}
import com.mogtech.utils.{ErrorCase, InvalidDiceRoll}

class DiceSpec extends FlatSpec with Matchers {

  "DiceRoll" should "allow numbers between 1-6" in {
    assertSuccess(Dice.fromInt(1), 1)
    assertSuccess(Dice.fromInt(6), 6)
  }

  it should "return left error if 0" in {
    assertFailure(Dice.fromInt(0), 0)
  }

  it should "return left error if negitive number" in {
    assertFailure(Dice.fromInt(-42), -42)
  }

  it should "return left error if greater than 6" in {
    assertFailure(Dice.fromInt(7), 7)
  }

  // could use property based testing like scala check
  "randomInRange" should "always return a number between 1 -6" in {
    val beWithin1and6 = be >= 1 and be <= 6

      0.to(100).map { _ =>
        val rollResult = Dice.randomInRange(new Random())
        rollResult should beWithin1and6
      }
  }

  def assertSuccess(diceRoll: Either[InvalidDiceRoll, Dice], expected: Int): Assertion =
    diceRoll match {
      case Left(InvalidDiceRoll(_)) => fail
      case Right(Dice(roll)) => roll shouldBe expected
    }

  def assertFailure(diceRoll: Either[InvalidDiceRoll, Dice], expected: Int): Assertion =
    diceRoll match {
      case Left(InvalidDiceRoll(roll)) => roll shouldBe expected
      case Right(Dice(_)) => fail
    }
}
