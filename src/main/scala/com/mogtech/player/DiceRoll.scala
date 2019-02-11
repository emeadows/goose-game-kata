package com.mogtech.player

import com.mogtech.utils.{ErrorCase, InvalidDiceRoll, InvalidDiceRollTwice, UnableToParseRoll}

import scala.util.Try

// as this is side effected code we should conside a STATE monad (but is a bit over kill here)

case class DiceRoll(values: (Int, Int)) {
  val total: Int = values._1 + values._2
}

object DiceRoll {
  def apply(roll: (Either[InvalidDiceRoll, Dice], Either[InvalidDiceRoll, Dice])): Either[ErrorCase, DiceRoll] =
    roll match {
      case (Right(r1), Right(r2)) => Right(DiceRoll(r1.value -> r2.value))
      case (Left(e1), Right(_)) => Left(e1)
      case (Right(_), Left(e2)) => Left(e2)
      case (Left(InvalidDiceRoll(e1)), Left(InvalidDiceRoll(e2))) => Left(InvalidDiceRollTwice(e1, e2))
    }

  def generate: Either[ErrorCase, DiceRoll] =
    apply(Dice.randomlyGenerateDiceRoll)

  def convertInput(d1: Char, d2: Char): Either[ErrorCase, DiceRoll] = {
    def asInt(d: String): Either[ErrorCase, Int] =
      Try(d.toInt).fold[Either[ErrorCase, Int]](_ => Left(UnableToParseRoll(d)), success => Right(success))
    for {
      d1AsInt <- asInt(d1.toString)
      d2AsInt <- asInt(d2.toString)
      dice1 <- Dice.fromInt(d1AsInt)
      dice2 <- Dice.fromInt(d2AsInt)
    } yield DiceRoll(dice1.value -> dice2.value)
  }
}
