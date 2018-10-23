package po;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import constant.Constant;
import util.FileUtil;

/**
 * @discription po 模板配置
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class Tmpl_po {
	private String tmpl;
	private String poConfigPath = Constant.BUSINESSCONFIGREALPATH + "/poConfig.properties";
	private String poTmplPath = Constant.BUSINESSCONFIGREALPATH + "/tmpl/po.tmpl";

	private Properties config;

	// 引用
	private List<String> importList;

	public Tmpl_po() throws Exception {

		config = new Properties();
		InputStreamReader inStream = new InputStreamReader(new FileInputStream(poConfigPath), "UTF-8");
		config.load(inStream);

		tmpl = FileUtil.fileReadToString(poTmplPath);

	}

	public Tmpl_po(Table table) throws Exception {
		this();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		tmpl = tmpl.replace("${author}", "kimmy");

		String TABLE_COMMENT = table.getTABLE_COMMENT();
		String TABLE_NAME = table.getTABLE_NAME();

		tmpl = tmpl.replace("${discription}", TABLE_COMMENT);
		tmpl = tmpl.replace("${date}", sdf.format(new Date()));
		tmpl = tmpl.replace("${tableName}", TABLE_NAME);

		List<Field> fieldList = table.getFieldList();
		String filedArea = filedArea(fieldList);

		tmpl = tmpl.replace("${filedArea}", filedArea);

		String importArea = importArea();
		tmpl = tmpl.replace("${import}", importArea);

		String pkg = config.getProperty("package");
		tmpl = tmpl.replace("${package}", pkg);

		String projectBasePath = Constant.RESULTFILEPATH + "\\" + Constant.PROJECTNAME + "\\" + Constant.BASEPATH;

		// 写入文件
		FileUtil.writeIntoFile(projectBasePath + "\\" + pkg.replace(".", "\\"), TABLE_NAME, "java", tmpl);
	}

	private String importArea() {

		String importArea = "";
		for (String imp : importList) {
			importArea += imp;
		}

		return importArea;
	}

	private String filedArea(List<Field> fieldList) {

		// 字段区域
		String oneFiledArea = oneFiledArea();
		String FiledArea = "";
		// 方法区域
		String oneFiledMethodArea = oneFiledMethodArea();
		String FiledMethodArea = "";

		for (Field field : fieldList) {
			String COLUMN_COMMENT = field.getCOLUMN_COMMENT();
			String DATA_TYPE = field.getDATA_TYPE();
			String COLUMN_NAME = field.getCOLUMN_NAME();

			// 字段区域
			String oneFiledAreaTemp = oneFiledArea;
			oneFiledAreaTemp = oneFiledAreaTemp.replace("${fieldComment}", COLUMN_COMMENT);

			String fieldType = matchDBType(DATA_TYPE);
			oneFiledAreaTemp = oneFiledAreaTemp.replace("${fieldType}", fieldType);
			oneFiledAreaTemp = oneFiledAreaTemp.replace("${fieldName}", COLUMN_NAME);

			// 方法区域
			String oneFiledMethodAreaTemp = oneFiledMethodArea;
			oneFiledMethodAreaTemp = oneFiledMethodAreaTemp.replace("${fieldType}", fieldType);

			String methodName = COLUMN_NAME.substring(0, 1).toUpperCase() + COLUMN_NAME.substring(1);
			oneFiledMethodAreaTemp = oneFiledMethodAreaTemp.replace("${fieldMethodName}", methodName);
			oneFiledMethodAreaTemp = oneFiledMethodAreaTemp.replace("${fieldName}", COLUMN_NAME);

			FiledArea += oneFiledAreaTemp;
			FiledMethodArea += oneFiledMethodAreaTemp;
		}
		// 总区域
		String filedArea = FiledArea + "\r\n" + FiledMethodArea;

		return filedArea;
	}

	private String oneFiledArea() {

		String oneFiledArea = "";

		String changeLine = "\r\n";
		String space = "		";

		oneFiledArea += space + "// ${fieldComment}" + changeLine;
		oneFiledArea += space + "private ${fieldType} ${fieldName};" + changeLine;

		return oneFiledArea;
	}

	private String oneFiledMethodArea() {

		String oneFiledMethodArea = "";

		String changeLine = "\r\n";
		String space = "		";

		oneFiledMethodArea += space + "public ${fieldType} get${fieldMethodName}() {" + changeLine;
		oneFiledMethodArea += space + "	return ${fieldName};" + changeLine;
		oneFiledMethodArea += space + "}" + changeLine;

		oneFiledMethodArea += changeLine;

		oneFiledMethodArea += space + "public void set${fieldMethodName}(${fieldType} ${fieldName}) {" + changeLine;
		oneFiledMethodArea += space + "	${fieldName} = ${fieldName};" + changeLine;
		oneFiledMethodArea += space + "}" + changeLine;

		oneFiledMethodArea += changeLine;

		return oneFiledMethodArea;
	}

	private String matchDBType(String DATA_TYPE) {

		String result = "";

		switch (DATA_TYPE) {
		case "varchar":
			result = "String";
			break;
		case "int":
			result = "Integer";
			break;
		case "datetime":
			addImport("java.util.Date");
			result = "Date";
			break;
		}

		return result;
	}

	private void addImport(String classType) {
		if (null == importList)
			importList = new ArrayList<String>();

		String importLine = "import " + classType.trim() + ";\r\n";
		importList.add(importLine);
	}
}
