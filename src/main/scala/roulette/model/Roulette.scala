package roulette.model

import java.security.SecureRandom

import games.model.Wallet

import scala.beans.BeanProperty




object Roulette {
  val randomPocket: () => Pocket = () => Pocket(new SecureRandom().nextInt(37))
}

case class Roulette(nextPocket: () => Pocket, wallet: Wallet, @BeanProperty pocket: Pocket = Pocket(0), @BeanProperty bets: List[Bet] = List()) {
  def addBet(bet: Bet): Roulette = {
    wallet.wager(bet.amount)
    copy(bets = bet :: bets)
  }

  def spin(): Roulette = {
    require(bets.nonEmpty, "no bets on table")
    val roulette = copy(pocket = nextPocket(), bets = List())
    bets.map {
      _.payout(roulette.pocket)
    }
      .foreach {
        wallet.payout
      }
    roulette
  }
}
