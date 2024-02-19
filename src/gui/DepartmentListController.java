package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.servicies.DepartmentService;

public class DepartmentListController implements Initializable {
	
	private DepartmentService service;
	
	@FXML //As listas vão ser o tipo da variavel que vai ser armazenado
	private TableView<Department> tableViewDepartment;
	
	@FXML 
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	public void onBtNewAction(ActionEvent event) { // ActionEvent
		Stage parentStage = Utils.currentStage(event); //Utils.currentStage(event) -> parentStage
		Department obj = new Department(); //Para a lista aparecer vazia e instanciado um departamento vazio
		createDialogForm(obj ,"/gui/DepartmentForm.fxml", parentStage);
	}
	
	//Injeção de dep.
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id")); //Todo esse comando e usado preparar a variavel que vai ser carregada, e passado como parametro o nome do atributo na package de entities
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); //Referencia a tela do main e pega a proprção da tela
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty()); //Define o novo tamanho(altura) da Coluna usando a do stage como base 
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}
	
	private void createDialogForm(Department obj, String absoluteName,Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			//Injetando a dependencia
			DepartmentFormController controller = loader.getController(); //Referencia
			controller.setDepartment(obj);
			controller.updaetFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); //Mantem o tamanho
			dialogStage.initOwner(parentStage); //
			dialogStage.initModality(Modality.WINDOW_MODAL); //Trava a tela, so desbloqueando quando for preenchida
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
