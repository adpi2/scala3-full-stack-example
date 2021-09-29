package example

import java.nio.file.Files
import scala.concurrent.ExecutionContext

class RepositorySpec extends munit.FunSuite:
  given ExecutionContext = ExecutionContext.global

  test("create and read single note") {
    val directory = Files.createTempDirectory("scala3-example-project")
    val repository = Repository(directory)
    val title = "Hello world!"
    val content = "Nice to meet you."
    for
      note <- repository.createNote(title, content)
      notes <- repository.getAllNotes()
    yield
      assertEquals(notes.size, 1)
      assertEquals(notes.head, Note(note.id, title, content))
  }

  test("create note twice") {
    val directory = Files.createTempDirectory("scala3-example-project")
    val repository = Repository(directory)
    val title = "Hello world!"
    val content = "Nice to meet you."
    for
      _ <- repository.createNote(title, content)
      _ <- repository.createNote(title, content)
      notes <- repository.getAllNotes()
    yield
      assertEquals(notes.size, 2)
      assert(notes.forall(n => n.title == title && n.content == content))
  }
