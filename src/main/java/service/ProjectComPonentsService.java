package service;

import java.util.List;
import java.util.Map;

import dao.ProjectComPonentsDao;
import po.Table;

/**
 * @discription
 * @author kimmy
 * @date 2018年9月29日 下午5:10:02
 */
public class ProjectComPonentsService {

	private ProjectComPonentsDao projectComPonentsDao = new ProjectComPonentsDao();

	public List<Table> tableConstruct(Map<String, String> params_tableConstruct) {

		return projectComPonentsDao.tableConstruct(params_tableConstruct);
	}

}
