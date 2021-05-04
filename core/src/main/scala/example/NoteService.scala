package example

import scala.concurrent.Future

trait NoteService:
  def getAllNotes(): Future[Seq[Note]]
  def createNote(title: String, content: String): Future[Note]
