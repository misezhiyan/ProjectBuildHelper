package frame;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import constant.Constant;
import po.Table;

public class CreateManager {

	// 表 --提供源数据
	static Table table;

	// po配置
	static Properties poConfig;
	// mapper配置
	static Properties mapperConfig;
	// dao配置
	static Properties daoConfig;

	// String projectBasePath = Constant.BUSINESSCONFIGREALPATH;

	public CreateManager() throws IOException {

		// po 配置
		if (null == poConfig) {

			poConfig = new Properties();
			String poConfigPath = Constant.BUSINESSCONFIGREALPATH + "/poConfig.properties";
			InputStreamReader inStream_po = new InputStreamReader(new FileInputStream(poConfigPath), "UTF-8");
			poConfig.load(inStream_po);
		}

		// mapper 配置
		if (null == mapperConfig) {
			mapperConfig = new Properties();
			String mapperConfigPath = Constant.BUSINESSCONFIGREALPATH + "/mapperConfig.properties";
			InputStreamReader inStream_mapper = new InputStreamReader(new FileInputStream(mapperConfigPath), "UTF-8");
			mapperConfig.load(inStream_mapper);
		}

		// dao 配置
		if (null == daoConfig) {
			daoConfig = new Properties();
			String daoConfigPath = Constant.BUSINESSCONFIGREALPATH + "/daoConfig.properties";
			InputStreamReader inStream_dao = new InputStreamReader(new FileInputStream(daoConfigPath), "UTF-8");
			daoConfig.load(inStream_dao);
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

	String getTmplPath(String type) {

		Properties config = matchConfig(type);
		String tmplpackage = config.getProperty("tmplpackage");
		String tmplfile = config.getProperty("tmplfile");

		String tmplPath = matchPath(tmplfile, Constant.BUSINESSCONFIGREALPATH, tmplpackage);

		return tmplPath;
	}

	String getTmplImplPath(String type) {
		Properties config = matchConfig(type);
		String tmplimplpackage = config.getProperty("tmplimplpackage");
		String tmplimplfile = config.getProperty("tmplimplfile");

		String tmplImplPath = matchPath(tmplimplfile, Constant.BUSINESSCONFIGREALPATH, tmplimplpackage);
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
		}
		return null;
	}

	// 拼接路径
	String matchPath(String fileName, String... pathArr) {

		String path = "";
		for (String path_tmp : pathArr) {
			if (!path.endsWith("/"))
				path = path + "/";
			path += path_tmp;
		}

		path = matchLinePath(path);

		path += "/" + fileName;

		return path;
	}

	// 无头无尾反斜杠路径
	String matchLinePath(String path) {

		path = path.replace(".", "/");
		path = path.replace("\\", "/");

		while (path.startsWith("/"))
			path = path.substring(1);
		while (path.endsWith("/"))
			path = path.substring(0, path.length() - 1);
		while (path.contains("//"))
			path = path.replace("//", "/");

		return path;
	}

	// 无头无尾点路径
	String matchPointPath(String path) {

		path = path.replace("\\", "/");
		while (path.startsWith("/"))
			path = path.substring(1);
		while (path.endsWith("/"))
			path = path.substring(0, path.length() - 1);
		while (path.contains("//"))
			path = path.replace("//", "/");

		path = path.replace("/", ".");

		return path;
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

	// 工程相对路径
	protected String matchProjectRelativePath(String pkg) {

		String path = Constant.BASEPATH + "/" + pkg;
		path = matchLinePath(path);
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

}
