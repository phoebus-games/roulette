package roulette

import roulette.app.RouletteController
import roulette.infra.RouletteRepo

object App {
  def main(args: String): Unit = {
    new App().run()
  }
}

class App extends games.App {
  register(classOf[RouletteController])
  bind(new RouletteRepo(mongo, writeConcern), classOf[RouletteRepo])

}

