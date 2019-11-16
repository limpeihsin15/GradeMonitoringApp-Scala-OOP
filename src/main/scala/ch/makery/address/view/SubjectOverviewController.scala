package ch.makery.address.view

import ch.makery.address.model.{Assessment, Assignment, Subject}
import ch.makery.address.MainApp
import ch.makery.address.MainApp.{person1, subjectData}
import ch.makery.address.util.Calculate
import scalafx.scene.control.{Alert, Label, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{DoubleProperty, ObjectProperty, StringProperty}
import ch.makery.address.util.DateUtil._
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.stage.Stage

@sfxml
class SubjectOverviewController(

                                 private val subjectTable : TableView[Subject],
                                 private val subCodeColumn : TableColumn[Subject, String],
                                 private val subNameColumn : TableColumn[Subject, String],


                                 private val assessmentTable : TableView[Assessment],
                                 private val assessmentNameColumn : TableColumn[Assessment, String],
                                 private val assessmentMarksColumn : TableColumn[Assessment, String],
                                 private val assessmentWeightageColumn : TableColumn[Assessment, Int],

                                 private var studentNameLabel : Label,
                                 private val studentIdLabel: Label,
                                 private val programmeLabel: Label,
                                 private val intakeLabel: Label,
                                 private val cgpaLabel: Label,

                                 private var currentPercentageLabel : Label,
                                 private val ExpectedPercentageLabel: Label,
                                 private val difference: Label


    ) {
  // initialize Table View display contents model
  subjectTable.items = MainApp.person1.subject
  // initialize columns's cell values
  subCodeColumn.cellValueFactory = {_.value.subCode}
  subNameColumn.cellValueFactory  = {_.value.subName}

  studentNameLabel.text = MainApp.person1.firstName.value
  studentIdLabel.text = MainApp.person1.studentId.value.toString
  programmeLabel.text = MainApp.person1.programme.value
  intakeLabel.text = MainApp.person1.intake.value
  cgpaLabel.text = Calculate.calcCGPA(MainApp.person1).toString


   // showSubjectDetails(None);   //reason why table label wont show

    subjectTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showSubjectDetails(Some(newValue))
    )



  //show assessment list in Subject
  private def showSubjectDetails (subject : Option[Subject]) = {
    subject match {
      case Some(subject) =>
        assessmentTable.items = subject.assessments
        //currentPercentageLabel.text = subject.totalmark.value.toString
      // Fill the labels with info from the subject object.
        for (i <- 0 until subject.assessments.length) {
          assessmentNameColumn.cellValueFactory = {_.value.name}
          assessmentMarksColumn.cellValueFactory = {_.value.obtainedWeightage}
          assessmentWeightageColumn.cellValueFactory = {_.value.weightage}

        }
        currentPercentageLabel.text = Calculate.calcTotalAssessments(subject).toString
        ExpectedPercentageLabel.text = subject.expectedmarks.value.toString
        difference.text = (subject.expectedmarks.value -subject.currentmarks).toString
        cgpaLabel.text = Calculate.calcCGPA(MainApp.person1).toString

      case None =>
        // Person is null, remove all the text.
        assessmentNameColumn.text = ""
        assessmentMarksColumn.text  = ""
        currentPercentageLabel.text = ""
        ExpectedPercentageLabel.text = ""
        difference.text = ""
    }    
  }

  var         dialogStage : Stage  = null
  implicit var assessment = new Assessment()
  def isValidWeightage(implicit assessment: Assessment):Boolean={
    var errorMessage = ""
    val selectedSubject = subjectTable.selectionModel().selectedItem.value
    var totalweightage = 0
    selectedSubject.assessments.foreach(elem =>totalweightage += elem.weightage.value)
    totalweightage += assessment.weightage.value
    if ( totalweightage > 100 ){
      errorMessage += "Total weightage cannot exceed 100!\n"
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
  //create new assessment in subject
  def handleNewAssessment(action : ActionEvent) = {
    val assessment = new Assessment()
    if (subjectTable.selectionModel().selectedItem.value != null){
      val okClicked = MainApp.showAssessmentEditDialog(assessment);
      if (isValidWeightage(assessment)) {
        if(okClicked){
          subjectTable.selectionModel().selectedItem.value.assessments += assessment
          Calculate.calcCGPA(MainApp.person1)
          showSubjectDetails(Some(subjectTable.selectionModel().selectedItem.value))
        }
      }
    }
    else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning){
        initOwner(MainApp.stage)
        title       = "No Selection"
        headerText  = "No Assessment Selected"
        contentText = "Please select a subject in the table."
      }.showAndWait()
    }


  }

  //edit assessment
  def handleEditAssessment(action : ActionEvent) = {
    val selectedAssessment = assessmentTable.selectionModel().selectedItem.value
    if (selectedAssessment != null) {
      val okClicked = MainApp.showAssessmentEditDialog(selectedAssessment) // when the button is clicked
      if(isValidWeightage){
        if (okClicked) showSubjectDetails(Some(subjectTable.selectionModel().selectedItem.value))


      }

    } else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning){
        initOwner(MainApp.stage)
        title       = "No Selection"
        headerText  = "No Assessment Selected"
        contentText = "Please select a subject in the table."
      }.showAndWait()
    }
  }

  //delete assessment
  def handleDeleteAssessment(action : ActionEvent) = {
    val selectedIndex = assessmentTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) {
      assessmentTable.items().remove(selectedIndex);
      showSubjectDetails(Some(subjectTable.selectionModel().selectedItem.value))
    } else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning){
        initOwner(MainApp.stage)
        title       = "No Selection"
        headerText  = "No Assessment Selected"
        contentText = "Please select a subject in the table."
      }.showAndWait()
    }
  }

  def handleNewSubject(action : ActionEvent) = {
    val subject = new Subject("","")
    val okClicked = MainApp.showSubjectEditDialog(subject);  //
        if (okClicked) {
            MainApp.subjectData += subject
        }
  }


  def handleEditSubject(action : ActionEvent) = {
    val selectedSubject = subjectTable.selectionModel().selectedItem.value
    if (selectedSubject != null) {
      val okClicked = MainApp.showSubjectEditDialog(selectedSubject)

      if (okClicked) showSubjectDetails(Some(selectedSubject))

    } else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning){
        initOwner(MainApp.stage)
        title       = "No Selection"
        headerText  = "No Subject Selected"
        contentText = "Please select a subject in the table."
      }.showAndWait()
    }
  }

  def handleDeleteSubject(action : ActionEvent) = {
      val selectedIndex = subjectTable.selectionModel().selectedIndex.value
      if (selectedIndex >= 0) {
          subjectTable.items().remove(selectedIndex);

      } else {
    // Nothing selected.
    val alert = new Alert(Alert.AlertType.Warning){
      initOwner(MainApp.stage)
      title       = "No Selection"
      headerText  = "No Subject Selected"
      contentText = "Please select a subject in the table."
    }.showAndWait()
  }
}}


