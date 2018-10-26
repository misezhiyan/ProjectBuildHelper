package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import frame.tableconstruct.tableconstructCommon;
import frame.tableconstruct.tableconstructExcutor;
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
		// String tablecreatesql = businessConfig.getProperty("tablecreatesql");
		// if (tablecreatesql == "true")
		// createTABLESql();

	}

	// private static void createTABLESql() {
	// List<String> createSqlList = projectComPonentsService.createSqlList(params_tableConstruct);
	//
	// }

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
