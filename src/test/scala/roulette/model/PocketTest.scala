package roulette.model

import org.junit.Assert._
import org.junit.Test

class PocketTest {

  private val redNumbers = List(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36)
  private val blackNumbers = List(2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35)

  @Test def pocketCanBeZero(): Unit = Pocket(0)

  @Test def pocket0IsNotRed(): Unit = assertFalse(Pocket(0).isRed)

  @Test def pocket0IsNotBlack(): Unit = assertFalse(Pocket(0).isBlack)

  @Test(expected = classOf[IllegalArgumentException]) def pocketCannotBeNegative(): Unit = Pocket(-1)

  @Test(expected = classOf[IllegalArgumentException]) def pocketCannotBe37(): Unit = Pocket(37)

  @Test def redPocketsAreRed(): Unit =
    assertTrue(redNumbers.forall {
      Pocket(_).isRed
    })

  @Test def redPocketsAreNotBlack(): Unit =
    assertTrue(redNumbers.forall {
      !Pocket(_).isBlack
    })

  @Test def blackPocketsAreBlack(): Unit =
    assertTrue(blackNumbers.forall {
      Pocket(_).isBlack
    })

  @Test def blackPocketsAreNotRed(): Unit =
    assertTrue(blackNumbers.forall {
      !Pocket(_).isRed
    })

}
