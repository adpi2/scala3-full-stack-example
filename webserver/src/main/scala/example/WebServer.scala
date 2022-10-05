package example

import java.nio.file.Paths

import cask.*

object WebServer extends MainRoutes:
  private val repository = Repository(Paths.get("./target/data"))

  initialize()
  println(s"Server online at http://$host:$port/")

  @cask.get("/")
  def base() =
    StaticResource(
      "index.html",
      getClass.getClassLoader,
      Seq("Content-Type" -> "text/html")
    )

  @cask.get("/assets/:fileName", subpath = true)
  def getAsset(fileName: String) =
    val contentType =
      if fileName.endsWith(".js") then Some("text/javascript")
      else if fileName.endsWith(".js.map") then Some("application/json")
      else if fileName.endsWith(".html") then Some("text/html")
      else if fileName.endsWith(".css") then Some("text/css")
      else None
    val headers = contentType.map("Content-Type" -> _).toSeq
    StaticResource(s"assets/$fileName", getClass.getClassLoader, headers)

  @cask.getJson("api/notes")
  def getAllNotes(): Seq[Note] = repository.getAllNotes()

  @cask.postJson("api/notes")
  def createNote(title: String, content: String): Note =
    repository.createNote(title, content)
