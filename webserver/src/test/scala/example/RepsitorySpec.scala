package example

import java.nio.file.Files

class RepositorySpec extends munit.FunSuite:
  test("create and read single note") {
    val directory = Files.createTempDirectory("scala3-example-project")
    val repository = Repository(directory)
    val title = "Hello world!"
    val content = "Nice to meet you."
    val note = repository.createNote(title, content)
    val notes = repository.getAllNotes()
    assertEquals(notes.size, 1)
    assertEquals(notes.head, Note(note.id, title, content))
  }

  test("create note twice") {
    val directory = Files.createTempDirectory("scala3-example-project")
    val repository = Repository(directory)
    val title = "Hello world!"
    val content = "Nice to meet you."
    repository.createNote(title, content)
    repository.createNote(title, content)
    val notes = repository.getAllNotes()
    assertEquals(notes.size, 2)
    assert(notes.forall(n => n.title == title && n.content == content))
  }
