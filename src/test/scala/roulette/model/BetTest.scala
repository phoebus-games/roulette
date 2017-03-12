package roulette.model

import games.model.Money
import org.junit.Assert.assertEquals
import org.junit.Test

class BetTest {

  @Test(expected = classOf[IllegalArgumentException])
  def zeroBetAmount(): Unit = BlackBet(Money(0))

  @Test(expected = classOf[IllegalArgumentException])
  def zeroNumber(): Unit = NumberBet(Money(1), Pocket(0))

  @Test
  def numberBetLose(): Unit = assertEquals(Money(0), NumberBet(Money(1), Pocket(1)).payout(Pocket(0)))

  @Test
  def numberBetWin(): Unit = assertEquals(Money(36), NumberBet(Money(1), Pocket(1)).payout(Pocket(1)))

  @Test
  def blackBetLose(): Unit = assertEquals(Money(0), BlackBet(Money(1)).payout(Pocket(0)))

  @Test
  def blackBetWin(): Unit = assertEquals(Money(2), BlackBet(Money(1)).payout(Pocket(2)))

  @Test
  def redBetLose(): Unit = assertEquals(Money(0), RedBet(Money(1)).payout(Pocket(0)))

  @Test
  def redBetWin(): Unit = assertEquals(Money(2), RedBet(Money(1)).payout(Pocket(1)))
}
