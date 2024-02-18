package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.servicies.DepartmentService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDeparmmentAction() {
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml"); //Metodo usado para carregar a tela
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	private synchronized void loadView(String absoluteName) { //synchronized garante que o processamento não vai ser interrompido
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent(); //Acessa o primeiro elemento (Scroll Pane) e depois acessa o content que contem o VBox
			
			Node mainMeu = mainVbox.getChildren().get(0); //Acessa o vBox e resgata o primeiro elemento filho
			mainVbox.getChildren().clear(); //Limpa tudo 
			mainVbox.getChildren().add(mainMeu); //Adoiciona o MainMenu
			mainVbox.getChildren().addAll(newVBox); //Adiciona a nova tela
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String absoluteName) { //synchronized garante que o processamento não vai ser interrompido
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent(); //Acessa o primeiro elemento (Scroll Pane) e depois acessa o content que contem o VBox
			
			Node mainMeu = mainVbox.getChildren().get(0); //Acessa o vBox e resgata o primeiro elemento filho
			mainVbox.getChildren().clear(); //Limpa tudo 
			mainVbox.getChildren().add(mainMeu); //Adoiciona o MainMenu
			mainVbox.getChildren().addAll(newVBox); //Adiciona a nova tela
			
			
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
