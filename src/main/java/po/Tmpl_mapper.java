package po;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import constant.Constant;
import util.FileUtil;

/**
 * @discription
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class Tmpl_mapper {

	private String tmpl;
	private String mapperConfigPath = Constant.BUSINESSCONFIGREALPATH + "/mapperConfig.properties";
	private String mapperTmplPath = Constant.BUSINESSCONFIGREALPATH + "/tmpl/mapper.tmpl";

	private Properties config;

	public Tmpl_mapper() throws Exception {

		config = new Properties();
		InputStreamReader inStream = new InputStreamReader(new FileInputStream(mapperConfigPath), "UTF-8");
		config.load(inStream);

		tmpl = FileUtil.fileReadToString(mapperTmplPath);
	}

	public Tmpl_mapper(Table table) throws Exception {
		this();

		String TABLE_NAME = table.getTABLE_NAME();
		tmpl = tmpl.replace("${tableName}", TABLE_NAME);

		String pkg = config.getProperty("package");
		tmpl = tmpl.replace("${package}", pkg);

		String mapperContent = mapperContent(table);
		tmpl = tmpl.replace("${mapperContent}", mapperContent);

		String projectBasePath = Constant.RESULTFILEPATH + "\\" + Constant.PROJECTNAME + "\\" + Constant.BASEPATH;
		// 写入文件
		FileUtil.writeIntoFile(projectBasePath + "\\" + pkg.replace(".", "\\"), TABLE_NAME + "Mapper", "xml", tmpl);
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

		String pkg = config.getProperty("package");
		insertArea += space + "<insert id=\"save\" parameterType=\"" + pkg + "." + TABLE_NAME + "\">" + changeLine;
		insertArea += space + "INSERT INTO " + TABLE_NAME + " (" + changeLine;

		String filedsArea = "";
		String valuesArea = "";
		for (Field field : fieldList) {
			String COLUMN_NAME = field.getCOLUMN_NAME();
			String DATA_TYPE = field.getDATA_TYPE();
			String dbType = dbType(DATA_TYPE);
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

	private String dbType(String DATA_TYPE) {

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
