package com.mogtech.player

import com.mogtech.utils.{InvalidDiceRoll, InvalidDiceRollTwice, UnableToParseRoll}
import org.scalatest.{FlatSpec, Matchers}

class DiceRollSpec extends FlatSpec with Matchers {

  "generate" should "return 2 random valid rolls" in {
    0.to(100).map { _ =>
      val rollResult = DiceRoll.generate
      rollResult match {
        case Right(diceRoll) => {
          val roll1 = diceRoll.values._1
          val roll2 = diceRoll.values._2
          roll1 should be > 0
          roll1 should be <= 6
          roll2 should be > 0
          roll2 should be <= 6
          val total = roll1 + roll2
          total should be > 0
          total should be <= 12
        }
        case Left(_) => fail
      }
    }
  }

  "convertInput" should "return valid rolls if numbers between 1 - 6 supplied" in {
    DiceRoll.convertInput('2', '5') match {
      case Right(dr) => dr shouldBe DiceRoll(2, 5)
      case _ => fail
    }
  }

  it should "return error code UnableToParseRoll if numbers not provided" in {
    DiceRoll.convertInput('a', '5') match {
      case Left(UnableToParseRoll("a")) => succeed
      case _ => fail
    }
  }

  it should "return error code InvalidDiceRoll if numbers are outside range" in {
    DiceRoll.convertInput('7', '2') match {
      case Left(InvalidDiceRoll(7)) => succeed
      case _ => fail
    }
  }
}
