package frame;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import constant.Constant;
import po.Field;
import po.Table;
import util.FileUtil;

/**
 * @discription dao 模板配置
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class DaoCreateManager extends CreateManager {

	private String tmpl;
	private String tmplTmp;

	public DaoCreateManager() throws Exception {
		super();
		if (null == tmpl) {
			String daoTmplPath = getDaoTmplPath();
			tmpl = FileUtil.fileReadToString(daoTmplPath);
		}
	}

	public DaoCreateManager(Table table) throws Exception {
		super(table);
		if (null == tmpl) {
			String daoTmplPath = getDaoTmplPath();
			tmpl = FileUtil.fileReadToString(daoTmplPath);
		}
	}

	public void matchTable(Table table) {
		super.matchTable(table);
		tmplTmp = tmpl;
	}

	public void createFile() throws Exception {

		String TABLE_NAME = table.getTABLE_NAME();
		tmplTmp = tmplTmp.replace("${tableName}", TABLE_NAME);

		String CLASS_NAME = getCLASS_NAME();
		tmplTmp = tmplTmp.replace("${className}", CLASS_NAME);

		String pkg = getDaoRelativePath();
		tmplTmp = tmplTmp.replace("${package}", matchPointPath(pkg));

		Set<String> importList = importList(table);
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
		FileUtil.writeIntoFile(Constant.RESULTFILEPATH + "\\" + Constant.PROJECTNAME + "\\" + pkg, TABLE_NAME + "Dao", "java", tmplTmp);
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

	private Set<String> importList(Table table) {

		Set<String> importList = new HashSet<String>();

		// 固定引入
		String listImport = "import java.util.List;";
		// 表类引入
		// String TABLE_NAME = table.getTABLE_NAME();
		String poPath = getPoRelativePath();
		String tableImport = "import " + matchPointPath(poPath) + "." + getCLASS_NAME() + ";";

		importList.add(listImport);
		importList.add(tableImport);

		return importList;
	}

}
