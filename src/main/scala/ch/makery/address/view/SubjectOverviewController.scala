package ch.makery.address.view

import ch.makery.address.model.{Assessment,  Subject}
import ch.makery.address.MainApp
import scalafx.scene.control.{Alert, Label, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{DoubleProperty, ObjectProperty, StringProperty}
import ch.makery.address.util.DateUtil._
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class SubjectOverviewController(

                                 private val subjectTable : TableView[Subject],

                                 private val subCodeColumn : TableColumn[Subject, String],

                                 private val subNameColumn : TableColumn[Subject, String],

                                 private val assessmentTable : TableView[Assessment],

                                 private val assessmentNameColumn : TableColumn[Assessment, String],

                                 private val assessmentMarksColumn : TableColumn[Assessment, String],

    ) {
  // initialize Table View display contents model
  subjectTable.items = MainApp.subjectData
  // initialize columns's cell values
  subCodeColumn.cellValueFactory = {_.value.subCode}
  subNameColumn.cellValueFactory  = {_.value.subName}


  showSubjectDetails(None);
  
  subjectTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showSubjectDetails(Some(newValue))
  )
  
  private def showSubjectDetails (subject : Option[Subject]) = {
    subject match {
      case Some(subject) =>
        assessmentTable.items = subject.assessments
      // Fill the labels with info from the subject object.
        for (i <- 0 until subject.assessments.length) {
          assessmentNameColumn.cellValueFactory = {_.value.name}
          assessmentMarksColumn.cellValueFactory = {_.value.obtainedWeightage}
        }

      case None =>
        // Person is null, remove all the text.
        assessmentNameColumn.text = ""
        assessmentMarksColumn.text  = ""
    }    
  }

  def handleNewSubject(action : ActionEvent) = {
    val subject = new Subject("","")
    val okClicked = MainApp.showSubjectEditDialog(subject);
        if (okClicked) {
            MainApp.subjectData += subject
        }
  }
  /*
  def handleEditPerson(action : ActionEvent) = {
    val selectedPerson = personTable.selectionModel().selectedItem.value
    if (selectedPerson != null) {
        val okClicked = MainApp.showPersonEditDialog(selectedPerson)

        if (okClicked) showPersonDetails(Some(selectedPerson))

    } else {
        // Nothing selected.
        val alert = new Alert(Alert.AlertType.Warning){
          initOwner(MainApp.stage)
          title       = "No Selection"
          headerText  = "No Person Selected"
          contentText = "Please select a person in the table."
        }.showAndWait()
    }
  }
  def handleDeletePerson(action : ActionEvent) = {
      val selectedIndex = personTable.selectionModel().selectedIndex.value
      if (selectedIndex >= 0) {
          personTable.items().remove(selectedIndex);
      } 
   } 
  */
}