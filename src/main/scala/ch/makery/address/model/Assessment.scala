package ch.makery.address.model

import ch.makery.address.util.Calculate
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

class Assessment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {
  val name = new StringProperty(nameS)
  val weightage = ObjectProperty(weightageI)
  val obtainedRaw = IntegerProperty(obtainedRawI)
  val totalRaw = IntegerProperty(totalRawI)
  val obtainedWeightage = calcWeightage(obtainedRaw, totalRaw, weightage)
  def this()=this("Assessment",0, 0, 0)
  def calcWeightage(_obtainedRaw:IntegerProperty, _totalRaw:IntegerProperty ,_weightage:ObjectProperty[Int]) = StringProperty(Calculate.roundAt2(_obtainedRaw.value.toFloat / _totalRaw.value.toFloat * _weightage.value.toFloat).toString)
  def test():StringProperty = name
  override def toString: String = name.toString() + weightage.toString() + obtainedRaw.toString() + totalRaw.toString()
}

class FinalExam(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) extends Assessment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int){

  def this() = this("Final Exam", 50, 0, 100)
}
class Coursework(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) extends Assessment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int)

class Assignment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) extends Coursework(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {

  def this() = this("Assignment", 20, 0, 100)
}
class Test(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) extends Coursework(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {

  def this() = this("Test", 20, 0, 100)
}
class Quiz(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) extends Coursework(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {

  def this() = this("Quiz", 10, 0, 100)
}


