package bucket_game.lib.vecmath

import scala.math.{acos, pow, sqrt}

final case class Vect2(x: Double, y: Double) {

  def this() = this(0, 0)

  def lengthSquared: Double = pow(x, 2) + pow(y, 2)

  def getModule: Double = sqrt(lengthSquared)

  def normalize: Vect2 = this * (1 / getModule)

  def isZero: Boolean = x == 0 && y == 0

  def +(that: Vect2): Vect2 = {
    Vect2(x + that.x, y + that.y)
  }

  def -(that: Vect2): Vect2 = {
    Vect2(x - that.x, y - that.y)
  }

  def *(coef: Double): Vect2 = Vect2(x * coef, y * coef)

  def dotProduct(that: Vect2): Double = {
    if (isZero && that.isZero) 0
    else x * that.x + y * that.y
  }

  def angleBetween(that: Vect2): Double = {
    acos(dotProduct(that) / getModule / that.getModule)
  }
}
