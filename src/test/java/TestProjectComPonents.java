import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import constant.Constant;
import po.Field;
import po.Table;
import po.Tmpl_mapper;
import service.ProjectComPonentsService;
import util.FileUtil;
import util.StringUtil;

/**
 * @discription 创建 Dao, Mapper, Po
 * @author kimmy
 * @date 2018年9月29日 下午3:18:09
 */
public class TestProjectComPonents {

	private static ProjectComPonentsService projectComPonentsService = new ProjectComPonentsService();

	public static void main(String[] args) throws Exception {

		// 获取表结构
		Table table = projectComPonentsService.tableConstruct("initialfrom");

		// 读取配置文件
		initConfig();
		// 创建目标文件
		createFiles(table);
	}

	private static void initConfig() throws IOException {

		// 业务配置文件路径
		File directory = new File("");// 参数为空, 项目路径
		Constant.PROJECTREALPATH = directory.getCanonicalPath();
		Constant.CONFIGREALPATH = Constant.PROJECTREALPATH + "/src/main/config";
		Constant.BUSINESSCONFIGREALPATH = Constant.CONFIGREALPATH + "/businessConfig";

		// 文件输出配置
		String pathConfig = Constant.CONFIGREALPATH + "/pathConfig.properties";
		Properties properties_pathConfig = new Properties();
		InputStreamReader inStream_pathConfig = new InputStreamReader(new FileInputStream(pathConfig), "UTF-8");
		properties_pathConfig.load(inStream_pathConfig);
		Constant.RESULTFILEPATH = properties_pathConfig.getProperty("RESULTFILEPATH");
		Constant.PROJECTNAME = properties_pathConfig.getProperty("PROJECTNAME");
		Constant.BASEPATH = properties_pathConfig.getProperty("BASEPATH");
	}

	// 读取配置文件 , 创建目标文件
	private static void createFiles(Table table) throws Exception {

		// po 配置
		// 创建 po
		// Tmpl_po po = new Tmpl_po(table);

		// mapper 配置
		// 创建 mapper
		Tmpl_mapper mapper = new Tmpl_mapper(table);

		// dao 配置
		// // 创建 dao
		// createDao(properties_daoConfig, table);

	}

	private static void createDao(Properties properties, Table table) {

		String daoContent = getDaoContent(properties, table);
		String pkg = properties.getProperty("package");

		String daoPath = pkg + "/" + table.getTABLE_NAME() + "Dao.java";
		if (!StringUtil.isEmpty(Constant.RESULTFILEPATH))
			daoPath = Constant.RESULTFILEPATH + "/" + daoPath;
		try {
			FileUtil.writeIntoFileWithDir(daoPath, daoContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getDaoContent(Properties properties, Table table) {

		String TABLE_NAME = table.getTABLE_NAME();
		// String TABLE_COMMENT = table.getTABLE_COMMENT();
		List<Field> fieldList = table.getFieldList();

		String pkg = properties.getProperty("package");

		String spaceLine = "";// 行前空格
		String lineChange = "\r\n";// 换行符
		String daoContent = "";

		return null;
	}

	private static String methodArea(String spaceLine, String lineChange, String COLUMN_COMMENT, String DATA_TYPE, String COLUMN_NAME) {

		String type = "";
		switch (DATA_TYPE) {
		case "int":
			type = "Integer";
			break;
		case "varchar":
			type = "String";
			break;
		}

		String methodName = COLUMN_NAME.substring(0, 1).toUpperCase() + COLUMN_NAME.substring(1).toLowerCase();
		// get方法
		String getMethodContent = "";
		String getMethodName = "get" + methodName;
		getMethodContent += spaceLine + "public " + type + " " + getMethodName + "() {" + lineChange;
		spaceLine += "	";
		getMethodContent += spaceLine + "return " + COLUMN_NAME + lineChange + ";";
		getMethodContent += spaceLine + "}" + lineChange;
		spaceLine = spaceLine.substring(1);
		getMethodContent += lineChange;
		// set方法
		String setMethodContent = "";
		String setMethodName = "set" + methodName;
		setMethodContent += spaceLine + "public " + type + " " + setMethodName + "(" + type + " " + COLUMN_NAME + ") {";
		spaceLine += "	";
		setMethodContent += spaceLine + COLUMN_NAME + " = " + COLUMN_NAME + ";";
		setMethodContent += spaceLine + "}";
		spaceLine = spaceLine.substring(1);
		setMethodContent += lineChange;

		return getMethodContent + setMethodContent;
	}

	private static String fieldsArea(String spaceLine, String lineChange, String COLUMN_COMMENT, String DATA_TYPE, String COLUMN_NAME) {

		String type = "";
		switch (DATA_TYPE) {
		case "int":
			type = "Integer";
			break;
		case "varchar":
			type = "String";
			break;
		}

		// 备注行
		String commentLine = spaceLine + "// " + COLUMN_COMMENT + lineChange;
		// 字段行
		String fieldContent = "";
		fieldContent += spaceLine + "private " + type + " " + COLUMN_NAME + lineChange;

		return commentLine + fieldContent;
	}
}
