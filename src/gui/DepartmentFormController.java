package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.servicies.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	private Department dep;
	
	private DepartmentService depSer;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	public void setDepartment(Department dep) {
		this.dep = dep;
	}
	
	public void setDepartmentService(DepartmentService depSer) {
		this.depSer = depSer;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener); 
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) { //Parametro que pega a referencia da janela atual
		if(dep == null) {
			throw new IllegalStateException("Department was null");
		}
		if(depSer == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		try {
			dep = getFormData(); //Formata o objeto
			depSer.saveOrUpdate(dep); //Salva ele
			notifyDataChangeListeners();
			Utils.currentStage(event).close(); //Pega a referencia da janela atual e fecga
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Department getFormData() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("Validation exception");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updaetFormData() {
		if(dep == null ) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(dep.getId()));
		txtName.setText(dep.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet(); //Caso tiver um name vai ser setada a error message
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
	
}
