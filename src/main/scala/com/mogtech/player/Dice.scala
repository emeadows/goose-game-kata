package com.mogtech.player


import com.mogtech.utils.{ErrorCase, InvalidDiceRoll}

import scala.util.Random

// using a sealed abstract class to prevent apply method being used
sealed abstract case class Dice(roll: Int) {
  def value: Int = roll
}

object Dice {

  // this is based on a D6
  val lowestNumber = 0
  val highestNumber = 6

  def fromInt(roll: Int): Either[InvalidDiceRoll, Dice] =
    roll match {
      case value if 0 < value && value <= 6 => Right(new Dice(value) {})
      case value => Left(InvalidDiceRoll(value))
    }

  def randomInRange(rng: Random): Int = {
    Math.abs(rng.nextInt() % 6) + 1
  }

  def randomlyGenerateDiceRoll: (Either[InvalidDiceRoll, Dice], Either[InvalidDiceRoll, Dice]) = {
    val random = new Random()
    val roll1 = randomInRange(random)
    val roll2 = randomInRange(random)

    val dice1 = Dice.fromInt(roll1)
    val dice2 = Dice.fromInt(roll2)

    (dice1, dice2)
  }

}