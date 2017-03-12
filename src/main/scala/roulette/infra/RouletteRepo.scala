package roulette.infra

import com.mongodb.{MongoClient, WriteConcern}
import games.infra.GameFactory
import games.model.Wallet
import games.infra.GameRepo
import roulette.model.Roulette.randomPocket
import roulette.model.Roulette


private object RouletteFactory extends GameFactory[Roulette, State] {
  override def toMaybeState(game: Roulette): Option[State] = {
    Some(State(game.pocket, game.bets))
  }

  override def toGame(wallet: Wallet, maybeState: Option[State]): Roulette = {
    maybeState
      .map { state => new Roulette(randomPocket, wallet, state.pocket, state.bets) }
      .getOrElse(new Roulette(randomPocket, wallet))
  }
}

class RouletteRepo(mongo: MongoClient, writeConcern: WriteConcern) extends GameRepo(mongo, "roulette", RouletteFactory, writeConcern)
