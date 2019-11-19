package ch.makery.address.view

import ch.makery.address.model.{Student, Subject}
import ch.makery.address.MainApp
import scalafx.scene.control.{Alert, Label, TableColumn, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.address.util.DateUtil._
import scalafx.event.ActionEvent
import scalikejdbc._

import scala.util.{Failure, Success}

@sfxml
class LoginPageController (

                                    private val  lastNameField : TextField,
                                    private val   studentIdField : TextField,

                                  ) {
  var okClicked = false

  def handleLogin(action: ActionEvent) {
    if (isExist) {
      MainApp.person1 = Student.getStudent(lastNameField.text.value, Integer.parseInt(studentIdField.getText()), Student.getAllStudents)
      MainApp.showSubjectOverview()
      }
    okClicked = true;
  }

  def handleNewStudent (action : ActionEvent) = {
    val student = new Student()
    val okClicked = MainApp.showStudentEditDialog(student);  //
    if (okClicked) {
      student.save() match {
        case Success(x) =>
          MainApp.person1 = student
          MainApp.showSubjectOverview()
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem failed to save changes"
          }.showAndWait()
      }


    }
  }


  def isExist : Boolean =  {
    DB readOnly { implicit session =>

      sql"""
				select * from student where
				studentId = ${Integer.parseInt(studentIdField.text.value)} and lastName = ${lastNameField.text.value}
			""".map(rs => rs.string("lastName")).single.apply()

    } match {
      case Some(x) => true
      case None => false
    }

  }
}
