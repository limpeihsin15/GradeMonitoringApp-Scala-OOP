package ch.makery.address.model
import ch.makery.address.model.Assessment
import ch.makery.address.util.Calculate
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.collections.ObservableBuffer

import scala.collection.mutable.ListBuffer

class Subject ( subCodeS : String, subNameS : String, creditS: Integer) {
  val subCode = new StringProperty(subCodeS)
  val subName = new StringProperty(subNameS)
  val assessments = new ObservableBuffer[Assessment]()
  var expectedmarks = IntegerProperty(70) //subject expected total marks
  val credit = IntegerProperty(creditS)
/*
  assessments += new Assessment("assessment1", 15, 85, 100)// default
  assessments += new Assessment("assessment2", 15, 70, 100)// default
  assessments += new Assessment("Final Exam", 50, 70, 100)
  assessments += new Assessment("MidTerm", 20, 70, 100)
*/
  assessments += new FinalExam()
  assessments += new Assignment()
  assessments += new Test()
  assessments += new Quiz()

  var currentmarks = Calculate.calcTotalAssessments(this)
  var difference = expectedmarks - currentmarks
/*
  def calcTotalAssessmentMarks(_assessments: ObservableBuffer[Assessment]): Double = {
    var totalmarks = 0.0
    for (i <- assessments) {
      totalmarks += i.obtainedWeightage.value.toFloat
    }
    totalmarks
  }*/
}