package model.servicies;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) {
		if (obj.getId() == null) { //Se não existir vai ser nulo, por consequencia adiciona
			dao.insert(obj);
		} else {
			dao.update(obj); //Se ja existir vai ser atualizado
		}
	}
	
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
}
