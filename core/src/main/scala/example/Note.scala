package example

import io.circe.generic.semiauto.*
import io.circe.Codec

final case class Note(id: String, title: String, content: String)

object Note:
  given Codec[Note] = deriveCodec[Note]
