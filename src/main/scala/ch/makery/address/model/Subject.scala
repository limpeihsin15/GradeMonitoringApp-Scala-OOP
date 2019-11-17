package ch.makery.address.model
import ch.makery.address.model.Assessment
import ch.makery.address.util.{Calculate, Database}
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalikejdbc._

import scala.collection.TraversableOnce
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}


class Subject (studentIdi: Int, subCodeS : String, subNameS : String, assessmentsOB: ObservableBuffer[Assessment], expectedmarksI:Int, creditI:Int) extends Database {
  def this() = this(0, null, null, null, 0, 0)
  def this(studentIdi: Int, subCodeS : String, subNameS : String, expectedmarksI:Int, creditI:Int) = this(0, subCodeS, subNameS, null, expectedmarksI, creditI)
  def this(subCodeS : String, subNameS : String ) = {
    this(0, subCodeS : String, subNameS : String, new ObservableBuffer[Assessment](), 70, 4)
    assessments += new FinalExam(subCodeS)
    assessments += new Assignment(subCodeS)
    assessments += new Test(subCodeS)
    assessments += new Quiz(subCodeS)
    for (a <-assessments){
      a.saveAssessment()
    }
  }

  var studentId = IntegerProperty(studentIdi)
  val subCode = new StringProperty(subCodeS)
  val subName = new StringProperty(subNameS)
  var expectedmarks = IntegerProperty(expectedmarksI) //subject expected total marks
  var credit = IntegerProperty(creditI)
  var assessments = assessmentsOB


  var currentmarks = 0.0
  var difference = expectedmarks - currentmarks


  def saveSubject() : Try[Int] = {

    if (!(isSubjectExist)) {
      Try(DB autoCommit { implicit session =>
        sql"""

                insert into subject (studentId, subCode, subName, expectedmarks, credit)
                values (${studentId.value}, ${subCode.value}, ${subName.value}, ${expectedmarks.value},${credit.value})
                """.update.apply()
      })
    }

    else {
      Try(DB autoCommit { implicit session =>

          sql"""
              update subject
               set
               studentId      = ${studentId.value}
               subCode        = ${subCode.value},
               subName        = ${subName.value},
               expectedmarks  = ${expectedmarks.value},
               credit         = ${credit.value}
               where subCode  = ${subCode.value}
              """.update.apply()}


      )//end of Try
    }//end of else
  }//end of saveSubject


  //delete subject
  def deleteSubject():Try[Int] = {
    if (isSubjectExist) {
      Try(DB autoCommit { implicit session => {

        sql"""
              delete from subject
              where subCode = ${subCode.value}
              """.update.apply()

        }

      }
      )//end of Try
    }
    else
      throw new Exception("Subject not Exists in Database")
  }

  //check existence of subject
  def isSubjectExist : Boolean =  {
    DB readOnly { implicit session =>

      //check subject from subject table

        sql"""
              select * from subject
              where subCode = ${subCode.value}
              """.map(rs => rs.string("subCode")).single.apply()
      } match {
        case Some(x) => true
        case None => false
      }

      /*//check the existence of student
      sql"""
				select * from student where
				firstName = ${firstName.value} and lastName = ${lastName.value}
			""".map(rs => rs.string("firstName")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }*/

  }}


  object Subject extends Database{
    def apply(studentIdi: Int,
               subCodeS : String,
               subNameS : String,
               expectedmarksI:Int,
               creditI:Int
             ) : Subject = {
      new Subject(studentIdi, subCodeS, subNameS, expectedmarksI, creditI)


    }

    def getAllSubjects : List[Subject] = {
      DB readOnly { implicit session =>
        sql"select * from subject".map(rs => Subject(rs.int("studentId"),rs.string("subCode"),
          rs.string("subName"), rs.int("expectedmarks"), rs.int("credit"))).list.apply()
      }
    }
    def insertAssessmentIntoSubjects(subjects:List[Subject]) : List[Subject] = {
        val _assessments = new ObservableBuffer[Assessment]()
        for (subject <- subjects){
          for (assessment <- Assessment.getAllAssessments){
            if(assessment.subCode == subject.subCode) {
              _assessments += assessment
            }
          }
          subject.assessments = _assessments
        }
        subjects
    }
  }

/*
  def calcTotalAssessmentMarks(_assessments: ObservableBuffer[Assessment]): Double = {
    var totalmarks = 0.0
    for (i <- assessments) {
      totalmarks += i.obtainedWeightage.value.toFloat
    }
    totalmarks
  }*/


/*
  assessments += new Assessment("assessment1", 15, 85, 100)// default
  assessments += new Assessment("assessment2", 15, 70, 100)// default
  assessments += new Assessment("Final Exam", 50, 70, 100)
  assessments += new Assessment("MidTerm", 20, 70, 100)
*/

/*
  def calcTotalAssessmentMarks(_assessments: ObservableBuffer[Assessment]): Double = {
    var totalmarks = 0.0
    for (i <- assessments) {
      totalmarks += i.obtainedWeightage.value.toFloat
    }
    totalmarks
  }*/

