package roulette

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import roulette.app.RouletteController
import roulette.infra.RouletteRepo

object App {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[App])
  }
}


@SpringBootApplication
class App extends games.App {
  register(classOf[RouletteController])
  bind(new RouletteRepo(mongo, writeConcern), classOf[RouletteRepo])

}

