package example

import upickle.default.*

class JsonParsingSpec extends munit.FunSuite:
  test("parse Note") {
    val note = Note("1234", "Hello, world!", "Nice to meet you")
    val json = write(note)
    assertEquals(read[Note](json), note)
  }
