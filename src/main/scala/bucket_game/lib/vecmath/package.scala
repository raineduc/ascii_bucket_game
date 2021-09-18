package bucket_game.lib

import scala.math.{cos, sin}

package object vecmath {

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
}
