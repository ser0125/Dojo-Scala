package controllers

import javax.inject._

import models.Place
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  var place1 = Place(1, "UdeA")
  var places = List(
    Place(1, "Robledo"),
    Place(2, "Medellín"),
    Place(3, "Barbosa")
  )

  implicit val placeJson = Json.writes[Place]


  def addPlace() = Action { implicit request =>
    val bodyAsJson = request.body.asJson.get

    bodyAsJson.validate[Place] match {
      case success: JsSuccess[Place] => {
        val placeName = success.get.name
        var place = success.get
        places = places :+ place
        Ok("El lugar "+ placeName + " se ingresó exitosamente")
      }
      case JsError(error) => BadRequest("No se pudo ingresar el lugar!")
    }
  }

  def listPlaces = Action {
    val json = Json.toJson(places)
    Ok(json)
  }

  def removePlace(id:Int) = Action{
    places = places.filterNot(_.id == id)
    Ok("Eliminado correctamente")
  }

  def updatePlace() = Action { implicit request =>
    val bodyAsJson = request.body.asJson.get

    bodyAsJson.validate[Place] match {
      case success: JsSuccess[Place] => {
        val placeName = success.get.name
        Ok("El lugar "+ placeName + " se actualizó exitosamente")
      }
      case JsError(error) => BadRequest("No se pudo actualizar el lugar!" + error)
    }
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

}




