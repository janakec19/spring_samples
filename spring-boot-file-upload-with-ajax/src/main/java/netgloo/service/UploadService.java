package netgloo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import netgloo.bean.Fund;
import netgloo.dao.FundDAO;

@Service
public class UploadService {

	@Autowired
	private FundDAO fundDAO;

	public List<Fund> uploadFile(MultipartFile multipartFile) throws IOException {

		File file = convertMultiPartToFile(multipartFile);

		List<Fund> mandatoryMissedList = new ArrayList<Fund>();

		try (Reader reader = new FileReader(file);) {
			@SuppressWarnings("unchecked")
			CsvToBean<Fund> csvToBean = new CsvToBeanBuilder<Fund>(reader).withType(Fund.class)
					.withIgnoreLeadingWhiteSpace(true).build();
			List<Fund> studentList = csvToBean.parse();

			Iterator<Fund> fundListClone = studentList.iterator();

			while (fundListClone.hasNext()) {

				Fund fund = fundListClone.next();

				if (fund.getFundName() == null || fund.getFundName().isEmpty() || fund.getPrice() == null
						|| fund.getPrice().isEmpty()) {
					mandatoryMissedList.add(fund);
					fundListClone.remove();
				}
			}

			fundDAO.batchStore(studentList);
		}
		return fundDAO.getStudents();
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public List<Fund> getStudents() {
		return fundDAO.getStudents();
	}

	public List<Fund> getFundByName(String name) {
		List<Fund> funds = fundDAO.getStudents();
		List<Fund> filtered = funds.stream().filter(x -> name.equals(x.getFundName())).collect(Collectors.toList());
		return filtered;
	}
}