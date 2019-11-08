package ch.makery.address.model
import ch.makery.address.model.Assessment
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.collections.ObservableBuffer

import scala.collection.mutable.ListBuffer

class Subject ( subCodeS : String, subNameS : String) {
  val subCode = new StringProperty(subCodeS)
  val subName = new StringProperty(subNameS)
  val assessments = new ObservableBuffer[Assessment]()

  assessments += new Assessment("assessment1",50,0,0)
  assessments += new Assessment("assessment2",50,0,0)

  def createAssessment(name:String, weightage: Int, obtainedRaw: Int, totalRaw: Int){
    assessments += new Assessment(name, weightage, obtainedRaw, totalRaw)
  }
}