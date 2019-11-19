package ch.makery.address.view

import ch.makery.address.model.{Student, Subject}
import ch.makery.address.MainApp
import scalafx.scene.control.{Alert, Label, TableColumn, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.address.util.DateUtil._
import scalafx.event.ActionEvent

import scala.util.{Failure, Success}

@sfxml
class StudentEditDialogController (

                                    private val firstNameField : TextField,
                                    private val lastNameField : TextField,
                                    private val studentIdField : TextField,
                                    private val programmeField : TextField,
                                    private val intakeField : TextField,

                                  ){
  var         dialogStage : Stage  = null
  private var _student     : Student = null
  var         okClicked            = false

  def student = _student
  def student_=(x : Student) {
    _student = x

    firstNameField.text = _student.firstName.value
    lastNameField.text  = _student.lastName.value
    studentIdField.text = _student.studentId.value.toString
    programmeField.text = _student.programme.value
    intakeField.text = _student.intake.value
  }

  def handleOk(action :ActionEvent){

    if (isInputValid()) {
      _student.firstName <== firstNameField.text
      _student.lastName  <== lastNameField.text
      _student.studentId <==  Integer.parseInt(studentIdField.getText())
      _student.programme <== programmeField.text
      _student.intake <== intakeField.text

      okClicked = true;
      dialogStage.close()
    }
  }

  def handleCancel(action :ActionEvent) {
    dialogStage.close();
  }
  def nullChecking (x : String) = x == null || x.length == 0

  def isInputValid() : Boolean = {
    var errorMessage = ""

    if (nullChecking(firstNameField.text.value))
      errorMessage += "No valid first name!\n"
    if (nullChecking(lastNameField.text.value))
      errorMessage += "No valid last name!\n"
    if (nullChecking(programmeField.text.value))
      errorMessage += "No valid programme!\n"
    if (nullChecking(intakeField.text.value))
      errorMessage += "No valid intake!\n"
    else {
      try {
        Integer.parseInt(studentIdField.getText());
      } catch {
        case e: NumberFormatException =>
          errorMessage += "No valid Student ID (must be an integer)!\n"
      }
    }

    if (errorMessage.length() == 0) {
      return true;
    } else {
      // Show the error message.
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()

      return false;
    }
  }
}