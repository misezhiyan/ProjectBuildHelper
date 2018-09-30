import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import constant.Constant;
import po.Field;
import po.Table;
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

	public static void main(String[] args) throws IOException {

		// 获取表结构
		Table table = projectComPonentsService.tableConstruct("sentense");
		System.out.println(table);

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
	}

	// 读取配置文件 , 创建目标文件
	private static void createFiles(Table table) throws IOException {

		// po 配置
		String poConfigPath = Constant.BUSINESSCONFIGREALPATH + "/poConfig.properties";
		Properties properties_poConfig = new Properties();
		InputStreamReader inStream_poConfig = new InputStreamReader(new FileInputStream(poConfigPath), "UTF-8");
		properties_poConfig.load(inStream_poConfig);
		// mapper 配置
		String mapperConfigPath = Constant.BUSINESSCONFIGREALPATH + "/mapperConfig.properties";
		Properties properties_mapperConfig = new Properties();
		InputStreamReader inStream_mapperConfig = new InputStreamReader(new FileInputStream(mapperConfigPath), "UTF-8");
		properties_mapperConfig.load(inStream_mapperConfig);
		// dao 配置
		String daoConfigPath = Constant.BUSINESSCONFIGREALPATH + "/daoConfig.properties";
		Properties properties_daoConfig = new Properties();
		InputStreamReader inStream_daoConfig = new InputStreamReader(new FileInputStream(daoConfigPath), "UTF-8");
		properties_daoConfig.load(inStream_daoConfig);
		// 创建 po
		createPo(properties_poConfig, table);
		// 创建 mapper
		createMapper(properties_mapperConfig, table);
		// 创建 dao
		createDao(properties_daoConfig, table);

	}

	private static void createMapper(Properties properties, Table table) {

		String mapperContent = getMapperContent(properties, table);
		String pkg = properties.getProperty("package");

		String mapperPath = pkg + "/" + table.getTABLE_NAME() + "Mapper.xml";
		if (!StringUtil.isEmpty(Constant.RESULTFILEPATH))
			mapperPath = (StringUtil.isEmpty(Constant.RESULTFILEPATH) ? Constant.RESULTFILEPATH : "resultData") + "/" + mapperPath;
		try {
			FileUtil.writeIntoFileWithDir(mapperPath, mapperContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getMapperContent(Properties properties, Table table) {

		String TABLE_NAME = table.getTABLE_NAME();
		// String TABLE_COMMENT = table.getTABLE_COMMENT();
		List<Field> fieldList = table.getFieldList();

		String pkg = properties.getProperty("package");

		String spaceLine = "";// 行前空格
		String lineChange = "\r\n";// 换行符
		String mapperContent = "";

		mapperContent += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + lineChange;
		mapperContent += "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">" + lineChange;
		mapperContent += lineChange;
		mapperContent += "<mapper namespace=\"" + pkg + "." + TABLE_NAME + "Mapper\">" + lineChange;
		mapperContent += lineChange;
		mapperContent += "</mapper>" + lineChange;

		return mapperContent;
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

	private static void createPo(Properties properties, Table table) {

		String poContent = getPoContent(properties, table);
		String pkg = properties.getProperty("package");

		String poPath = pkg + "/" + table.getTABLE_NAME() + ".java";
		if (!StringUtil.isEmpty(Constant.RESULTFILEPATH))
			poPath = Constant.RESULTFILEPATH + "/" + poPath;
		try {
			FileUtil.writeIntoFileWithDir(poPath, poContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getPoContent(Properties properties, Table table) {
		String TABLE_NAME = table.getTABLE_NAME();
		String TABLE_COMMENT = table.getTABLE_COMMENT();
		List<Field> fieldList = table.getFieldList();

		String pkg = properties.getProperty("package");
		// String discription = properties.getProperty("discription");
		String author = properties.getProperty("author");

		String spaceLine = "";// 行前空格
		String lineChange = "\r\n";// 换行符
		String poContent = "";
		// 所在包
		poContent += "package " + pkg + ";" + lineChange;
		poContent += lineChange;
		// 引用类
		poContent += "import " + ";";
		poContent += lineChange;
		// 类注释
		poContent += "/**" + lineChange + " * @discription " + TABLE_COMMENT + lineChange + " * @author " + author + lineChange + " * @date " + new Date() + lineChange + " */;" + lineChange;
		// 类声明开始
		poContent += "public class " + TABLE_NAME + " {";
		poContent += lineChange;
		spaceLine += "	";

		// 属性
		String fieldArea = "";
		// 方法区
		String methodArea = "";
		for (Field field : fieldList) {

			// 属性名称
			String COLUMN_NAME = field.getCOLUMN_NAME();
			// 数据类型
			String DATA_TYPE = field.getDATA_TYPE();// int, varchar
			// 数据长度
			Integer CHARACTER_MAXIMUM_LENGTH = field.getCHARACTER_MAXIMUM_LENGTH();
			// 整数位数 - 1
			Integer NUMERIC_PRECISION = field.getNUMERIC_PRECISION();
			// 小数位数
			Integer NUMERIC_SCALE = field.getNUMERIC_SCALE();
			// 字段关联键
			String COLUMN_KEY = field.getCOLUMN_KEY();
			// 自动增长
			String EXTRA = field.getEXTRA();
			// 字段注释
			String COLUMN_COMMENT = field.getCOLUMN_COMMENT();

			// 字段区
			String oneFieldArea = fieldsArea(spaceLine, lineChange, COLUMN_COMMENT, DATA_TYPE, COLUMN_NAME);
			fieldArea += oneFieldArea;
			// 方法区
			String oneMethodArea = methodArea(spaceLine, lineChange, COLUMN_COMMENT, DATA_TYPE, COLUMN_NAME);
			methodArea += oneMethodArea;
		}

		poContent += fieldArea;
		poContent += methodArea;

		// 类声明结束
		poContent += "}";
		return poContent;
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
