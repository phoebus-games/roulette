package roulette

import scala.beans.BeanProperty

package object model {

  class Pocket(@BeanProperty val number: Int) extends AnyVal {

    def isBlack: Boolean = number > 0 && !isRed

    def isRed: Boolean = List(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36).contains(number)

    // def isColumn(col: Int): Boolean = {
    //  Map(
    //    1 -> List(1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34),
    //   2 -> List(2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35),
    //   3 -> List(3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36)
    //  )
    //}.get(col).contains(number)
  }

  object Pocket {
    val MAX: Int = 36

    def apply(number: Int): Pocket = {
      require(number >= 0)
      require(number <= MAX)
      new Pocket(number)
    }
  }

}
