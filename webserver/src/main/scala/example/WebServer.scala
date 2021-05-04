package example

import akka.actor.ActorSystem
import akka.http.scaladsl.*
import com.typesafe.config.ConfigFactory

import java.nio.file.Paths

import scala.concurrent.ExecutionContext

object WebServer extends server.Directives with CirceSupport:
  @main def start =
    given system: ActorSystem = ActorSystem("webserver")
    given ExecutionContext = system.dispatcher

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")
    val directory = Paths.get(config.getString("example.directory"))

    val repository = Repository(directory)
    Http()
      .newServerAt(interface, port)
      .bindFlow(base ~ assets ~ api(repository))
    println(s"Server online at http://$interface:$port/")

  private val base: server.Route =
    pathSingleSlash (
      getFromResource("index.html")
    )

  private val assets: server.Route =
    path("assets" / Remaining) { file =>
      getFromResource("assets/" + file)
    }

  private def api(repository: Repository): server.Route =
    path("api" / "notes")(
      get (
        complete(repository.getAllNotes())
       ) ~
        post (
          entity(as[CreateNote]) { request =>
            complete(repository.createNote(request.title, request.content))
          }
        )
    )
