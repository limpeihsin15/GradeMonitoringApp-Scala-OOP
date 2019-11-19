package ch.makery.address

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import ch.makery.address.model.{Assessment, Person, Student, Subject}
import ch.makery.address.util.{Calculate, Database, DateUtil}
import ch.makery.address.view.{AssessmentEditDialogController, StudentEditDialogController, SubjectEditDialogController}
import scalafx.scene.control.Alert
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

import scala.util.{Failure, Success}

object MainApp extends JFXApp {

  //initialize database
  Database.setupDB()

  /*run this code if person1 not yet initialize in database
  // the data as an observable list of Subjects
  val subjectData = new ObservableBuffer[Subject]()

  //assign all subject into subjectData array
  subjectData ++= Subject.getAllSubjects

  //val students = new ObservableBuffer[Student]()
  //students ++= Student.getAllStudents
  val person1 = new Student("firstname","lastname", 12345,"programme","august18",subjectData)
  person1.save() match {

    case Success(x) =>true
    case Failure(e) =>
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "Failed to Save"
        headerText = "Database Error"
        contentText = "Database problem failed to save changes"
      }.showAndWait()
  }
  */


  /*run this if person1 is in database
  var person1 = Student.getStudent("Lim", 15104763, Student.getAllStudents)
  */


  var person1 = new Student()

  // transform path of RootLayout.fxml to URI for resource location.
  val rootResource = getClass.getResourceAsStream("view/RootLayout.fxml")
  // initialize the loader object.
  val loader = new FXMLLoader(null, NoDependencyResolver)
  // Load root layout from fxml file.
  loader.load(rootResource);
  // retrieve the root component BorderPane from the FXML 
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  // initialize stage
  stage = new PrimaryStage {
    title = "Grade Monitoring App"
    scene = new Scene (width= 1300, height = 700) {
      root = roots
      stylesheets = List(getClass.getResource("view/GradeMonitoring.css").toExternalForm)
      icons += new Image(getClass.getResourceAsStream("view/student.png"))
    }
  }
  // actions for display subject overview window
  def showSubjectOverview() = {
    val resource = getClass.getResourceAsStream("view/SubjectOverview.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  def showLoginPage() = {
    val resource = getClass.getResourceAsStream("view/LoginPage.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

   def showSubjectEditDialog(subject: Subject): Boolean = {
    val resource = getClass.getResourceAsStream("view/SubjectEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);//
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[SubjectEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        stylesheets = List(getClass.getResource("view/GradeMonitoring.css").toExternalForm)
      }
    }
    control.dialogStage = dialog
    control.subject = subject
    dialog.showAndWait()
    control.okClicked
  }
  def showAssessmentEditDialog(assessment: Assessment): Boolean = {
    val resource = getClass.getResourceAsStream("view/AssessmentEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[AssessmentEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        stylesheets = List(getClass.getResource("view/GradeMonitoring.css").toExternalForm)
      }
    }
    control.dialogStage = dialog
    control.assessment = assessment
    dialog.showAndWait()
    control.okClicked
  }

  def showStudentEditDialog(student: Student): Boolean = {
    val resource = getClass.getResourceAsStream("view/StudentEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);//
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[StudentEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
        stylesheets = List(getClass.getResource("view/GradeMonitoring.css").toExternalForm)
      }
    }
    control.dialogStage = dialog
    control.student = student
    dialog.showAndWait()
    control.okClicked
  }

  // call to display SubjectOverview when app start
  //showSubjectOverview()
  showLoginPage()
}
