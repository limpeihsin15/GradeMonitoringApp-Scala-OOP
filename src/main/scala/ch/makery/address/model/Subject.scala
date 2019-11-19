package ch.makery.address.model
import ch.makery.address.MainApp
import ch.makery.address.model.Assessment
import ch.makery.address.util.{Calculate, Database}
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Alert
import scalikejdbc._
import scala.util.control.Breaks._

import scala.collection.TraversableOnce
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}


class Subject (studentIdi: Int, subCodeS : String, subNameS : String, assessmentsOB: ObservableBuffer[Assessment], expectedmarksI:Int, creditI:Int) extends Database {
  def this() = this(0, null, null, null, 0, 0)
  def this(studentIdi: Int, subCodeS : String, subNameS : String, expectedmarksI:Int, creditI:Int) = this(0, subCodeS, subNameS, null, expectedmarksI, creditI)
  def this(studentIdi: Int, subCodeS : String, subNameS : String ,creditI:Int) = {
    this(studentIdi, subCodeS, subNameS, new ObservableBuffer[Assessment](), 70, creditI)
    assessments += new FinalExam(subCodeS)
    assessments += new Assignment(subCodeS)
    assessments += new Test(subCodeS)
    assessments += new Quiz(subCodeS)
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
              where subCode = ${subCode.value} and subName = ${subName.value}
              """.map(rs => rs.string("subCode")).single.apply()
      } match {
        case Some(x) => true
        case None => false
      }


  }}


  object Subject extends Database {
    def apply(studentIdi: Int,
              subCodeS: String,
              subNameS: String,
              assessments: ObservableBuffer[Assessment],
              expectedmarksI: Int,
              creditI: Int
             ): Subject = {
      new Subject(studentIdi, subCodeS, subNameS, assessments, expectedmarksI, creditI)


    }

    def getAllSubjects: List[Subject] = {

      DB readOnly { implicit session =>
        sql"select * from subject".map(rs => Subject(rs.int("studentId"), rs.string("subCode"),
          rs.string("subName"), getSubjectAssessment((rs.string("subCode")), nestedAssessment), rs.int("expectedmarks"), rs.int("credit"))).list.apply()
      }
    }

    def cleanAssessment(_subCode: String, _assessments: List[Assessment]): ObservableBuffer[Assessment] = {
      var temp = new ObservableBuffer[Assessment]()
      for (i <- _assessments.indices) {
        if (_assessments(i).subCode.value == _subCode) {
          temp += _assessments(i)
        }
      }
      temp
    }

    def nestedAssessment: List[ObservableBuffer[Assessment]] = {
      DB readOnly { implicit session =>
        sql"""select subCode from subject""".map(rs => cleanAssessment(rs.string("subCode"), Assessment.getAllAssessments)).list.apply()
      }
    }

    def getSubjectAssessment(subCode: String, _nestedAssessment: List[ObservableBuffer[Assessment]]): ObservableBuffer[Assessment] = {
      var temp = new ObservableBuffer[Assessment]()
      breakable{
        for (list <- _nestedAssessment) {
          for (assessment <- list) {
            if (assessment.subCode.value == subCode && !temp.contains(assessment)) {
              temp ++= list
              break
            }
          }
        }
      }
      temp
    }

  }


