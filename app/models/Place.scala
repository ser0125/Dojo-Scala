package models

import com.sun.org.apache.xpath.internal.operations.Or
import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._

case class Place(id: Int, name: String)

object Place {

  implicit val placeCreate: Reads[Place] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String]
    ) (Place.apply _)

}
