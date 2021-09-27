package example

import cask.*
import io.circe.generic.auto.*
import com.typesafe.config.ConfigFactory
import java.nio.file.Paths

object WebServer extends MainRoutes:
  @route("/api/notes", methods=Seq("get", "post"))
  def api(req: Request) = 
    if req.exchange.getRequestMethod.equalToString("get") then
      encodeBody(repository.getAllNotes())
    else 
      val CreateNote(title, content) = parseBody[CreateNote](req)
      val newNote = repository.createNote(title, content)
      encodeBody(newNote)

  @staticResources("/index.html")
  def index() = "index.html"

  @staticResources("/assets")
  def assets() = "assets/"

  val config = ConfigFactory.load()
  override def host = config.getString("http.interface")
  override def port = config.getInt("http.port")

  val repository = Repository(Paths.get(config.getString("example.directory")))

  initialize()
