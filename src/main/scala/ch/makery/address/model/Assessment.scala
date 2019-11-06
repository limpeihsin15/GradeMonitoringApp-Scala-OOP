package ch.makery.address.model

import scalafx.beans.property.{IntegerProperty, StringProperty}

class Assessment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {
  val name = new StringProperty(nameS)
  val weightage = IntegerProperty(weightageI)
  val obtainedRaw = IntegerProperty(obtainedRawI)
  val totalRaw = IntegerProperty(totalRawI)

  def roundAt2(n: Double) = { val s = math pow (10, 2); (math round n * s) / s }
  def calcWeightage: Double = roundAt2(obtainedRaw.value.toFloat / totalRaw.value.toFloat * weightage.value.toFloat)
  override def toString: String = name.toString() + weightage.toString() + obtainedRaw.toString() + totalRaw.toString()
}