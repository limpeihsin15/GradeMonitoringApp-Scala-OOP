package ch.makery.address.model

import java.time.LocalDate

import ch.makery.address.util.Calculate
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer

class Student( firstNameS : String, lastNameS : String , studentIdi:Int,programmeS:String, intakeS: String, subjectS:ObservableBuffer[Subject]) extends Person ( firstNameS : String, lastNameS : String ){

  var studentId = IntegerProperty(studentIdi)
  var programme = StringProperty(programmeS)
  var intake =  StringProperty(intakeS)
  val subject = subjectS
  var cgpa = 0.0


}

