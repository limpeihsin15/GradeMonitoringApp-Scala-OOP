package ch.makery.address.util

import ch.makery.address.model.{Assessment, Student, Subject}
import scalafx.collections.ObservableBuffer

object Calculate {
  def roundAt2(n: Double) = {
    val s = math pow(10, 2);
    (math round n * s) / s
  }

  def calcTotalAssessments(subject:Subject): Double = {
    subject.currentmarks = 0.0
    if(subject.assessments != null) {
      for (i <- subject.assessments) {

        subject.currentmarks += i.obtainedWeightage.value.toFloat
      }

      subject.currentmarks = roundAt2(subject.currentmarks)

    }
    subject.currentmarks
  }
  //##
  def calcCGPA(student: Student): Double = {
    var _accumulatedmarks = 0.0
    var totalcredit = 0
    if(student.subject != null){
      for (i <- student.subject) {
        Calculate.calcTotalAssessments(i)
        _accumulatedmarks += i.currentmarks *i.credit.value
        //println(_accumulatedmarks)
        totalcredit += i.credit.value
        //println(totalcredit)
      }
      student.cgpa = roundAt2(_accumulatedmarks / totalcredit)
    }
    student.cgpa
  }
}