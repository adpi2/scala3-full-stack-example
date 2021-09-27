package example

import cask.Request
import cask.model.Response
import io.circe.{Decoder, Encoder}
import io.circe.jawn.CirceSupportParser
import java.io.OutputStream

def parseBody[T: Decoder](req: Request): T =
  CirceSupportParser.parseFromByteArray(req.bytes)
    .flatMap(_.as[T].toTry)
    .get

def encodeBody[T](t: T)(using encoder: Encoder[T]): Response[Response.Data] =
  val data = new Response.Data:
    def headers = Seq("Content-Type" -> "application/json")
    def write(out: OutputStream) =
      val bytes = encoder(t).noSpaces.getBytes
      out.write(bytes)

  Response(data)
