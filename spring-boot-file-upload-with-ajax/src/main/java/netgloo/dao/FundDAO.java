package netgloo.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import netgloo.bean.Fund;

@Repository
public class FundDAO {

	@Autowired
	private FundRepository fundRepository;

	public void batchStore(List<Fund> studentList) {
		fundRepository.save(studentList);
	}

	public List<Fund> getStudents() {
		return fundRepository.findAll();
	}

}