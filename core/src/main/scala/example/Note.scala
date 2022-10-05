package example

import upickle.default.*

final case class Note(id: String, title: String, content: String)

object Note:
  given ReadWriter[Note] = macroRW
