package ch.makery.address.view

import ch.makery.address.model.Assessment
import ch.makery.address.MainApp
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import ch.makery.address.util.DateUtil._
import javafx.collections.ObservableList
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.ComboBox


@sfxml
class AssessmentEditDialogController (

                                     //private val comboboxType : ComboBox[String],
                                   private val  assessmentNameField : TextField,
                                   private val   weightageField : TextField,
                                   private val   obtainedRawField : TextField,
                                   private val   totalRawField : TextField,



                                 ){
  var         dialogStage : Stage  = null
  private var _assessment : Assessment = null
  var         okClicked           = false

  def assessment = _assessment
  def assessment_=(x : Assessment) {
    _assessment = x

    assessmentNameField.text = _assessment.name.value
    weightageField.text  = _assessment.weightage.value.toString
    obtainedRawField.text = _assessment.obtainedRaw.value.toString
    totalRawField.text = _assessment.totalRaw.value.toString

  }

  def handleOk(action :ActionEvent){

    if (isInputValid()) {
      _assessment.name <== assessmentNameField.text
      _assessment.weightage  <== ObjectProperty(Integer.parseInt(weightageField.getText()))
      _assessment.obtainedRaw <== Integer.parseInt(obtainedRawField.getText())
      _assessment.totalRaw <== Integer.parseInt(totalRawField.getText())
      _assessment.obtainedWeightage <== _assessment.calcWeightage(_assessment.obtainedRaw,_assessment.totalRaw,_assessment.weightage)


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

    if (nullChecking(assessmentNameField.text.value))
      errorMessage += "No valid assessment name!\n"

    else {
      try {
        Integer.parseInt(weightageField.getText());
      } catch {
        case e : NumberFormatException =>
          errorMessage += "No valid weightage (must be an integer)!\n"
      }
      try {
        Integer.parseInt(obtainedRawField.getText());
      } catch {
        case e : NumberFormatException =>
          errorMessage += "No valid obtained Marks (must be an integer)!\n"
      }
      try {
        Integer.parseInt(totalRawField.getText());
      } catch {
        case e : NumberFormatException =>
          errorMessage += "No valid total obtainable marks (must be an integer)!\n"
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





















