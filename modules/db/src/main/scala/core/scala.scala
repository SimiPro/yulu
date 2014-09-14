package core


import play.api.libs.json.{JsNumber, JsValue, Writes, Format}
import play.api.mvc.{PathBindable, QueryStringBindable}

import scala.slick.driver.H2Driver.simple._
import scala.slick.model.Model

/**
 * Created by simipro on 9/14/14.
 */


case class Id[T](id: Long) {
  def toString = id.toString
}

object Id {
  //slick MappedColumnType for any Id[M]

  //Model[M] ?
  implicit def idMapper[M <: Model] = MappedColumnType.base[Id[M], Long](_.id, Id[M])

  //JSON formatter
  def format[T]: Format[Id[T]] =
    Format(__.read[Long].map(Id(_)), new Writes[Id[T]] {
      def writes(o: Id[T]) = JsNumber(o.id)
    })


  // Binders for Play query string and paths
  implicit def queryStringBinder[T](implicit longBinder: QueryStringBindable[Long]) = new QueryStringBindable[Id[T]] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Id[T]]] = {
      longBinder.bind(key, params) map {
        case Right(id) => Right(Id(id))
        case _ => Left("Unable to bind an Id")
      }
    }

    override def unbind(key: String, id: Id[T]): String = {
      longBinder.unbind(key, id.id)
    }
  }

  implicit def pathBinder[T](implicit longBinder: PathBindable[Long]) = new PathBindable[Id[T]] {
    override def bind(key: String, value: String): Either[String, Id[T]] =
      longBinder.bind(key, value) match {
        case Right(id) => Right(Id(id))
        case _ => Left("Unable to bind an Id")
      }
    override def unbind(key: String, id: Id[T]): String = id.id.toString
  }
}