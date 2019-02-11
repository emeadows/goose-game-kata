# goose-game-kata

This has been based on [xpeppers Game of Goose](https://github.com/xpeppers/goose-game-kata)

I have written this code in as functional way as possible.  This means apart from the ramdom number generation all state is held in the Main class.

To run the game use `sbt run`.  

## Dice 
This has been implement using a sealed abstract class to ensure apply and copy methods will throw a compiler error.
This code is not completely functional due to `Random` library being used to generate numbers.

The tests for this could have used ScalaCheck for property testing but for a single use the additional libary dependency seemed overkill

## DiceRoll
This is a complementary object to `Dice` which allows users to generate 2 dice rolls at once, either using the dice random generation or by entering numbers as 'char'.

## Player
Again a sealed abstract class to prevent apply method, this ensures Players always start at 0. 
The companion object provides mechanisms for adding, updating and moving players.  

## Event
Event is a sealed trait and the following case classes have extended this.
    Moved
    Victory
    Bridge
    Goose
    OffBoard
    Prank
As the message given to the user depends on the event there is a `def message(name: String): List[String]` which is overridden in each instance.  This allows the messages to be constructed in the right way without concern of how the event was generated.  There is also a `def finalPosition: Int`, due to `Goose` this may not be immediately obvious and so a helper method was added.

## GameEvent
This is the actual game logic choosing the event required based on the players and the move.

## GooseGame 
This is where the Event messages are calculated and unpacked to include the roll values in the final response.

## Constants
Ideally you would pull this out in to a file and use Typesafe Config.  Again overkill for this kata.

## ErrorCase 
Another sealed trait to allow issues to be caught without throwing errors.
 
## Additional
Currently the game will only support 2 players, this could be changed but I chose to leave this.
 