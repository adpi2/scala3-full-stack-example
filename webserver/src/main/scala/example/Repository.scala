package example

import scala.concurrent.{Future, ExecutionContext}
import scala.jdk.CollectionConverters.*

import java.nio.file.{Path, Paths, Files, StandardOpenOption}
import java.util.UUID

import cats.syntax.either._

import io.circe.syntax.*
import io.circe.parser.decode
import io.circe.Printer

trait Repository extends NoteService

object Repository:
  private val printer: Printer = Printer(
    dropNullValues = true,
    indent = ""
  )

  def apply(directory: Path)(using ExecutionContext): Repository =
    if !Files.exists(directory) then Files.createDirectory(directory)
    new FileRepository(directory)

  private class FileRepository(directory: Path)(using ExecutionContext)
      extends Repository:
    def getAllNotes(): Future[Seq[Note]] = Future {
      val files = Files.list(directory).iterator.asScala
      files
        .filter(_.toString.endsWith(".json"))
        .map { file =>
          val bytes = Files.readAllBytes(file)
          decode[Note](new String(bytes)).valueOr(throw _)
        }
        .toSeq
    }

    def createNote(title: String, content: String): Future[Note] = Future {
      val id = UUID.randomUUID().toString
      val note = Note(id, title, content)
      val file = directory.resolve(s"$id.json")
      val bytes = printer.print(note.asJson).getBytes
      Files.write(file, bytes, StandardOpenOption.CREATE)
      note
    }
