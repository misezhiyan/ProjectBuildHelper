package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import frame.session.SessionConfig;
import frame.tableconstruct.tableconstructCommon;
import frame.tableconstruct.tableconstructExcutor;
import frame.tablecreate.tablecreateCommon;
import frame.tablecreate.tablecreateExcutor;
import frame.tablecreatesql.tablecreatesqlCommon;
import frame.tablecreatesql.tablecreatesqlExcutor;
import util.PathUtil;

/**
 * @discription 创建 Dao, Mapper, Po
 * @author kimmy
 * @date 2018年9月29日 下午3:18:09
 */
public class ProjectComPonents {

	// 业务处理配置
	private static Properties businessConfig;

	public static void main(String[] args) throws Exception {

		// 初始化
		init();

		// 1.创建 java 结构
		String tableconstruct = businessConfig.getProperty("tableconstruct");
		if ("true".equals(tableconstruct)) {
			tableconstructCommon.BusinessName = "tableconstruct";
			tableconstructExcutor.createJAVAFiles();
		}

		// 2.保存表创建 sql
		String tablecreatesql = businessConfig.getProperty("tablecreatesql");
		if ("true".equals(tablecreatesql)) {
			tablecreatesqlCommon.BusinessName = "tablecreatesql";
			tablecreatesqlExcutor.createTABLESqlCreateFile();
		}

		// 3.表创建
		String tablecreate = businessConfig.getProperty("tablecreate");
		if ("true".equals(tablecreate)) {
			tablecreateCommon.BusinessName = "tablecreate";
			tablecreateExcutor.createTable();
		}

	}

	private static void createTABLESql() throws IOException {

	}

	private static void init() throws Exception {

		// 初始化架构
		initConstruct();

		// 初始化配置
		initConfig();

	}

	private static void initConstruct() {
		// 只有构造方法才能加载静态代码块
		new PathUtil();
	}

	private static void initConfig() throws IOException {

		// 业务配置文件路径
		// File directory = new File("");// 参数为空, 项目路径
		// Constant.PROJECTREALPATH = directory.getCanonicalPath();
		// Constant.CONFIGREALPATH = Constant.PROJECTREALPATH + "/src/main/config";
		// Constant.BUSINESSCONFIGREALPATH = Constant.CONFIGREALPATH + "/businessConfig";

		// 执行业务配置
		businessConfig = new Properties();
		String businessConfigPath = PathUtil.LOCAL_PATH + "/businessConfig.properties";
		businessConfig.load(new InputStreamReader(new FileInputStream(businessConfigPath), "UTF-8"));
		// 通用设置
		SessionConfig.FILE_ENCODE_IN = businessConfig.getProperty("fileInCode");

		// // 文件输出配置
		// String pathConfig = Constant.CONFIGREALPATH + "/pathConfig.properties";
		// Properties properties_pathConfig = new Properties();
		// InputStreamReader inStream_pathConfig = new InputStreamReader(new FileInputStream(pathConfig), "UTF-8");
		// properties_pathConfig.load(inStream_pathConfig);
		// Constant.RESULTFILEPATH = properties_pathConfig.getProperty("RESULTFILEPATH");
		// Constant.PROJECTNAME = properties_pathConfig.getProperty("PROJECTNAME");
		// Constant.BASEPATH = properties_pathConfig.getProperty("BASEPATH");
	}

}
