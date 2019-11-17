import ch.makery.address.MainApp.subjectData
import ch.makery.address.model.{Student, Subject}
import ch.makery.address.util.Calculate
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.collections.ObservableBuffer

import scala.collection.mutable.ListBuffer

object Calculate {
  def roundAt2(n: Double) = {
    val s = math pow(10, 2);
    (math round n * s) / s
  }

  def calcTotalAssessments(subject:Subject): Double = {
    subject.currentmarks = 0.0
    for (i <- subject.assessments) {

      subject.currentmarks += i.obtainedWeightage.value.toFloat
    }

    subject.currentmarks = roundAt2(subject.currentmarks)
    subject.currentmarks
  }
  //##
  def calcCGPA(student: Student): Unit = {
    var _accumulatedmarks = 0.0
    var totalcredit = 0
    for (i <- student.subject) {

      _accumulatedmarks += i.currentmarks * i.credit.toInt
      totalcredit += i.credit.toInt
    }
    student.cgpa = roundAt2(_accumulatedmarks / totalcredit)
    //student.cgpa
  }
  val subjectData = new ObservableBuffer[Subject]()
  subjectData += new Subject("PRG2104", "Object-Oriented Programming")
  subjectData += new Subject("SEG2202", "Software Engineering")
  subjectData += new Subject("CSC3024", "Human Computer Interaction")
  val chun = new Student("chun","tan", 123,"soit","august",subjectData)
  calcCGPA(chun)
  println(chun.cgpa)
  var _accumulatedmarks = 0.0
  var totalcredit = 0
  chun.subject
  for (i <- chun.subject) yield {
    Calculate.calcTotalAssessments(i)
    _accumulatedmarks += i.currentmarks *i.credit.value
    println(_accumulatedmarks)
    totalcredit += i.credit.value
    println(totalcredit)
  }
  _accumulatedmarks/totalcredit

}