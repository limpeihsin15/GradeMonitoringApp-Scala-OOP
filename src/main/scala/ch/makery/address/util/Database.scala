package ch.makery.address.util
import ch.makery.address.model.{Assessment, Person, Student, Subject}
import scalikejdbc.{AutoSession, ConnectionPool, DB}
trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"

  val dbURL = "jdbc:derby:myDB;create=true;";
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)

  ConnectionPool.singleton(dbURL, "me", "mine")

  //ad-hoc session provider on the REPL
  implicit val session = AutoSession
}

object Database extends Database {
  def setupDB() = {
    if (!hasDBInitialize) {
      Person.initializeTable()
    }
    if (!hasStudentDBInitialize) {
      Student.initializeTable()
    }

  }


  def hasDBInitialize: Boolean = {
    DB getTable "Person" match {
      case Some(x) => true
      case None => false
    }}

    def hasStudentDBInitialize: Boolean = {
      val temp2 = DB getTable "student" match {
        case Some(x) => true
        case None => false
      }
      val temp3 = DB getTable "subject" match {
        case Some(x) => true
        case None => false
      }

      val temp4 = DB getTable "assessment" match {
        case Some(x) => true
        case None => false
      }

      temp2 && temp3 && temp4
    }



}
