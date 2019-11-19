package ch.makery.address.view

import ch.makery.address.model._
import ch.makery.address.MainApp
import ch.makery.address.MainApp.{person1}
import ch.makery.address.util.Calculate
import scalafx.scene.control._
import scalafxml.core.macros.sfxml
import scalafx.beans.property.{DoubleProperty, ObjectProperty, StringProperty}
import ch.makery.address.util.DateUtil._
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.stage.Stage

import scala.util.{Failure, Success}

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
                                 private val difference: Label,

                                 private var comboboxType : ComboBox[String],



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


  comboboxType += "Final Exam"
  comboboxType += "Assignment"
  comboboxType += "Test"
  comboboxType += "Quiz"


   // showSubjectDetails(None);   //reason why table label wont show

    subjectTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showSubjectDetails(Some(newValue))
    )



  //show assessment list in Subject
  private def showSubjectDetails (subject : Option[Subject]) = {
    subject match {
      case Some(subject) =>
        if(subject.assessments != null) {
          assessmentTable.items = subject.assessments
          //currentPercentageLabel.text = subject.totalmark.value.toString
          // Fill the labels with info from the subject object.
          for (i <- 0 until subject.assessments.length) {
            assessmentNameColumn.cellValueFactory = {
              _.value.name
            }
            assessmentMarksColumn.cellValueFactory = {
              _.value.obtainedWeightage
            }
            assessmentWeightageColumn.cellValueFactory = {
              _.value.weightage
            }

          }
          currentPercentageLabel.text = Calculate.calcTotalAssessments(subject).toString
          ExpectedPercentageLabel.text = subject.expectedmarks.value.toString
          difference.text = (subject.expectedmarks.value - subject.currentmarks).toString
          cgpaLabel.text = Calculate.calcCGPA(MainApp.person1).toString
        } else{
          assessmentNameColumn.text = ""
          assessmentMarksColumn.text  = ""
          currentPercentageLabel.text = ""
          ExpectedPercentageLabel.text = ""
          difference.text = ""
        }
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

    if (subjectTable.selectionModel().selectedItem.value != null){
      if(comboboxType.selectionModel().getSelectedIndex != -1 ) {
        val assessment = createAssessment(comboboxType.selectionModel().getSelectedIndex, subjectTable.selectionModel().selectedItem.value.subCode.value)
        val okClicked = MainApp.showAssessmentEditDialog(assessment);
        if (okClicked) {
          if (isValidWeightage(assessment)) {

            assessment.saveAssessment()
            match {

              case Success(x) =>
                subjectTable.selectionModel().selectedItem.value.assessments += assessment
                Calculate.calcCGPA (MainApp.person1)
                showSubjectDetails (Some (subjectTable.selectionModel().selectedItem.value) )


              case Failure(e) =>
                val alert = new Alert(Alert.AlertType.Warning) {
                  initOwner(MainApp.stage)
                  title = "Failed to Save"
                  headerText = "Database Error"
                  contentText = "Database problem failed to save changes"
                }.showAndWait()
            }
          }}}else {
          displayErrorMessage("Type")}
      }else{displayErrorMessage("Subject")
  }}

  def createAssessment(i: Int, subCode:String): Assessment = {
    i match{
       case 0 =>
        return new FinalExam(subCode)
       case 1 =>
         return new Assignment(subCode)
       case 2 =>
         return new Test(subCode)
       case 3 =>
         return new Quiz(subCode)
    }
  }
  //edit assessment
  def handleEditAssessment(action : ActionEvent) = {
    val selectedAssessment = assessmentTable.selectionModel().selectedItem.value
    if (selectedAssessment != null) {
      val okClicked = MainApp.showAssessmentEditDialog(selectedAssessment) // when the button is clicked
      if (isValidWeightage) {
        if (okClicked) {
          selectedAssessment.saveAssessment() match {
            case Success(x) =>
              showSubjectDetails(Some(subjectTable.selectionModel().selectedItem.value))
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
    }
     else {
      // Nothing selected.
      displayErrorMessage("Assessment")
    }
  }
  //delete assessment
  def handleDeleteAssessment(action : ActionEvent) = {
    val selectedIndex = assessmentTable.selectionModel().selectedIndex.value
    val selectedAssessment = assessmentTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) {
      selectedAssessment.deleteAssessment() match{
        case Success(x) => assessmentTable.items().remove(selectedIndex)
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem failed to save changes"
          }.showAndWait()
      }
      showSubjectDetails(Some(subjectTable.selectionModel().selectedItem.value))
    } else {
      // Nothing selected.
      displayErrorMessage("Assessment")
    }
  }

  def handleNewSubject(action : ActionEvent) = {
    val subject = new Subject(MainApp.person1.studentId.value, "","",creditI= 0)
    val okClicked = MainApp.showSubjectEditDialog(subject);  //
        if (okClicked) {

        subject.saveSubject() match {
          case Success(x) => MainApp.person1.subject += subject
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


  def handleEditSubject(action : ActionEvent) = {
    val selectedSubject = subjectTable.selectionModel().selectedItem.value
    if (selectedSubject != null) {
      val okClicked = MainApp.showSubjectEditDialog(selectedSubject)

      if (okClicked) {
        selectedSubject.saveSubject() match {
          case Success(x) => showSubjectDetails(Some(selectedSubject))
          case Failure(e) =>
            val alert = new Alert(Alert.AlertType.Warning) {
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = "Database problem failed to save changes"
            }.showAndWait()
        }

      }

    } else {
      // Nothing selected.
      displayErrorMessage("Subject")
    }
  }

  def handleDeleteSubject(action : ActionEvent) = {
      val selectedIndex = subjectTable.selectionModel().selectedIndex.value
    val selectedSubject = subjectTable.selectionModel().selectedItem.value
      if (selectedIndex >= 0) {
          selectedSubject.deleteSubject() match{
            case Success(x) => subjectTable.items().remove(selectedIndex)
            case Failure(e) =>
              val alert = new Alert(Alert.AlertType.Warning) {
                initOwner(MainApp.stage)
                title = "Failed to Save"
                headerText = "Database Error"
                contentText = "Database problem failed to save changes"
              }.showAndWait()
          }

      } else {
        displayErrorMessage("Subject")
  }
}
def displayErrorMessage(s:String): Unit ={
  // Nothing selected.
  val alert = new Alert(Alert.AlertType.Warning){
    initOwner(MainApp.stage)
    title       = "No Selection"
    headerText  = "No "+ s +" Selected"
    contentText = "Please select a "+ s +" in the table."
  }.showAndWait()
}

  def handleEditStudent(action : ActionEvent) = {
      val okClicked = MainApp.showStudentEditDialog(MainApp.person1)
      if (okClicked) {
        MainApp.person1.save() match {
          case Success(x) => MainApp.showSubjectOverview
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

}


