package frame.tableconstruct.manager;

import java.util.List;

import po.Field;
import po.Table;
import util.FileUtil;
import util.PathUtil;

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
		tmplTmp = tmplTmp.replace("${package}", PathUtil.matchPointPath(pkg));

		String mapperContent = mapperContent(table);
		tmplTmp = tmplTmp.replace("${mapperContent}", mapperContent);

		// 写入文件
		String outFilePath = getOutFilePath(pkg);
		FileUtil.writeIntoFile(outFilePath, TABLE_NAME + "Mapper", "xml", tmplTmp);

	}

	private String mapperContent(Table table) {

		String mapperContent = "";

		String TABLE_NAME = table.getTABLE_NAME();
		List<Field> fieldList = table.getFieldList();

		// select
		String selectArea = selectArea(TABLE_NAME, fieldList);
		// update
		String updateArea = updateArea(TABLE_NAME, fieldList);
		// insert
		String insertArea = insertArea(TABLE_NAME, fieldList);

		mapperContent += selectArea + "\r\n";
		mapperContent += updateArea + "\r\n";
		mapperContent += insertArea + "\r\n";

		return mapperContent;
	}

	private String selectArea(String TABLE_NAME, List<Field> fieldList) {

		Field keyField = getKeyField(fieldList);

		String selectArea = "";
		if (null != keyField)
			selectArea += selectByKeyArea(TABLE_NAME, keyField, fieldList);
		return selectArea;
	}

	private String selectByKeyArea(String TABLE_NAME, Field keyField, List<Field> fieldList) {

		String space = "	";
		String changeLine = "\r\n";

		String selectByKeyArea = "";

		String keyFieldName = keyField.getCOLUMN_NAME();
		String CLASS_NAME = getCLASS_NAME();

		String poPkg = getPoRelativePath();
		selectByKeyArea += space + "<select id=\"selectBy" + firstUpper(keyFieldName) + "\" parameterType=\"" + PathUtil.matchPointPath(poPkg) + "." + CLASS_NAME + "\" resultType=\"" + PathUtil.matchPointPath(poPkg) + "." + CLASS_NAME + "\">" + changeLine;
		selectByKeyArea += space + "SELECT " + changeLine;

		String fieldArea = "";
		for (Field field : fieldList) {
			String COLUMN_NAME = field.getCOLUMN_NAME();
			fieldArea += space + space + COLUMN_NAME + "," + changeLine;
		}
		fieldArea = fieldArea.substring(0, fieldArea.length() - 3);

		selectByKeyArea += fieldArea;
		selectByKeyArea += changeLine;
		selectByKeyArea += space + "WHERE " + keyFieldName + " = #{" + keyFieldName + "}" + changeLine;
		selectByKeyArea += space + "</select>" + changeLine;

		return selectByKeyArea;
	}

	private String updateArea(String TABLE_NAME, List<Field> fieldList) {

		Field keyField = getKeyField(fieldList);
		if (null != keyField)
			return updateByKeyArea(TABLE_NAME, keyField, fieldList);
		return "";
	}

	private String updateByKeyArea(String TABLE_NAME, Field keyField, List<Field> fieldList) {

		String space = "	";
		String changeLine = "\r\n";

		String updateByKeyArea = "";

		String keyFieldName = keyField.getCOLUMN_NAME();
		String CLASS_NAME = getCLASS_NAME();

		String poPkg = getPoRelativePath();
		updateByKeyArea += space + "<update id=\"updateBy" + firstUpper(keyFieldName) + "\" parameterType=\"" + PathUtil.matchPointPath(poPkg) + "." + CLASS_NAME + "\">" + changeLine;
		updateByKeyArea += space + "UPDATE " + TABLE_NAME + changeLine;
		updateByKeyArea += space + "SET " + changeLine;

		String fieldArea = "";
		for (Field field : fieldList) {
			String COLUMN_NAME = field.getCOLUMN_NAME();
			String DATA_TYPE = field.getDATA_TYPE();
			DATA_TYPE = matchMapperType(DATA_TYPE);
			fieldArea += space + space + COLUMN_NAME + " = #{" + COLUMN_NAME + ":" + DATA_TYPE + "}," + changeLine;
		}

		fieldArea = fieldArea.substring(0, fieldArea.length() - 3);
		updateByKeyArea += fieldArea;
		updateByKeyArea += changeLine;
		updateByKeyArea += space + "</update>" + changeLine;

		return updateByKeyArea;
	}

	private String insertArea(String TABLE_NAME, List<Field> fieldList) {

		String space = "	";
		String changeLine = "\r\n";

		String insertArea = "";

		String poPkg = getPoRelativePath();
		String CLASS_NAME = getCLASS_NAME();

		insertArea += space + "<insert id=\"save\" parameterType=\"" + PathUtil.matchPointPath(poPkg) + "." + CLASS_NAME + "\">" + changeLine;
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

	private Field getKeyField(List<Field> fieldList) {

		for (Field field : fieldList) {

			String COLUMN_KEY = field.getCOLUMN_KEY();
			if ("PRI".equals(COLUMN_KEY))
				return field;
		}

		return null;
	}

	private String firstUpper(String str) {

		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
