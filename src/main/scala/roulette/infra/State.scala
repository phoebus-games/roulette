package roulette.infra


import roulette.model.{Bet, Pocket}

import scala.beans.BeanProperty

case class State(@BeanProperty pocket: Pocket, @BeanProperty bets: List[Bet])
