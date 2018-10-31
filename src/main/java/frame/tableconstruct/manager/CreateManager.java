package frame.tableconstruct.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import frame.tableconstruct.tableconstructCommon;
import po.Table;
import util.PathUtil;

public class CreateManager {

	// 表 --提供源数据
	static Table table;

	// po配置
	static Properties poConfig;
	// mapper配置
	static Properties mapperConfig;
	// dao配置
	static Properties daoConfig;
	// dao配置
	static Properties resConfig;

	// String projectBasePath = Constant.BUSINESSCONFIGREALPATH;

	public CreateManager() throws IOException {

		Properties config = tableconstructCommon.config;

		// po 配置
		if (null == poConfig) {
			String poConfigFile = config.getProperty("poConfigFile");
			String poConfigPath = PathUtil.businessConfigPath(tableconstructCommon.BusinessName) + "/" + poConfigFile;
			poConfig = new Properties();
			InputStreamReader inStream_po = new InputStreamReader(new FileInputStream(poConfigPath), "UTF-8");
			poConfig.load(inStream_po);
		}

		// mapper 配置
		if (null == mapperConfig) {
			String mapperConfigFile = config.getProperty("mapperConfigFile");
			String mapperConfigPath = PathUtil.businessConfigPath(tableconstructCommon.BusinessName) + "/" + mapperConfigFile;
			mapperConfig = new Properties();
			InputStreamReader inStream_mapper = new InputStreamReader(new FileInputStream(mapperConfigPath), "UTF-8");
			mapperConfig.load(inStream_mapper);
		}

		// dao 配置
		if (null == daoConfig) {
			String daoConfigFile = config.getProperty("daoConfigFile");
			String daoConfigPath = PathUtil.businessConfigPath(tableconstructCommon.BusinessName) + "/" + daoConfigFile;
			daoConfig = new Properties();
			InputStreamReader inStream_dao = new InputStreamReader(new FileInputStream(daoConfigPath), "UTF-8");
			daoConfig.load(inStream_dao);
		}

		// res 配置
		if (null == resConfig) {
			String resConfigFile = config.getProperty("resConfigFile");
			String resConfigPath = PathUtil.businessConfigPath(tableconstructCommon.BusinessName) + "/" + resConfigFile;
			resConfig = new Properties();
			InputStreamReader inStream_dao = new InputStreamReader(new FileInputStream(resConfigPath), "UTF-8");
			resConfig.load(inStream_dao);
		}

	}

	public CreateManager(Table table) throws IOException {
		this();

		this.table = table;
	}

	// 通过表名转换类名
	public String getCLASS_NAME() {
		String TABLE_NAME = table.getTABLE_NAME();
		String CLASS_NAME = TABLE_NAME.substring(0, 1).toUpperCase() + TABLE_NAME.substring(1);

		return CLASS_NAME;
	}

	// po 模板路径
	String getPoTmplPath() {

		return getTmplPath("PO");
	}

	// mapper 模板路径
	String getMapperTmplPath() {

		return getTmplPath("MAPPER");
	}

	// dao 模板路径
	String getDaoTmplPath() {

		return getTmplPath("DAO");
	}

	// daoImpl 模板路径
	String getDaoTmplImplPath() {

		return getTmplImplPath("DAO");
	}

	// daoImpl 模板路径
	String getResTmplPath() {

		return getTmplPath("RES");
	}

	String getTmplPath(String type) {

		// 模板配置
		Properties tmplConfig = matchConfig(type);
		String tmplpackage = tmplConfig.getProperty("tmplpackage");
		String tmplfile = tmplConfig.getProperty("tmplfile");

		String tmplPath = PathUtil.matchPath(tmplfile, PathUtil.businessConfigPath(tableconstructCommon.BusinessName), tmplpackage);

		return tmplPath;
	}

	String getTmplImplPath(String type) {
		Properties config = matchConfig(type);
		String tmplimplpackage = config.getProperty("tmplimplpackage");
		String tmplimplfile = config.getProperty("tmplimplfile");

		String tmplImplPath = PathUtil.matchPath(tmplimplfile, PathUtil.businessConfigPath(tableconstructCommon.BusinessName), tmplimplpackage);
		return tmplImplPath;
	}

	Properties matchConfig(String type) {

		switch (type) {
		case "PO":
			return poConfig;
		case "MAPPER":
			return mapperConfig;
		case "DAO":
			return daoConfig;
		case "RES":
			return resConfig;
		}
		return null;
	}

	// 重置表
	public void matchTable(Table table) {

		this.table = table;
	}

	// 获取po类路径
	String getPoRelativePath() {

		String pkg = poConfig.getProperty("package");
		return matchProjectRelativePath(pkg);
	}

	// 获取mapper类路径
	String getMapperRelativePath() {

		String pkg = mapperConfig.getProperty("package");
		return matchProjectRelativePath(pkg);
	}

	// 获取dao类路径
	String getDaoRelativePath() {

		String pkg = daoConfig.getProperty("package");
		return matchProjectRelativePath(pkg);
	}

	// 获取daoImpl类路径
	String getDaoImplRelativePath() {

		String pkg = daoConfig.getProperty("implpackage");
		return matchProjectRelativePath(pkg);
	}

	// 获取剩余配置路径
	String getResRelativePath() {

		String pkg = resConfig.getProperty("package");
		return matchProjectRelativePath(pkg);
	}

	// 工程相对路径
	protected String matchProjectRelativePath(String pkg) {

		Properties config = tableconstructCommon.config;
		String BASEPATH = config.getProperty("BASEPATH");

		String path = PathUtil.matchLinePath(BASEPATH + "/" + pkg);
		return path;
	}

	// 数据库类型转 java类型
	String matchJAVAType(String keyFieldType) {
		String javaType = keyFieldType;
		switch (keyFieldType) {
		case "int":
			javaType = "Integer";
			break;
		case "varchar":
			javaType = "String";
			break;
		case "datetime":
			javaType = "Date";
			break;
		}

		return javaType;
	}

	// 默认补全java类型
	String matchFullJAVAType(String shortJavaType) {
		String fullJavaType = shortJavaType;
		switch (shortJavaType) {
		case "Integer":
			fullJavaType = "java.lang.Integer";
			break;
		case "String":
			fullJavaType = "java.lang.String";
			break;
		case "Date":
			fullJavaType = "java.util.Date";
			break;
		}

		return fullJavaType;
	}

	// 转换mapper类型
	protected String matchMapperType(String DATA_TYPE) {

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

	String formmatImportArea(Set<String> set) {

		List<String> importList = new ArrayList<String>(set);

		Collections.sort(importList, (p1, p2) -> p1.compareTo(p2));

		String importArea = "";
		String javaArea = "";
		String notJavaArea = "";
		for (String importOne : importList) {

			String importTmp = importOne.replaceFirst("import", "");
			importTmp = importTmp.trim();

			if (importTmp.startsWith("java.lang"))
				continue;

			if (importTmp.startsWith("java"))
				javaArea += importOne + "\r\n";
			else
				notJavaArea += importOne + "\r\n";
		}

		importArea += javaArea + "\r\n";
		importArea += notJavaArea;

		return importArea;
	}

	String getOutFilePath(String pkg) {

		Properties config = tableconstructCommon.config;
		String RESULTFILEPATH = config.getProperty("RESULTFILEPATH");
		String PROJECTNAME = config.getProperty("PROJECTNAME");
		String outFilePath = PathUtil.matchLinePath(RESULTFILEPATH + "\\" + PROJECTNAME + "\\" + pkg);

		return outFilePath;
	}
}
