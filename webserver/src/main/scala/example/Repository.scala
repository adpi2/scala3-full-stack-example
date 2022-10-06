package example

import scala.concurrent.{Future, ExecutionContext}
import scala.jdk.CollectionConverters.*

import java.nio.file.{Path, Files, StandardOpenOption}
import java.util.UUID

import upickle.default.*

trait Repository:
  def getAllNotes(): Seq[Note]
  def saveNote(note: Note): Unit
  def deleteNote(id: String): Boolean

  def createNote(title: String, content: String): Note =
    val id = UUID.randomUUID().toString
    val note = Note(id, title, content)
    saveNote(note)
    note

object Repository:
  def apply(directory: Path): Repository =
    if !Files.exists(directory) then Files.createDirectory(directory)
    new FileRepository(directory)

  private class FileRepository(directory: Path) extends Repository:
    def getAllNotes(): Seq[Note] =
      files.map { file =>
        val bytes = Files.readAllBytes(file)
        read[Note](bytes)
      }

    def saveNote(note: Note): Unit =
      val file = directory.resolve(s"${note.id}.json")
      val bytes = write(note).getBytes
      Files.write(file, bytes, StandardOpenOption.CREATE)

    def deleteNote(id: String): Boolean =
      val file = directory.resolve(s"$id.json")
      if Files.exists(file) then
        Files.delete(file)
        true
      else false

    private def files: Seq[Path] = Files.list(directory).iterator.asScala.toSeq
