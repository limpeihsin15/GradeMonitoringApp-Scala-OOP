package ch.makery.address.model

import java.time.LocalDate
import ch.makery.address.util.{Calculate, Database}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalikejdbc._
import scala.util.Try

class Student( firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String, intakeS: String, cgpaD:Double, subjectS:ObservableBuffer[Subject])
  extends Person(firstNameS : String, lastNameS : String)
  with Database{

  def this() = this (null, null, 0, null, null, 0.0, null) //auxiliary constructor
  def this(firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String,  intakeS: String, subjectS:ObservableBuffer[Subject]) = this(firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String, intakeS: String, 0.0, subjectS:ObservableBuffer[Subject])
  def this(firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String,  intakeS: String, cgpaD:Double) = this (firstNameS : String, lastNameS : String , studentIdi:Int, programmeS:String, intakeS: String, cgpaD:Double, null)
  var studentId = IntegerProperty(studentIdi)
  var programme = StringProperty(programmeS)
  var intake =  StringProperty(intakeS)
  if(subjectS != null){subjectS.foreach(elem => elem.studentId = studentId)}
  var subject = subjectS
 var cgpa = cgpaD



  //save Student with assessment and subject
  override
  def save() : Try[Int] = {

    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>

        sql"""
					    insert into student (firstName, lastName, studentId, programme, intake, cgpa) values
						  (${firstName.value}, ${lastName.value}${studentId.value}, ${programme.value}, ${intake.value}, ${cgpa})
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
				cgpa        = ${cgpa}
				 where firstName = ${firstName.value} and lastName = ${lastName.value}
				""".update.apply()

      }
      )
    }//end of else statement
  }// end of save function

  /*
  def saveSubject() : Try[Int] = {

    saveAssessment()
    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>

        for(i <- subject) {
          sql"""
                insert into student-subject (studentId, subCode, expectedmarks)
                values (${studentId.value}, ${i.subCode.value}, ${i.expectedmarks.value})

                insert into subject (subCode, subName, credit)
                values (${i.subCode}, ${i.subName}, ${i.credit})
                """.update.apply()
        }


        sql"""

				      """.update.apply()

      })
    }
    else {
      Try(DB autoCommit { implicit session =>
        for (i <- subject) {
          sql"""
              update student-subject
              set
              studentId = ${studentId.value},
              subCode = ${i.subCode.value},
              expectedmarks = ${i.expectedmarks.value}
              where studentId = ${studentId.value} and subCode = ${i.subCode.value}

              update subject
               set
               subCode = ${i.subCode.value},
               subName = ${i.subName.value},
               credit = ${i.credit.value}
               where subCode = ${i.subCode.value}
              """.update.apply()}


        sql"""

				""".update.apply()
      }
      )
    }
  }*/

  /*def saveAssessment() : Try[Int] = {
    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>


        for(i <- subject) {
          for(a <- i.assessments) {
            sql"""
                insert into student-subject-assessment (studentid, subCode, assessmentName, rawObtained)
                values (${studentId.value}, ${i.subCode.value}, ${a.name.value}, ${a.obtainedRaw.value})

                insert into assessment (subCode, name, weightage, totalRaw)
                values (${i.subCode}, ${a.name}, ${a.weightage}, ${a.totalRaw})
                """.update.apply()}
        }

        sql"""

				      """.update.apply()

      })
    }
    else {
      Try(DB autoCommit { implicit session =>

        for(i <- subject) {
          for(a <- i.assessments) {
            sql"""
              update student-subject-assessment
              set
              studentId = ${studentId.value},
              subCode = ${i.subCode.value},
              assessmentName = ${a.name.value},
              rawObtained = ${a.obtainedRaw.value}
              where studentId = ${studentId.value} and subCode = ${i.subCode.value} and assessmentName = ${a.name.value}

              update assessment
              set
              subCode = ${i.subCode.value},
              assessmentName = ${a.name.value},
              weightage = ${a.weightage.value},
              totalRaw = ${a.totalRaw.value}
              where subCode = ${i.subCode.value} and assessmentName = ${a.name.value}
              """.update.apply()}
        }

        sql"""

				""".update.apply()
      }
      )
    }
  }*/

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

  /*
  //delete subject
  def deleteSubject():Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session => {
        deleteAssessment()
        for (i <- subject) {
          sql"""
              delete from student-subject
              where studentId = ${studentId.value} and subCode = ${i.subCode.value}

              delete from subject
              where subCode = ${i.subCode.value}
              """.update.apply()
        }//end of for loop

        sql"""
              """.update.apply()
      }
      }
      )//end of Try
    }
    else
      throw new Exception("Subject not Exists in Database")
  }*/

  /*//delete assessment
  def deleteAssessment():Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>

        for(i <- subject) {
          for(a <- i.assessments) {
            sql"""
              delete from student-subject-assessment
              where studentId = ${studentId.value} and subCode = ${i.subCode.value} and assessmentName = ${a.name.value}

               delete from assessment
               where subCode = ${i.subCode.value} and name = ${a.name.value}
              """.update.apply()}
        }

        sql"""
              """.update.apply()
      })
    }
    else
      throw new Exception("Assessment not Exists in Database")
  }*/

  //check the existence of student
  override
  def isExist : Boolean =  {
    DB readOnly { implicit session =>

      //check subject from student-subject table
      /*for (i <- subject) {
        sql"""
              select * from student_subject
              where studentId = ${studentId.value} and subCode = ${i.subCode.value}
              """.map(rs => rs.string("studentId")).single.apply()
      } match {
        case Some(x) => true
        case None => false
      }*/

      sql"""
				select * from student where
				firstName = ${firstName.value} and lastName = ${lastName.value}
			""".map(rs => rs.string("firstName")).single.apply()

    } match {
      case Some(x) => true
      case None => false
    }

  }

  /*
  //check existence of subject
  def isSubjectExist : Boolean =  {
    DB readOnly { implicit session =>

      //check subject from subject table
      for (i <- subject) {
        sql"""
              select * from student-subject
              where studentId = ${studentId.value} and subCode = ${i.subCode.value}
              """.map(rs => rs.string("studentId")).single.apply()
      } match {
        case Some(x) => true
        case None => false
      }

      //check subject from subject table
      for (i <- subject) {
        sql"""
              select * from subject
              where subCode = ${i.subCode.value}
              """.map(rs => rs.string("subCode")).single.apply()
      } match {
        case Some(x) => true
        case None => false
      }

      //check the existence of student
      sql"""
				select * from student where
				firstName = ${firstName.value} and lastName = ${lastName.value}
			""".map(rs => rs.string("firstName")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }

  }*/

  /*//check existence of assessment
  def isAssessmentExist : Boolean =  {
    DB readOnly { implicit session =>

      //check the existence of subject
      isSubjectExist

      //check the existence of assessment from student-subject-assessment table
      for(i <- subject) {
        for(a <- i.assessments) {
          sql"""
              select * from student-subject-assessment
              where studentId = ${studentId.value} and subCode = ${i.subCode.value} and assessmentName = ${a.name.value}
              """.map(rs => rs.string("studentId")).single.apply()
        }match {
          case Some(x) => true
          case None => false
        }
      }

      /*check the existence of student
      *if student doesn't exist, subject and assessment also won't exist
      */
        sql"""
				    select * from student where
				    firstName = ${firstName.value} and lastName = ${lastName.value}
			      """.map(rs => rs.string("firstName")).single.apply()
        } match {
          case Some(x) => true
          case None => false
    }

  }*/
}

// create an object of Student and define apply, initializeTable function
// that's mean Student can be treat as a function
object Student extends Database{
  def apply (
              firstNameS : String,
              lastNameS : String,
              studentIdi : Integer,
              programmeS : String,
              intakeS : String,
              cgpaD : Double
            ) : Student = {

    new Student(firstNameS, lastNameS, studentIdi, programmeS, intakeS, cgpaD){
      cgpa = cgpaD
    }

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
        cgpa double,
        PRIMARY KEY(studentId)
			  )""".execute.apply()

        sql"""
      create table subject (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        studentId int,
        subCode varchar(10),
        subName varchar(64),
        credit int,
        expectedmarks int,
        PRIMARY KEY(subCode)
        )""".execute.apply()

      sql"""
      create table assessment (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        subCode varchar(10),
        name varchar(64) unique,
        weightage int,
        obtainedRaw int,
        totalRaw  int,
        PRIMARY KEY (subCode, name)
        )""".execute.apply()
      /*
      sql"""
      create table student_subject (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        studentId int references student (studentId),
        subCode varchar(10) references subject(subCode),
        expectedmarks int,
        PRIMARY KEY(studentId, subCode)
        )""".execute.apply()}}

      def initializeTable1() = {
        DB autoCommit { implicit session =>
      sql"""
      create table student_subject_assessment (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        studentId int references student(studentId),
        subCode varchar(10) references subject(subCode),
        assessmentName varchar(64) references assessment(name),
        rawObtained int,
        PRIMARY KEY(id)
        )
        """.execute.apply()*/
    }
  }

  def getAllStudents : List[Student] = {
    DB readOnly { implicit session =>
      sql"select * from student".map(rs => Student(rs.string("firstName"),
        rs.string("lastName"), rs.int("studentId"),
        rs.string("programme"),rs.string("intake"),
        rs.double("cgpa"))).list.apply()
    }
  }
  def insertSubjectIntoStudents(students: List[Student]) : List[Student] = {
    val _subjects = new ObservableBuffer[Subject]()
    for (student <- students){
      for (subject <- _subjects){
        if(subject.studentId == student.studentId) {
          _subjects += subject
        }
      }
      student.subject = _subjects
    }
    students
  }
}


/*
object Assessment extends Database {
  def apply(nameS: String,
            weightageI: Int,
            obtainedRawI: Int,
            totalRawI: Int,
            obtainedWeightageS: String
           ): Assessment = {

    new Assessment(nameS, weightageI, obtainedRawI, totalRawI) {
      obtainedWeightage.value = obtainedWeightageS
    }
  }

  def getAllAssessments : List[Assessment] = {
    DB readOnly( { implicit session =>
      sql"select * from assessment".map(rs => Assessment(rs.string("subCode"), rs.string("name"), rs.int("weightage"), rs.int("totalRaw")))
    })
  }
}*/

/*
object Subject extends Database{
  def apply(
             subCodeS : String,
             subNameS : String,
             assessmentsOB: Assessment,
             expectedmarksI:Int,
             creditI:Int
           ) : Subject = {
    new Subject(subCodeS, subNameS, assessmentsOB, expectedmarksI, creditI)
  }

  def getAllSubjects : List[Subject] = {
    DB readOnly { implicit session =>
      sql"select * from subject".map(rs => Subject(rs.string("subCode"),
        rs.string("subName"))).list.apply()
    }
  }
}*/