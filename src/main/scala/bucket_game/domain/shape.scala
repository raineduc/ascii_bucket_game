package bucket_game.domain

import bucket_game.vecmath.Vect2

import scala.math.{pow, random}

sealed abstract class Shape(
                           val topLeft: Vect2,
                           val rightBottom: Vect2
                           )

class AABBShape(
                 override val topLeft: Vect2,
                 override val rightBottom: Vect2
               ) extends Shape(topLeft, rightBottom)

class CircleShape(
                 val center: Vect2,
                 val radius: Float
               ) extends Shape(
  center - Vect2(radius, radius),
  center + Vect2(radius, radius)
)


object Shape {
  private def ballToBallCollision(b1: CircleShape, b2: CircleShape): Boolean = {
    val sumOfRadii = b1.radius + b2.radius
    (pow(b1.center.x - b2.center.x, 2) + pow(b1.center.y - b2.center.y, 2)) < pow(sumOfRadii, 2)
  }

  private def ballToAABBCollision(ball: CircleShape, shape: AABBShape): Boolean = {
    false
  }

  private def AABBtoAABBCollision(s1: AABBShape, s2: AABBShape): Boolean = {
    if (s1.topLeft.x > s2.rightBottom.x || s1.rightBottom.x < s2.topLeft.x) false
    else if (s1.topLeft.y > s2.rightBottom.y || s1.rightBottom.y < s2.topLeft.y) false
    else true
  }


  def defineCollision(shape1: Shape, shape2: Shape): Boolean = (shape1, shape2) match {
    case (s1: CircleShape, s2: CircleShape) => ballToBallCollision(s1, s2)
    case (s1: CircleShape, s2: AABBShape) => ballToAABBCollision(s1, s2)
    case (s1: AABBShape, s2: CircleShape) => ballToAABBCollision(s2, s1)
    case (s1: AABBShape, s2: AABBShape) => AABBtoAABBCollision(s1, s2)
  }
}
