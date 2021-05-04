package example

import io.circe.{Decoder, Encoder, Printer}
import io.circe.parser.*
import io.circe.syntax.*

import cats.syntax.either.*

import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.http.scaladsl.marshalling.{ToEntityMarshaller, Marshaller}
import akka.http.scaladsl.model.{ContentTypes, MediaTypes}

trait CirceSupport:
  private val printer: Printer = Printer(
    dropNullValues = true,
    indent = ""
  )

  given [T: Decoder]: FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller
      .forContentTypes(ContentTypes.`application/json`)
      .map(decode(_).valueOr(throw _))

  given [T: Encoder]: ToEntityMarshaller[T] =
    Marshaller
      .stringMarshaller(MediaTypes.`application/json`)
      .compose(value => printer.print(value.asJson))

object CirceSupport extends CirceSupport
