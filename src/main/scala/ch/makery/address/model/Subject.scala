package ch.makery.address.model
import ch.makery.address.model.Assessment
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.collections.ObservableBuffer

import scala.collection.mutable.ListBuffer

class Subject ( subCodeS : String, subNameS : String) {
  val subCode = new StringProperty(subCodeS)
  val subName = new StringProperty(subNameS)
  val assessments = new ObservableBuffer[Assessment]()
  var expectedmarks = IntegerProperty(70) //subject expected total marks

  assessments += new Assessment("assessment1", 20, 85, 100)
  assessments += new Assessment("assessment2", 30, 70, 100)

  var currentmarks = calcTotalAssessmentMarks(assessments)
  var difference = expectedmarks - currentmarks

  def calcTotalAssessmentMarks(_assessments: ObservableBuffer[Assessment]): Double = {
    var totalmarks = 0.0
    for (i <- assessments) {
      totalmarks += i.obtainedWeightage.value.toFloat
    }
    totalmarks
  }
}