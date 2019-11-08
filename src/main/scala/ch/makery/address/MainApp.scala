package ch.makery.address

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import ch.makery.address.model.{Assessment, Subject}
import ch.makery.address.view.SubjectEditDialogController
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp {
  // the data as an observable list of Persons
  val subjectData = new ObservableBuffer[Subject]()
    subjectData += new Subject("PRG2104", "Object-Oriented Programming")
    subjectData += new Subject("SEG2202", "Software Engineering")
    subjectData += new Subject("CSC3024", "Human Computer Interaction")
    subjectData += new Subject("MKT2224", "Marketing Principles")
    subjectData += new Subject("NET2201", "Computer Networks")


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
    title = "AddressApp"
    scene = new Scene {
      root = roots
    }
  }
  // actions for display person overview window 
  def showSubjectOverview() = {
    val resource = getClass.getResourceAsStream("view/SubjectOverview.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  } 

   def showSubjectEditDialog(subject: Subject): Boolean = {
    val resource = getClass.getResourceAsStream("view/SubjectEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[SubjectEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.subject = subject
    dialog.showAndWait()
    control.okClicked
  }
  // call to display PersonOverview when app start
  showSubjectOverview()
}