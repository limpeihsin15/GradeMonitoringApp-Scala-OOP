package ch.makery.address.model

import java.time.LocalDate

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty};

class Person ( firstNameS : String, lastNameS : String ){
  var firstName  = new StringProperty(firstNameS)
  var lastName   = new StringProperty(lastNameS) 
	var street     = new StringProperty("some Street")
	var postalCode = IntegerProperty(1234)
	var city       = new StringProperty("some city")
	var date       = ObjectProperty[LocalDate](LocalDate.of(1999, 2, 21))

}


