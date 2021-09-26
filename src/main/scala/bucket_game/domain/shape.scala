package bucket_game.domain

import bucket_game.domain.Collision.{CollisionFactory}
import bucket_game.lib.vecmath.Vect2

import scala.math.{Pi, abs, max, min, pow, signum}

sealed abstract class Shape(
                           var topLeft: Vect2,
                           var rightBottom: Vect2
                           ) {
  protected[domain] def changePosition(pos: Vect2): Unit = {
    val delta = pos - topLeft
    topLeft = pos
    rightBottom += delta
  }
  def calculateArea: Float

  def calculateMass(density: Float): Float = calculateArea * density
}

class AABBShape(topLeft: Vect2, rightBottom: Vect2) extends Shape(topLeft, rightBottom) {
  def centerPos: Vect2 = topLeft + (rightBottom - topLeft) * 0.5

  def width: Double = rightBottom.x - topLeft.x
  def height: Double = topLeft.y - rightBottom.y
  def calculateArea: Float = (width * height).toFloat
}

class CircleShape(
                 var center: Vect2,
                 var radius: Float
               ) extends Shape(
  center + Vect2(-radius, radius),
  center + Vect2(radius, -radius)
) {
  override protected[domain] def changePosition(pos: Vect2): Unit = {
    super.changePosition(pos)
    center = pos + Vect2(radius, -radius)
  }

  def calculateArea: Float = (Pi * pow(radius, 2)).toFloat
}

private[domain] case class Boundary(center: Vect2, radius: Float) {
  private val left_vector = Vect2(center.x - radius, center.y)
  private val right_vector = Vect2(center.x + radius, center.y)
  val left = new AABBShape(left_vector, left_vector)
  val right = new AABBShape(right_vector, right_vector)
}

class BucketShape(
                  var center: Vect2,
                  var radius: Float
                 ) extends Shape(
  center + Vect2(-radius, 0),
  center + Vect2(radius, 0)
) {
  val boundary: Boundary = Boundary(center, radius)

  override def calculateArea: Float = 2 * radius

}

object Shape {
  private def circleToCircleCollision(b1: CircleShape, b2: CircleShape): Option[Collision] = {
    val normal = b2.center - b1.center
    val squaredRadius = pow(b1.radius + b2.radius, 2)

    if (normal.lengthSquared > squaredRadius) return None

    val normalModule = normal.getModule

    if (normalModule != 0) {
      val penetration: Float = (squaredRadius - normalModule).toFloat

      normal.normalize match {
        case Some(n) => Some(Collision(b1, b2, n, penetration))
        case _ => None
      }
    }
    else Some(Collision(b1, b2, Vect2(1, 0), b1.radius))
  }

  private def circleToAABBCollision(aabb: AABBShape, circle: CircleShape): Option[Collision] = {
    val centerVector = circle.center - aabb.centerPos

    var closestX = centerVector.x
    var closestY = centerVector.y

    val aabbHalfWidth = aabb.width / 2
    val aabbHalfHeight = aabb.height / 2

    closestX = min(aabbHalfWidth, max(closestX, -aabbHalfWidth))
    closestY = min(aabbHalfHeight, max(closestY, -aabbHalfHeight))

    var inside = false

    if (centerVector == Vect2(closestX, closestY)) { // circle inside AABB
      inside = true

      if (abs(centerVector.x) > abs(centerVector.y)) {
        closestX = if (closestX > 0) aabbHalfWidth else -aabbHalfWidth
      } else {
        closestY = if (closestY > 0) aabbHalfHeight else -aabbHalfHeight
      }
    }

    val normal = centerVector - Vect2(closestX, closestY)
    val distanceSquared = normal.lengthSquared

    if (distanceSquared > circle.radius * circle.radius && !inside) return None

    val penetration = circle.radius - distanceSquared

    val normalized = normal.normalize

    normalized match {
      case Some(n) => Some(Collision(aabb, circle, if (inside) n * -1 else n, penetration.toFloat))
      case _ => None
    }
  }

  private def AABBtoAABBCollision(s1: AABBShape, s2: AABBShape): Option[Collision] = {
    val normal = s2.centerPos - s1.centerPos

    val s1HalfWidth = s1.width / 2
    val s2HalfWidth = s2.width / 2

    val xOverlap = s1HalfWidth + s2HalfWidth - abs(normal.x)

    if (xOverlap > 0) {
      val s1HalfHeight = s1.height / 2
      val s2HalfHeight = s2.height / 2

      val yOverlap = s1HalfHeight + s2HalfHeight - abs(normal.y)

      if (yOverlap > 0) {
        val (resultNormal, penetration) =
          if (xOverlap > yOverlap) (Vect2(signum(normal.x), 0), xOverlap)
          else (Vect2(0, signum(normal.y)), yOverlap)
        return Some(Collision(s1, s2, resultNormal, penetration.toFloat))
      }
    }
    None
  }

  private def bucketToShapeCollision(bucket: BucketShape, shape: Shape): Option[Collision] = {
    val leftBorderCollision = matchCollisionMethod(bucket.boundary.left, shape)
    val rightBorderCollision = matchCollisionMethod(bucket.boundary.right, shape)

    (leftBorderCollision, rightBorderCollision) match {
      case (Some(col1), Some(col2)) =>
        Some(Collision(bucket, shape, col1.normal + col2.normal, max(col1.penetration, col2.penetration)))
      case (Some(col1), None) => Some(col1)
      case (None, Some(col2)) => Some(col2)
      case _ => None
    }
  }

  case class CollisionMatchResult(factory: Option[CollisionFactory], directOrder: Boolean = true)

  private def matchCollisionMethod(shape1: Shape, shape2: Shape): Option[Collision] = (shape1, shape2) match {
    case (s1: BucketShape, s2: Shape) => bucketToShapeCollision(s1, s2)
    case (s1: Shape, s2: BucketShape) => bucketToShapeCollision(s2, s1) match {
      case Some(col) => Some(Collision.reverseShapeOrder(col))
      case _ => None
    }
    case (s1: CircleShape, s2: CircleShape) => circleToCircleCollision(s1, s2)
    case (s1: CircleShape, s2: AABBShape) => circleToAABBCollision(s2, s1) match {
      case Some(col) => Some(Collision.reverseShapeOrder(col))
      case _ => None
    }
    case (s1: AABBShape, s2: CircleShape) => circleToAABBCollision(s1, s2)
    case (s1: AABBShape, s2: AABBShape) => AABBtoAABBCollision(s1, s2)
  }

  def defineCollision(shape1: Shape, shape2: Shape): Option[Collision] = {
    matchCollisionMethod(shape1, shape2)
  }
}