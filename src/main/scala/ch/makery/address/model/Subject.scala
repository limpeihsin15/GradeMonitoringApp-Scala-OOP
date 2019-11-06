package ch.makery.address.model
import ch.makery.address.model.Assessment
import scalafx.beans.property.{IntegerProperty, StringProperty}

import scala.collection.mutable.ListBuffer

class Subject ( subCodeS : String, subNameS : String) {
  var subCode = new StringProperty(subCodeS)
  var subName = new StringProperty(subNameS)
  var assessments:ListBuffer[Assessment] = ListBuffer()

  def createAssessment(name:String, weightage: Int, obtainedRaw: Int, totalRaw: Int) {
    assessments.append(new Assessment(name, weightage, obtainedRaw, totalRaw))
  }
}