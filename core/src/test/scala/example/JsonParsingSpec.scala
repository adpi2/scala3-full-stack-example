package example

import io.circe.parser
import io.circe.syntax.*
import io.circe.Printer

class JsonParsingSpec extends munit.FunSuite:
  private val printer: Printer = Printer(
    dropNullValues = true,
    indent = ""
  )

  test("parse Note") {
    val note = Note("1234", "Hello, world!", "Nice to meet you")
    val json = printer.print(note.asJson)
    assertEquals(parser.decode[Note](json), Right(note))
  }

  test("parse CreateNote") {
    val createNote = CreateNote("Hello, world!", "Nice to meet you")
    val json = printer.print(createNote.asJson)
    assertEquals(parser.decode[CreateNote](json), Right(createNote))
  }
