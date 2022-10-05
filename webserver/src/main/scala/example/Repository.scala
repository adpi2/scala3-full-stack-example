package example

import scala.concurrent.{Future, ExecutionContext}
import scala.jdk.CollectionConverters.*

import java.nio.file.{Path, Files, StandardOpenOption}
import java.util.UUID

import upickle.default.*

trait Repository:
  def getAllNotes(): Seq[Note]
  def createNote(title: String, content: String): Note
  def deleteNote(id: String): Boolean

object Repository:
  def apply(directory: Path): Repository =
    if !Files.exists(directory) then Files.createDirectory(directory)
    new FileRepository(directory)

  private class FileRepository(directory: Path) extends Repository:
    def getAllNotes(): Seq[Note] =
      val files = Files.list(directory).iterator.asScala
      files
        .filter(_.toString.endsWith(".json"))
        .map { file =>
          val bytes = Files.readAllBytes(file)
          read[Note](bytes)
        }
        .toSeq

    def createNote(title: String, content: String): Note =
      val id = UUID.randomUUID().toString
      val note = Note(id, title, content)
      val file = directory.resolve(s"$id.json")
      val bytes = write(note).getBytes
      Files.write(file, bytes, StandardOpenOption.CREATE)
      note

    def deleteNote(id: String): Boolean =
      val file = directory.resolve(s"$id.json")
      if Files.exists(file) then
        Files.delete(file)
        true
      else false
