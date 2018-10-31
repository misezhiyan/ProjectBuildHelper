package service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.ProjectComPonentsDao;
import po.Table;
import util.FileUtil;

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

	public Map<String, String> sqlCreateList(Map<String, List<String>> params_tablecreatesql) {

		Map<String, String> sqlCreateMapAll = new HashMap<String, String>();

		// 获取表名
		List<String> allTables = projectComPonentsDao.allTables();
		for (String table : allTables) {
			Map<String, String> sqlCreateMap = projectComPonentsDao.createSql(table);
			// String table = sqlCreateMap.get("Table");
			String sql = sqlCreateMap.get("Create Table");

			sqlCreateMapAll.put(table, sql);
		}

		return sqlCreateMapAll;
	}

	public void createTables(File[] fileArr) throws Exception {

		List<String> createSqlList = new ArrayList<String>();
		for (File file : fileArr) {
			List<String> oneFileCreateSqlList = oneFileCreateSqlList(file);
			createSqlList.addAll(oneFileCreateSqlList);
		}

		for (String createSql : createSqlList)
			projectComPonentsDao.createOneTable(createSql);
	}

	private List<String> oneFileCreateSqlList(File file) throws Exception {

		List<String> resultList = new ArrayList<String>();

		String content = FileUtil.fileReadToString(file);
		String[] arr = content.split(";");

		for (String sql : arr) {
			resultList.add(sql);
		}

		return resultList;
	}

}
