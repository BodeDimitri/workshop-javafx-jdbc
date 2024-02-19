package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() { //Metodo usado para carregar a tela
		loadView("/gui/About.fxml", x -> {}); 
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	//Consumer<T> sendo usado para passar um parametro dentro de uma expressão lambda 
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) { //synchronized garante que o processamento não vai ser interrompido
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent(); //Acessa o primeiro elemento (Scroll Pane) e depois acessa o content que contem o VBox
			
			Node mainMeu = mainVbox.getChildren().get(0); //Acessa o vBox e resgata o primeiro elemento filho
			mainVbox.getChildren().clear(); //Limpa tudo 
			mainVbox.getChildren().add(mainMeu); //Adoiciona o MainMenu
			mainVbox.getChildren().addAll(newVBox); //Adiciona a nova tela
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
