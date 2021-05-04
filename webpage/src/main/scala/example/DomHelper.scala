package example

import org.scalajs.dom.document
import org.scalajs.dom.html.*

object DomHelper:
  def div(children: Element*): Div =
    val elem = document.createElement("div")
    for child <- children do elem.appendChild(child)
    elem.asInstanceOf[Div]

  def h1(textContent: String): Heading =
    val elem = document.createElement("h1")
    elem.textContent = textContent
    elem.asInstanceOf[Heading]

  def h2(textContent: String): Heading =
    val elem = document.createElement("h2")
    elem.textContent = textContent
    elem.asInstanceOf[Heading]

  def p(textContent: String): Paragraph =
    val elem = document.createElement("p")
    elem.textContent = textContent
    elem.asInstanceOf[Paragraph]

  def input(): Input =
    val elem = document.createElement("input")
    elem.asInstanceOf[Input]

  def textarea(): TextArea =
    val elem = document.createElement("textarea")
    elem.asInstanceOf[TextArea]

  def button(textContent: String): Button =
    val elem = document.createElement("button")
    elem.textContent = textContent
    elem.asInstanceOf[Button]
