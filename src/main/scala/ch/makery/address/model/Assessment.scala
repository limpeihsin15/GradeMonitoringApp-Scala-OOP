package ch.makery.address.model

import ch.makery.address.util.{Calculate, Database}
import scalafx.beans.property.{DoubleProperty, IntegerProperty, ObjectProperty, StringProperty}
import scalikejdbc._
import scala.util.Try

// create an object of Assessment and define apply fucntion
// that's mean Assessment can be treat as a function
class Assessment(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) extends Database {

  def this() = this(null, "Assessment", 0, 0, 0)
  def this(nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) = this(null, "Assessment", 0, 0, 0)
  def this(subCodeS: String) = this(subCodeS, "Assessment", 0, 0, 0)

  var subCode           = new StringProperty(subCodeS)
  val name              = new StringProperty(nameS)
  val weightage         = ObjectProperty(weightageI)
  val obtainedRaw       = IntegerProperty(obtainedRawI)
  val totalRaw          = IntegerProperty(totalRawI)
  val obtainedWeightage = calcWeightage(obtainedRaw, totalRaw, weightage)


  def calcWeightage(_obtainedRaw: IntegerProperty, _totalRaw: IntegerProperty, _weightage: ObjectProperty[Int]) =
    StringProperty(Calculate.roundAt2(_obtainedRaw.value.toFloat / _totalRaw.value.toFloat * _weightage.value.toFloat).toString)

  def test(): StringProperty = name

  override def toString: String = name.toString() + weightage.toString() + obtainedRaw.toString() + totalRaw.toString()


  def saveAssessment(): Try[Int] = {
    if (!(isAssessmentExist)) {
      Try(DB autoCommit { implicit session => {
        sql"""

                insert into assessment (subCode, name, weightage, obtainedRaw, totalRaw)
                values (${subCode.value}, ${name.value}, ${weightage.value}, ${obtainedRaw.value}, ${totalRaw.value})


                """.update.apply()
      }

      })
    }
    else {
      Try(DB autoCommit { implicit session => {
        sql"""
              update assessment
              set
              subCode         = ${subCode.value},
              name            = ${name.value},
              weightage       = ${weightage.value},
              obtainedRaw     = ${obtainedRaw.value},
              totalRaw        = ${totalRaw.value}
              where subCode   = ${subCode.value} and name = ${name.value}
              """.update.apply()
      }
      })

    }

  }


  //delete assessment
  def deleteAssessment(): Try[Int] = {
    if (isAssessmentExist) {
      Try(DB autoCommit { implicit session =>

        //delete assessment that inside assessment table
        sql"""
              delete from assessment
              where subCode = ${subCode.value} and name = ${name.value}
              """.update.apply()


      }
      ) //end of Try
    }
    else
      throw new Exception("Assessment not Exists in Database")
  }

  //check existence of assessment
  def isAssessmentExist: Boolean = {
    DB readOnly { implicit session => {
      sql"""
              select * from assessment
              where subCode = ${subCode.value} and name = ${name.value}
              """.map(rs => rs.string("subCode")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }
    }
  } //end of isAssessmentExist
}



  object Assessment extends Database {
    def apply(subCodeS: String,
              nameS: String,
              weightageI: Int,
              obtainedRawI: Int,
              totalRawI: Int
             ): Assessment = {
      new Assessment(subCodeS, nameS, weightageI,obtainedRawI, totalRawI)
    }

    def getAllAssessments : List[Assessment] = {
      DB readOnly {implicit session =>
        //

        sql"select * from assessment".map(rs => Assessment(rs.string("subCode"),
          rs.string("name"), rs.int("weightage"), rs.int("obtainedRaw"),rs.int("totalRaw"))).list.apply()
      }
    }
}


class FinalExam(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int)
  extends Assessment(subCodeS: String,nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int){

  def this() = this(null, "Final Exam", 50, 0, 100)
  def this(subCodeS: String) = this(subCodeS, "Final Exam", 50, 0, 100)
}

class Coursework(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int)
  extends Assessment(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int)

class Assignment(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int)
  extends Coursework(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {

  def this() = this(null, "Assignment", 20, 0, 100)
  def this(subCodeS: String) = this(subCodeS, "Assignment", 20, 0, 100)
}

class Test(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int)
  extends Coursework(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {

  def this() = this(null, "Test", 20, 0, 100)
  def this(subCodeS: String) = this(subCodeS, "Test", 20, 0, 100)
}

class Quiz(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int)
  extends Coursework(subCodeS: String, nameS: String, weightageI: Int, obtainedRawI: Int, totalRawI: Int) {

  def this() = this(null, "Quiz", 10, 0, 100)
  def this(subCodeS: String) = this(subCodeS, "Quiz", 10, 0, 100)}


