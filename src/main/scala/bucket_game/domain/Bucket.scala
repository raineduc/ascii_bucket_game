package bucket_game.domain

import bucket_game.lib.vecmath.Vect2

class Bucket(val center: Vect2, val radius: Float) extends Body {
  val shape: BucketShape = new BucketShape(center, radius)
  val hitDetectionLayer: AABBShape = {
    val boundary = Boundary(center, radius)
    new AABBShape(boundary.left.topLeft, boundary.right.topLeft)
  }
  val density: Float = 1f
  val mass: Float = InfiniteMass
  val restitution: Float = 0.9f
  var velocity: Vect2 = Vect2(0, 0)
}
