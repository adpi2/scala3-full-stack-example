package example

import scala.quoted.*

object Macros:
  inline def showType[T](exp: T): String = ${ showType('exp) }
  
  def showType(exp: Expr[Any])(using Quotes): Expr[String] =
    import quotes.reflect.*
    Expr(exp.asTerm.tpe.widen.show)
