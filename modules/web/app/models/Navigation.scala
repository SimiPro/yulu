package models.web

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Navigation(page: String, menus: Seq[NavigationMenu])
case class NavigationMenu(items: Seq[NavigationItem], position:String, dropDown: Boolean=false)
case class NavigationItem(display: String, route: String, css: String = "")

object Navigation {
  implicit val navigationItemReads = Json.reads[NavigationItem]
  implicit val navigationItemWrites: Writes[NavigationItem] =  (
    (__ \ "display").write[String] and
      ( __ \ "route").write[String] and
      (__ \ "css").write[String]
    )(unlift(NavigationItem.unapply))

  implicit val navigationMenuWrites: Writes[NavigationMenu] = (
    (__ \ "items").lazyWrite(Writes.traversableWrites[NavigationItem](navigationItemWrites)) and
      (__ \ "position").write[String] and
      (__ \ "dropDown").write[Boolean]
    )(unlift(NavigationMenu.unapply))

  implicit val navigationWrites: Writes[Navigation] = (
  (__ \ "page").write[String] and
    (__ \ "menus").lazyWrite(Writes.traversableWrites[NavigationMenu](navigationMenuWrites))
  )(unlift(Navigation.unapply))

  def toJson(navigation: Navigation): String = {
    Json.stringify(Json.toJson(navigation))
  }
}