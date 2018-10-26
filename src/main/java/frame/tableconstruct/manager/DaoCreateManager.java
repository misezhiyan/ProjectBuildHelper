package frame.tableconstruct.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import frame.tableconstruct.tableconstructCommon;
import po.Field;
import po.Table;
import util.FileUtil;
import util.PathUtil;

/**
 * @discription dao 模板配置
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class DaoCreateManager extends CreateManager {

	private String tmpl;
	private String tmplImpl;
	private String tmplTmp;
	private String tmplImplTmp;

	public DaoCreateManager() throws Exception {
		super();
		if (null == tmpl) {
			String daoTmplPath = getDaoTmplPath();
			tmpl = FileUtil.fileReadToString(daoTmplPath);
			String daoTmplImplPath = getDaoTmplImplPath();
			tmplImpl = FileUtil.fileReadToString(daoTmplImplPath);
		}
	}

	public DaoCreateManager(Table table) throws Exception {
		super(table);
		if (null == tmpl) {
			String daoTmplPath = getDaoTmplPath();
			tmpl = FileUtil.fileReadToString(daoTmplPath);
			String daoTmplImplPath = getDaoTmplImplPath();
			tmplImpl = FileUtil.fileReadToString(daoTmplImplPath);
		}
	}

	public void matchTable(Table table) {
		super.matchTable(table);
		tmplTmp = tmpl;
		tmplImplTmp = tmplImpl;
	}

	public void createFile() throws Exception {

		String TABLE_NAME = table.getTABLE_NAME();
		tmplTmp = tmplTmp.replace("${tableName}", TABLE_NAME);

		String CLASS_NAME = getCLASS_NAME();
		tmplTmp = tmplTmp.replace("${className}", CLASS_NAME);

		String pkg = getDaoRelativePath();
		tmplTmp = tmplTmp.replace("${package}", PathUtil.matchPointPath(pkg));

		Set<String> importList = daoImportList(table);
		String importArea = formmatImportArea(importList);
		tmplTmp = tmplTmp.replace("${import}", importArea);

		// 主键名称
		Field keyField = priKey(table);
		String keyFieldName = keyField.getCOLUMN_NAME();
		String keyFieldType = keyField.getDATA_TYPE();
		keyFieldType = matchJAVAType(keyFieldType);
		tmplTmp = tmplTmp.replace("${keyFieldName}", keyFieldName);
		tmplTmp = tmplTmp.replace("${keyFieldType}", keyFieldType);

		// 写入文件
		String outFilePath = getOutFilePath(pkg);
		FileUtil.writeIntoFile(outFilePath, CLASS_NAME + "Dao", "java", tmplTmp);
	}

	public void createImplFile() throws Exception {

		String TABLE_NAME = table.getTABLE_NAME();
		tmplImplTmp = tmplImplTmp.replace("${tableName}", TABLE_NAME);

		String CLASS_NAME = getCLASS_NAME();
		tmplImplTmp = tmplImplTmp.replace("${className}", CLASS_NAME);

		String pkg = getDaoImplRelativePath();
		tmplImplTmp = tmplImplTmp.replace("${package}", PathUtil.matchPointPath(pkg));

		String mapperpackage = getMapperRelativePath();
		tmplImplTmp = tmplImplTmp.replace("${mapperpackage}", PathUtil.matchPointPath(mapperpackage));

		Set<String> importList = daoImplImportList(table);
		String importArea = formmatImportArea(importList);
		tmplImplTmp = tmplImplTmp.replace("${import}", importArea);

		// 主键名称
		Field keyField = priKey(table);
		String keyFieldName = keyField.getCOLUMN_NAME();
		String keyFieldType = keyField.getDATA_TYPE();
		keyFieldType = matchJAVAType(keyFieldType);
		tmplImplTmp = tmplImplTmp.replace("${keyFieldName}", keyFieldName);
		tmplImplTmp = tmplImplTmp.replace("${keyFieldType}", keyFieldType);

		// 写入文件
		String outFilePath = getOutFilePath(pkg);
		FileUtil.writeIntoFile(outFilePath, CLASS_NAME + "DaoImpl", "java", tmplImplTmp);

	}

	private Field priKey(Table table) {

		List<Field> fieldList = table.getFieldList();
		for (Field field : fieldList) {

			String COLUMN_KEY = field.getCOLUMN_KEY();
			if (COLUMN_KEY.equals("PRI"))
				return field;
		}
		return null;
	}

	private Set<String> daoImportList(Table table) {
		Set<String> importList = importList(table);
		// 固定引入
		String listImport = "import java.util.List;";
		importList.add(listImport);
		return importList;
	}

	private Set<String> daoImplImportList(Table table) {
		Set<String> importList = importList(table);

		Properties config = tableconstructCommon.config;
		String BASEPATH = config.getProperty("BASEPATH");

		// 固定引入
		String listImport = "import java.util.List;";
		importList.add(listImport);
		String daoImport = "import " + PathUtil.matchPointPath(BASEPATH + "." + daoConfig.getProperty("package")) + "." + getCLASS_NAME() + "Dao;";
		importList.add(daoImport);
		String queryDaoImport = "import " + PathUtil.matchPointPath(BASEPATH + "." + daoConfig.getProperty("basedaopackage")) + "." + "QueryDao;";
		importList.add(queryDaoImport);
		String updateDaoImport = "import " + PathUtil.matchPointPath(BASEPATH + "." + daoConfig.getProperty("basedaopackage")) + "." + "UpdateDao;";
		importList.add(updateDaoImport);

		return importList;
	}

	private Set<String> importList(Table table) {

		Set<String> importList = new HashSet<String>();

		// 表类引入
		// String TABLE_NAME = table.getTABLE_NAME();
		String poPath = getPoRelativePath();
		String tableImport = "import " + PathUtil.matchPointPath(poPath) + "." + getCLASS_NAME() + ";";

		importList.add(tableImport);

		return importList;
	}

}
