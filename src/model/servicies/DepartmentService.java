package model.servicies;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Department obj) {
		if (obj.getId() == null) { //Se não existir vai ser nulo, por consequencia adiciona
			dao.insert(obj);
		} else {
			dao.update(obj); //Se ja existir vai ser atualizado
		}
	}
	
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}
}
