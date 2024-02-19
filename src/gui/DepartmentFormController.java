package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

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
	
	public void setDepartment(Department dep) {
		this.dep = dep;
	}
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("1");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("2");
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
	
}