package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.servicies.DepartmentService;
import model.servicies.SellerService;

public class SellerFormController implements Initializable {

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField baseSalary;

	@FXML
	private ComboBox<Department> combBoxDepartment;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private Seller dep;

	private SellerService depSer;

	private DepartmentService depDepSer;

	private ObservableList<Department> obsList;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	public void setSeller(Seller dep) {
		this.dep = dep;
	}

	public void setServices(SellerService depSer, DepartmentService depDepSer) {
		this.depSer = depSer;
		this.depDepSer = depDepSer;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) { // Parametro que pega a referencia da janela atual
		if (dep == null) {
			throw new IllegalStateException("Seller was null");
		}
		if (depSer == null) {
			throw new IllegalStateException("SellerService was null");
		}
		try {
			dep = getFormData(); // Formata o objeto
			depSer.saveOrUpdate(dep); // Salva ele
			notifyDataChangeListeners();
			Utils.currentStage(event).close(); // Pega a referencia da janela atual e fecga
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation exception");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());
		
		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		} 		else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		
		if (baseSalary.getText() == null || baseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty");
		}
		
		obj.setBaseSalary(Utils.tryParseToDouble(baseSalary.getText()));
		
		obj.setDepartment(combBoxDepartment.getValue());
		
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

	private void initializeNodes() { // Aqui coloca as constraints
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(baseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void updaetFormData() {
		if (dep == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(dep.getId()));
		txtName.setText(dep.getName());
		txtEmail.setText(dep.getEmail());
		Locale.setDefault(Locale.US);
		baseSalary.setText(String.format("%.2f", dep.getBaseSalary()));
		if (dep.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(dep.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if (dep.getDepartment() == null) { //Caso seja um vendedor novo não vai ter departamento
			combBoxDepartment.getSelectionModel().selectFirst();
		} else {
			combBoxDepartment.setValue(dep.getDepartment());
		}
	}

	public void loadAssociatedObjects() {

		if (depDepSer == null) {
			throw new IllegalStateException("Department service was null");
		}
		List<Department> list = depDepSer.findAll();
		obsList = FXCollections.observableArrayList(list);
		combBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet(); // Caso tiver um name vai ser setada a error message

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		} else {
			labelErrorName.setText("");
		}
		
		if (fields.contains("email")) {
			labelErrorEmail.setText(errors.get("email"));
		} else {
			labelErrorEmail.setText("");
		}
		
		if (fields.contains("baseSalary")) {
			labelErrorBaseSalary.setText(errors.get("baseSalary"));
		} else {
			labelErrorBaseSalary.setText("");
		}
		
		if (fields.contains("birthDate")) {
			labelErrorBirthDate.setText(errors.get("birthDate"));
		} else {
			labelErrorBirthDate.setText("");
		}
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		combBoxDepartment.setCellFactory(factory);
		combBoxDepartment.setButtonCell(factory.call(null));
	}

}
