import scalafx.beans.property.{IntegerProperty, StringProperty}

import scala.collection.mutable.ListBuffer

class Subject ( subCodeS : String, subNameS : String) {
  var subCode = new StringProperty(subCodeS)
  var subName = new StringProperty(subNameS)
  var assessments:ListBuffer[Assessment] = ListBuffer()

  def addAssessment(assessment: Assessment) {
    assessments.append(assessment)
  }
}


class Assessment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {
  val name = new StringProperty(nameS)
  val weightage = IntegerProperty(weightageI)
  val obtainedRaw = IntegerProperty(obtainedRawI)
  val totalRaw = IntegerProperty(totalRawI)

  def calcWeightage: Double = obtainedRaw.value.toFloat / totalRaw.value.toFloat * weightage.value.toFloat
  override def toString: String = name.toString() + weightage.toString() + obtainedRaw.toString() + totalRaw.toString()
}

new Assessment("Final", 50, 70, 100)
val subject = new Subject("CSC3024", "HCI")
subject.addAssessment(new Assessment("Final", 50, 70, 100))
subject.assessments.length
subject.assessments(0) name()