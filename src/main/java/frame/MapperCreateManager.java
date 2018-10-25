package frame;

import java.util.List;

import constant.Constant;
import po.Field;
import po.Table;
import util.FileUtil;

/**
 * @discription
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class MapperCreateManager extends CreateManager {

	private String tmpl;
	private String tmplTmp;

	public MapperCreateManager() throws Exception {
		super();
		if (null == tmpl) {
			String mapperTmplPath = getMapperTmplPath();
			tmpl = FileUtil.fileReadToString(mapperTmplPath);
		}
	}

	public MapperCreateManager(Table table) throws Exception {
		super(table);
		if (null == tmpl) {
			String mapperTmplPath = getMapperTmplPath();
			tmpl = FileUtil.fileReadToString(mapperTmplPath);
		}
	}

	public void matchTable(Table table) {
		super.matchTable(table);
		tmplTmp = tmpl;
	}

	public void createFile() throws Exception {

		String TABLE_NAME = table.getTABLE_NAME();
		tmplTmp = tmplTmp.replace("${tableName}", TABLE_NAME);

		String pkg = getMapperRelativePath();
		tmplTmp = tmplTmp.replace("${package}", matchPointPath(pkg));

		String mapperContent = mapperContent(table);
		tmplTmp = tmplTmp.replace("${mapperContent}", mapperContent);

		// 写入文件
		FileUtil.writeIntoFile(Constant.RESULTFILEPATH + "\\" + Constant.PROJECTNAME + "\\" + matchLinePath(pkg), TABLE_NAME + "Mapper", "xml", tmplTmp);
	}

	private String mapperContent(Table table) {

		String mapperContent = "";

		String TABLE_NAME = table.getTABLE_NAME();
		List<Field> fieldList = table.getFieldList();

		// insert
		String insertArea = insertArea(TABLE_NAME, fieldList);

		mapperContent += insertArea;

		return mapperContent;
	}

	private String insertArea(String TABLE_NAME, List<Field> fieldList) {

		String space = "	";
		String changeLine = "\r\n";

		String insertArea = "";

		String pkg = getMapperRelativePath();
		insertArea += space + "<insert id=\"save\" parameterType=\"" + pkg + "." + TABLE_NAME + "\">" + changeLine;
		insertArea += space + "INSERT INTO " + TABLE_NAME + " (" + changeLine;

		String filedsArea = "";
		String valuesArea = "";
		for (Field field : fieldList) {
			String COLUMN_NAME = field.getCOLUMN_NAME();
			String DATA_TYPE = field.getDATA_TYPE();
			String dbType = matchMapperType(DATA_TYPE);
			filedsArea += space + space + COLUMN_NAME + "," + changeLine;
			valuesArea += space + space + "#{" + COLUMN_NAME + ":" + dbType + "}" + "," + changeLine;
		}
		insertArea += filedsArea.substring(0, filedsArea.length() - 3) + changeLine;
		insertArea += space + ")VALUES (" + changeLine;

		insertArea += valuesArea.substring(0, valuesArea.length() - 3) + changeLine;
		insertArea += space + ")" + changeLine;

		insertArea += space + "</insert>";

		return insertArea;
	}

	private String matchMapperType(String DATA_TYPE) {

		String result = "";
		switch (DATA_TYPE) {
		case "varchar":
			result = "VARCHAR";
			break;
		case "int":
			result = "INTEGER";
			break;
		case "datetime":
			result = "TIMESTAMP";
			break;
		}

		return result;
	}

}
