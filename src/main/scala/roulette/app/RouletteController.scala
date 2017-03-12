package roulette.app

import java.net.URI
import javax.inject.Inject
import javax.ws.rs._
import javax.ws.rs.core.Response

import _root_.games.model.Money
import games.infra.HttpWallet
import roulette.infra.RouletteRepo
import roulette.model._

import scala.beans.BeanProperty

case class NumberBetRequest(@BeanProperty amount:Money, @BeanProperty number:Int)
case class BetRequest(@BeanProperty amount:Money)

@Path("/games/roulette")
class RouletteController @Inject() (repo: RouletteRepo) {

  // TOOD - remove
  @DELETE
  def delete(@HeaderParam("PlayerId") playerId: String): Response = {
    repo.delete(playerId)
    Response.noContent().build
  }

  @GET
  def get(@HeaderParam("PlayerId") playerId: String, @HeaderParam("Wallet") uri: URI): Response = {
    val roulette = repo.get(playerId, getWallet(uri))
    Response.ok(Map(
      "pocket" -> roulette.pocket.number,
      "bets" -> roulette.bets.map {
        case NumberBet(amount, pocket) => Map("type" -> "number", "number" -> pocket.number, "amount" -> amount)
        case RedBet(amount) => Map("type" -> "red", "amount" -> amount)
        case BlackBet(amount) => Map("type" -> "black", "amount" -> amount)
        case _ => throw new AssertionError()
      }
    )).build()
  }

  private def getWallet(uri: URI) = new HttpWallet(uri, "roulette", "roulette")

  @POST
  @Path("/bets/number")
  def addNumbersBet(@HeaderParam("PlayerId") playerId: String, @HeaderParam("Wallet") uri: URI, numberBet: NumberBetRequest): Response =
    addBet(playerId, uri, NumberBet(numberBet.amount, Pocket(numberBet.number)))

  private def addBet(playerId: String, uri: URI, bet: Bet) :Response= {
    val wallet = getWallet(uri)
    repo.set(playerId, repo.get(playerId, wallet).addBet(bet))
    Response.created(URI.create(".")).entity(Map("balance" -> wallet.getBalance)).build()
  }

  @POST
  @Path("/bets/red")
  def addRedBet(@HeaderParam("PlayerId") playerId: String, @HeaderParam("Wallet") uri: URI, bet: BetRequest): Response =
    addBet(playerId, uri, RedBet(bet.amount))

  @POST
  @Path("/bets/black")
  def addBlackBet(@HeaderParam("PlayerId") playerId: String, @HeaderParam("Wallet") uri: URI, bet: BetRequest): Response =
    addBet(playerId, uri, BlackBet(bet.amount))

  @POST
  @Path("/spins")
  def spin(@HeaderParam("PlayerId") playerId: String, @HeaderParam("Wallet") uri: URI): Response = {
    val wallet = getWallet(uri)
    val roulette = repo.get(playerId, wallet).spin()
    repo.set(playerId, roulette)
    Response.created(URI.create(".")).entity(Map(
      "balance" -> wallet.getBalance,
      "pocket" -> roulette.pocket.number
    )).build()
  }
}
