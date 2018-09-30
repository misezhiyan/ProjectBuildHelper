package service;

import dao.ProjectComPonentsDao;
import po.Table;

/**
 * @discription
 * @author kimmy
 * @date 2018年9月29日 下午5:10:02
 */
public class ProjectComPonentsService {

	private ProjectComPonentsDao projectComPonentsDao = new ProjectComPonentsDao();

	public Table tableConstruct(String tableName) {

		return projectComPonentsDao.tableConstruct(tableName);
	}
}
