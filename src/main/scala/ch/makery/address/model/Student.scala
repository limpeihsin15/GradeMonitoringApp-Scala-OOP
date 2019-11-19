package ch.makery.address.model

import java.time.LocalDate

import ch.makery.address.MainApp
import ch.makery.address.util.{Calculate, Database}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Alert
import scalikejdbc._
import scala.util.control.Breaks._

import scala.util.{Failure, Success, Try}

class Student( firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String, intakeS: String, cgpaD:Double, subjectS:ObservableBuffer[Subject])
  extends Person(firstNameS : String, lastNameS : String)
  with Database{

  def this() = this (null, null, 0, null, null, 0.0, null) //auxiliary constructor
  def this(firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String,  intakeS: String, subjectS:ObservableBuffer[Subject]) = this(firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String, intakeS: String, 0.0, subjectS:ObservableBuffer[Subject])
  def this(firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String,  intakeS: String, cgpaD:Double) = this (firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String, intakeS: String, cgpaD:Double, null)
  var studentId = IntegerProperty(studentIdi)
  var programme = StringProperty(programmeS)
  var intake =  StringProperty(intakeS)
  //if(subjectS != null){subjectS.foreach(elem => elem.studentId = studentId)}
  var subject:ObservableBuffer[Subject] = subjectS
  var cgpa:Double = cgpaD



  //save Student with assessment and subject
  override
  def save() : Try[Int] = {

    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>

        sql"""
					    insert into student (firstName, lastName, studentId, programme, intake, cgpa) values
						  (${firstName.value}, ${lastName.value},${studentId.value}, ${programme.value}, ${intake.value}, $cgpa)
				      """.update.apply()


      })
    }//end of if statement

    else {
      Try(DB autoCommit { implicit session =>

        sql"""
				update student
				set
        firstName   = ${firstName.value},
        lastName    = ${lastName.value},
				studentId   = ${studentId.value} ,
				programme   = ${programme.value},
				intake      = ${intake.value},
				cgpa        = $cgpa
				 where firstName = ${firstName.value} and lastName = ${lastName.value}
				""".update.apply()

      }
      )
    }//end of else statement
  }// end of save function


  //delete student
  override
  def delete() : Try[Int] = {

    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
				delete from student where
					firstName = ${firstName.value} and lastName = ${lastName.value} and studentId = ${studentId.value}
				""".update.apply()
      })
    }
    else
      throw new Exception("Student not Exists in Database")
  }


  //check the existence of student
  override
  def isExist : Boolean =  {
    DB readOnly { implicit session =>

      sql"""
				select * from student where
				firstName = ${firstName.value} and lastName = ${lastName.value}
			""".map(rs => rs.string("firstName")).single.apply()

    } match {
      case Some(x) => true
      case None => false
    }

  }

}

// create an object of Student and define apply, initializeTable function
// that's mean Student can be treat as a function
object Student extends Database{
  def apply (
             firstNameS : String,
             lastNameS : String ,
             studentIdi:Int,
             programmeS:String,
             intakeS: String,
             cgpaD:Double,
             subjectS:ObservableBuffer[Subject]
            ) : Student = {

    new Student( firstNameS, lastNameS, studentIdi, programmeS, intakeS, cgpaD, subjectS)

  }
  //to get one student out of all students in the database based on lastname and studentId
  def getStudent(lastNameS : String ,
                 studentIdi:Int, students: List[Student]) : Student ={
    var temp = new Student()
    for(student <- students){
      if (student.lastName.value == lastNameS && student.studentId.value == studentIdi){
        temp = student
      }
    }
    temp
  }
  //returns a list of all student objects in the database
  def getAllStudents : List[Student] = {
    DB readOnly { implicit session =>
      sql"select * from student".map(rs => Student(rs.string("firstName"),
        rs.string("lastName"), rs.int("studentId"),
        rs.string("programme"),rs.string("intake"),
        rs.double("cgpa"), getStudentSubject(rs.int("studentId"), nestedSubject))).list.apply()
    }
  }
  //to clean the subject list for one student
  def cleanSubject(_studentIdi: Int, _subjectS: List[Subject]): ObservableBuffer[Subject] = {
    var temp = new ObservableBuffer[Subject]()
    for (i <- _subjectS.indices) {
      if (_subjectS(i).studentId.value == _studentIdi && !temp.contains(_subjectS(i))) {
        temp += _subjectS(i)
      }
    }
    temp
  }

  //create a nested subject list categorizing subjects taken by each students in database by calling cleanSubject
  def nestedSubject: List[ObservableBuffer[Subject]] = {
    DB readOnly { implicit session =>
      sql"""select studentId from student""".map(rs => cleanSubject(rs.int("studentId"), Subject.getAllSubjects)).list.apply()
    }
  }

  //get the subject list for a specific student using studentId, parsing in nestedSubject, to be used in apply method in getAllStudents
  def getStudentSubject(studentId: Int, _nestedSubject: List[ObservableBuffer[Subject]]): ObservableBuffer[Subject] = {
    var temp = new ObservableBuffer[Subject]()
    breakable {
      for (list <- _nestedSubject) {
        for (subject <- list) {
          if (subject.studentId.value == studentId && !temp.contains(subject)) {
            temp ++= list
            break
          }
        }
      }
    }
    temp
  }

  //create 3 tables
  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
			create table student (
			  id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
			  firstName varchar(64),
			  lastName varchar(64),
			  studentId int,
			  programme varchar(100),
			  intake varchar(100),
        cgpa double
			  )""".execute.apply()

        sql"""
      create table subject (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        studentId int,
        subCode varchar(10),
        subName varchar(64),
        credit int,
        expectedmarks int
        )""".execute.apply()

      sql"""
      create table assessment (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        subCode varchar(10),
        name varchar(64),
        weightage int,
        obtainedRaw int,
        totalRaw  int
        )""".execute.apply()

    }
  }


}