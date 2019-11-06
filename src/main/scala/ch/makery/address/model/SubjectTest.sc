
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

class Assessment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {
  val name = new StringProperty(nameS)
  val weightage = IntegerProperty(weightageI)
  val obtainedRaw = IntegerProperty(obtainedRawI)
  val totalRaw = IntegerProperty(totalRawI)

  def roundAt2(n: Double) = { val s = math pow (10, 2); (math round n * s) / s }
  def calcWeightage: Double = roundAt2(obtainedRaw.value.toFloat / totalRaw.value.toFloat * weightage.value.toFloat)
  override def toString: String = name.toString() + weightage.toString() + obtainedRaw.toString() + totalRaw.toString()
}


val subject = new Subject("CSC3024", "HCI");
subject.createAssessment("Final Exam", 50, 70, 100)
subject.createAssessment("Test", 15, 36, 48)
subject.createAssessment("Quiz", 10, 56, 67)
subject.createAssessment("Assignment", 25, 69, 80)
var test5 = subject.assessments
test5.length
test5(0).calcWeightage
val testing = for(i <- 0 until subject.assessments.length)yield{
  subject.assessments(i).calcWeightage
}


