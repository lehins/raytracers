package raytracer

import Raytracer.{Pos, Sphere, Objs, Camera}

final case class Scene(camLookFrom: Pos, camLookAt: Pos, camFov: Float, spheres: List[Sphere]) {
  def toObjsCam(width: Int, height: Int): (Objs, Camera) =
    (BVH(_.aabb, spheres), Camera(camLookFrom, camLookAt, Vec3(0,1,0), camFov, width.toFloat / height.toFloat))
}

object Scene {

  def fromString(name: String): Option[Scene] = name match {
    case "irreg" => Some(irreg)
    case "rgbbox" => Some(rgbbox)
    case _ => None
  }

  def rgbbox: Scene = {
    val n = 10
    val k = 60f
    val leftwall = for {
      y <- 0 to n-1
      z <- 0 to n-1
    } yield 
    Sphere( Vec3( -k/2f
                , -k/2f + k/n * y
                , -k/2f + k/n * z)
          , Vec3(1,0,0)
          , k/(n * 2f)
          )

    val midwall = for {
      x <- 0 to n-1
      y <- 0 to n-1
    } yield 
    Sphere( Vec3( -k/2f + k/n * x
                , -k/2f + k/n * y
                , -k/2f)
          , Vec3(0,1,0)
          , k/(n * 2f)
          )
    val rightwall = for {
      y <- 0 to n-1
      z <- 0 to n-1
    } yield 
    Sphere( Vec3( k/2f
                , -k/2f + k/n * y
                , -k/2f + k/n * z)
          , Vec3(0,0,1)
          , k/(n * 2f)
          )

    val bottom = for {
      x <- 0 to n-1
      z <- 0 to n-1
    } yield
    Sphere( Vec3( -k/2f + k/n * x
                , -k/2f
                , -k/2f + k/n * z)
          , Vec3(1,1,1)
          , k/(n*2f)
          )

    Scene( Vec3(0, 30, 30)
         , Vec3(0, -1, -1)
         , 75
         , (leftwall ++ midwall ++ rightwall ++ bottom).toList
         )
  }

  def irreg: Scene = {
    val n = 100
    val k = 600f
    val bottom = for {
      x <- 0 to n-1
      z <- 0 to n-1
    } yield
    Sphere( Vec3( -k/2f + k/n * x
                , 0
                , -k/2f + k/n * z)
          , Vec3(1,1,1)
          , k/(n*2.0f)
          )

    Scene(Vec3(0, 12, 30), Vec3(0, 10, -1), 75, bottom.toList)
  }
}

