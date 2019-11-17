
import ch.makery.address.model.Assessment
import scalafx.beans.property.{IntegerProperty, StringProperty}

import scala.collection.mutable.ListBuffer
import scalikejdbc._
val assessment = List(new Assessment(), new Assessment())
println(assessment.foreach(elem => elem.subCode))