package roulette.model

import games.model.{Money, SimpleWallet}
import org.junit.Assert._
import org.junit.Test

class RouletteTest {
  private val wallet = SimpleWallet()
  private val wager = Money(10)
  //noinspection ForwardReference
  private val nextPocket = () => winningPocket
  private val roulette = Roulette(nextPocket = nextPocket, wallet = wallet)
  private var winningPocket = Pocket(1)
  private var losingPocket = Pocket(20)

  @Test(expected = classOf[IllegalArgumentException]) def cannotBetOnZero(): Unit = {
    roulette.addBet(NumberBet(wager, Pocket(0)))
  }

  @Test(expected = classOf[IllegalArgumentException]) def cannotBetOnm37(): Unit = {
    roulette.addBet(NumberBet(wager, Pocket(37)))
  }

  @Test(expected = classOf[IllegalArgumentException]) def wagerAmountCannotBetNegative(): Unit = {
    roulette.addBet(NumberBet(Money(-1), winningPocket))
  }

  @Test def addingBetDecreasesBalance(): Unit = {
    roulette.addBet(winningBet())
    assertEquals(Money(990), wallet.getBalance)
  }

  @Test def addedBetIsAdded(): Unit = {
    val bet = NumberBet(wager, Pocket(1))

    assertEquals(List(bet), roulette.addBet(bet).bets)
  }

  @Test def spinClearsBets(): Unit = {
    assertEquals(List.empty, roulette.addBet(winningBet())
      .spin().bets)
  }

  @Test(expected = classOf[IllegalArgumentException]) def cannotSpinEmptyBets(): Unit = {
    roulette.spin()
  }

  @Test def spinEmptiesBet(): Unit = {
    assertEquals(List.empty, roulette.addBet(winningBet())
      .spin().bets)
  }

  private def winningBet() = NumberBet(wager, winningPocket)

  @Test def losingBetDoesNotPayout(): Unit = {
    roulette.addBet(losingBet())
      .spin()
    assertEquals(Money(990), wallet.getBalance)
  }

  private def losingBet() = NumberBet(wager, losingPocket)

  @Test def winningNumberBetPays36x() = {
    roulette.addBet(winningBet())
      .spin()
    assertEquals(Money(990 + 360), wallet.getBalance)
  }

  @Test def winningRedBetPaysEvens() = {
    roulette.addBet(RedBet(wager))
      .spin()
    assertEquals(Money(1010), wallet.getBalance)
  }

  @Test def losingRedBetPaysZero() = {
    winningPocket = Pocket(20)
    roulette.addBet(RedBet(wager))
      .spin()
    assertEquals(Money(990), wallet.getBalance)
  }

  @Test def winningBlackBetPaysEvens() = {
    winningPocket = Pocket(33)
    roulette.addBet(BlackBet(wager))
      .spin()
    assertEquals(Money(1010), wallet.getBalance)
  }

  @Test def losingBlackBetPaysZero() = {
    roulette.addBet(BlackBet(wager))
      .spin()
    assertEquals(Money(990), wallet.getBalance)
  }
}
