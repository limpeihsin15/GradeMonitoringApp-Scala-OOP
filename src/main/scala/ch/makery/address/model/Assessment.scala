package ch.makery.address.model

import scalafx.beans.property.{DoubleProperty, IntegerProperty, StringProperty}

class Assessment(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {
  val name = new StringProperty(nameS)
  val weightage = IntegerProperty(weightageI)
  val obtainedRaw = IntegerProperty(obtainedRawI)
  val totalRaw = IntegerProperty(totalRawI)


  def roundAt2(n: Double) = { val s = math pow (10, 2); (math round n * s) / s }
  val obtainedWeightage = StringProperty(roundAt2(obtainedRaw.value.toFloat / totalRaw.value.toFloat * weightage.value.toFloat).toString)
  def calcWeightage(_obtainedRaw:IntegerProperty, _totalRaw:IntegerProperty ,_weightage:IntegerProperty) = StringProperty(roundAt2(_obtainedRaw.value.toFloat / _totalRaw.value.toFloat * _weightage.value.toFloat).toString)

  override def toString: String = name.toString() + weightage.toString() + obtainedRaw.toString() + totalRaw.toString()

}