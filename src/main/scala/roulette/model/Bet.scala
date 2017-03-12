package roulette.model

import games.model.Money

import scala.beans.BeanProperty


sealed trait Bet {
  @BeanProperty val amount: Money

  require(amount > 0, "amount must be greater than zero")

  def payout(pocket: Pocket): Money = amount * (payoutMultiplier(pocket) match {
    case 0 => 0
    case m => m + 1
  })

  protected def payoutMultiplier(pocket: Pocket): Int
}

case class NumberBet(@BeanProperty amount: Money, pocket: Pocket) extends Bet {
  require(pocket.number > 0)

  override protected def payoutMultiplier(pocket: Pocket): Int = if (pocket == this.pocket) 35 else 0
}

case class RedBet(@BeanProperty amount: Money) extends Bet {
  override protected def payoutMultiplier(pocket: Pocket): Int = if (pocket.isRed) 1 else 0
}

case class BlackBet(@BeanProperty amount: Money) extends Bet {
  override protected def payoutMultiplier(pocket: Pocket): Int = if (pocket.isBlack) 1 else 0
}
