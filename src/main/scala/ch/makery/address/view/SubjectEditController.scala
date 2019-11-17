package ch.makery.address.view

import ch.makery.address.model.{ Subject}
import ch.makery.address.MainApp
import scalafx.scene.control.{Alert, Label, TableColumn, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.address.util.DateUtil._
import scalafx.event.ActionEvent

@sfxml
class SubjectEditDialogController (

    private val  subNameField : TextField,
    private val   subCodeField : TextField,
    private val creditField : TextField,


){
  var         dialogStage : Stage  = null
  private var _subject     : Subject = null
  var         okClicked            = false

  def subject = _subject
  def subject_=(x : Subject) {
        _subject = x

        subNameField.text = _subject.subName.value
        subCodeField.text  = _subject.subCode.value
        creditField.text = _subject.credit.value.toString

  }

  def handleOk(action :ActionEvent){

     if (isInputValid()) {
        _subject.subName <== subNameField.text
        _subject.subCode  <== subCodeField.text
       _subject.credit <==  Integer.parseInt(creditField.getText())
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

    if (nullChecking(subNameField.text.value))
      errorMessage += "No valid subject name!\n"
    if (nullChecking(subCodeField.text.value))
      errorMessage += "No valid subuject code!\n"


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