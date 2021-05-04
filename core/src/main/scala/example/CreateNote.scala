package example

import io.circe.generic.semiauto.*
import io.circe.Codec

final case class CreateNote(title: String, content: String)

object CreateNote:
  given Codec[CreateNote] = deriveCodec[CreateNote]
