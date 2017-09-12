package controllers

import javax.inject._

import models.Place
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.mvc.Result._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
/*Controller to handle the actions*/
/*action: DefaultActionBuilder,
parse: PlayBodyParsers,
messagesApi: MessagesApi*/
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  //val places2: List[Place] = Place(1, "Robledo") :: Place(2, "Medellin") :: Place(3, "Barbosa") :: Nil

  var places = List(
    Place(1, "Robledo"),
    Place(2, "Medellín"),
    Place(3, "Barbosa")
  )


  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index = Action {
    Ok(views.html.index())
  }


  def listPlaces = Action {
    val json = Json.toJson(places)
    Ok(json)
  }

  def specificPlace (id:Int) = Action {

    val aux = places.filter(_.id == id)
    val json = Json.toJson(places)
    Ok(json)
  }

  def addPlace() = Action { implicit request =>
    val bodyAsJson = request.body.asJson.get
  

    bodyAsJson.validate[Place] match {
      case success: JsSuccess[Place] =>
        val place = success.get

        val message: Option[String] = {
          places.find(_.id == success.get.id) match{
            case Some(q) =>  Option("Ya existe el lugar que desea ingresar")
            case None => places = places :+ place
              Option("Ingreso exitoso")
          }
        }
        
        Ok(Json.toJson(
          Map("message" -> message)
        ))
      case JsError(error) => BadRequest(Json.toJson(
        Map("error" -> "Bad Parameters", "description" -> "Missing a parameter")
      ))
    }
  }

  def removePlace(id:Int) = Action {
    places = places.filterNot(_.id == id)
    Ok(Json.toJson(
      Map("message" -> "Borrado exitoso")
    ))
  }

  def updatePlace() = Action { implicit request =>
    val bodyAsJson = request.body.asJson.get

    bodyAsJson.validate[Place] match {
      case success: JsSuccess[Place] =>
        var newPlace = Place(success.get.id, success.get.name)
        places = places.map(x => if (x.id == success.get.id) newPlace else x)
        Ok(Json.toJson(
          Map("message" -> "Actualizacion exitosa")
        ))
      case e:JsError => BadRequest(Json.toJson(
        Map("error" -> "No se pudo actualizar", "description" -> "Bad parameters")))
    }
  }


}




