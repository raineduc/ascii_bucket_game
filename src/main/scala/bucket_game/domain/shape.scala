package bucket_game.domain

import bucket_game.domain.Collision.{CollisionFactory, buildCollision}
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

private[domain] class Boundary(center: Vect2, radius: Float) {
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
  val boundary = new Boundary(center, radius)

  override def calculateArea: Float = 2 * radius

}

object Shape {
  private def circleToCircleCollision(b1: CircleShape, b2: CircleShape): Option[CollisionFactory] = {
    val normal = b2.center - b1.center
    val squaredRadius = pow(b1.radius + b2.radius, 2)

    if (normal.lengthSquared > squaredRadius) return None

    val normalModule = normal.getModule

    if (normalModule != 0) {
      val penetration: Float = (squaredRadius - normalModule).toFloat
      Some(buildCollision(normal.normalize, penetration))
    }
    else Some(buildCollision(Vect2(1, 0), b1.radius))
  }

  private def circleToAABBCollision(aabb: AABBShape, circle: CircleShape): Option[CollisionFactory] = {
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

    Some(buildCollision(if (inside) (normal * -1).normalize else normal.normalize, penetration.toFloat))
  }

  private def AABBtoAABBCollision(s1: AABBShape, s2: AABBShape): Option[CollisionFactory] = {
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
        return Some(buildCollision(resultNormal, penetration.toFloat))
      }
    }
    None
  }

  private def bucketToShapeCollision(bucket: BucketShape, shape: Shape): Option[CollisionFactory] = {
    val leftBorderCollision = matchCollisionMethod(bucket.boundary.left, shape)
    val rightBorderCollision = matchCollisionMethod(bucket.boundary.right, shape)

    (leftBorderCollision.factory, rightBorderCollision.factory) match {
      case (Some(factory1), Some(factory2)) =>
        Some(buildCollision(factory1.normal + factory2.normal, max(factory1.penetration, factory2.penetration)))
      case (Some(factory1), None) => Some(factory1)
      case (None, Some(factory2)) => Some(factory2)
      case _ => None
    }
  }

  case class CollisionMatchResult(factory: Option[CollisionFactory], directOrder: Boolean = true)

  private def matchCollisionMethod(shape1: Shape, shape2: Shape): CollisionMatchResult = (shape1, shape2) match {
    case (s1: BucketShape, s2: Shape) => CollisionMatchResult(bucketToShapeCollision(s1, s2))
    case (s1: Shape, s2: BucketShape) => CollisionMatchResult(bucketToShapeCollision(s2, s1), directOrder = false)
    case (s1: CircleShape, s2: CircleShape) => CollisionMatchResult(circleToCircleCollision(s1, s2))
    case (s1: CircleShape, s2: AABBShape) => CollisionMatchResult(circleToAABBCollision(s2, s1), directOrder = false)
    case (s1: AABBShape, s2: CircleShape) => CollisionMatchResult(circleToAABBCollision(s1, s2))
    case (s1: AABBShape, s2: AABBShape) => CollisionMatchResult(AABBtoAABBCollision(s1, s2))
  }

  def defineCollision(body1: Body, body2: Body): Option[Collision] = {
    val collisionMatchResult = matchCollisionMethod(body1.shape, body2.shape)

    collisionMatchResult.factory match {
      case Some(collisionFactory) =>
        if (collisionMatchResult.directOrder) Some(collisionFactory(body1, body2))
        else Some(collisionFactory(body2, body1))
      case None => None
    }
  }
}