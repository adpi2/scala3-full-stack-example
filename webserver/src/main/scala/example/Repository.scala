package example

import cats.syntax.either._
import io.circe.Printer
import io.circe.parser.decode
import io.circe.syntax.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.UUID
import scala.jdk.CollectionConverters.*

trait Repository extends NoteService[[A] =>> A]

object Repository:
  private val printer: Printer = Printer(
    dropNullValues = true,
    indent = ""
  )

  def apply(directory: Path): Repository =
    if !Files.exists(directory) then Files.createDirectory(directory)
    new FileRepository(directory)

  private class FileRepository(directory: Path)
      extends Repository:
    def getAllNotes(): Seq[Note] = 
      val files = Files.list(directory).iterator.asScala
      files
        .filter(_.toString.endsWith(".json"))
        .map { file =>
          val bytes = Files.readAllBytes(file)
          decode[Note](new String(bytes)).valueOr(throw _)
        }
        .toSeq

    def createNote(title: String, content: String): Note =
      val id = UUID.randomUUID().toString
      val note = Note(id, title, content)
      val file = directory.resolve(s"$id.json")
      val bytes = printer.print(note.asJson).getBytes
      Files.write(file, bytes, StandardOpenOption.CREATE)
      note
