package bucket_game.lib

import scala.math.{cos, signum, sin}

package object vecmath {
  // zero angle vector
  private val referenceVector = Vect2(1, 0)

  /*
  * 2d vector rotation for right-handed coordinate system
  * with positive counterclockwise rotation
  * */
  def rotateVect2(vect: Vect2, deg: Float): Vect2 = {
    Vect2(
      vect.x * cos(deg) - vect.y * sin(deg),
      vect.x * sin(deg) + vect.y * cos(deg)
    )
  }

  // from 0 to 2pi
  def polarAngle(vec: Vect2): Float = {
    val angleBetween = vec.angleBetween(referenceVector).toFloat
    if (signum(vec.y) >= 0) angleBetween else (2 * math.Pi - angleBetween).toFloat
  }
}
