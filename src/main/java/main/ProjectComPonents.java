package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import constant.Constant;
import frame.DaoCreateManager;
import frame.MapperCreateManager;
import frame.PoCreateManager;
import po.Table;
import service.ProjectComPonentsService;

/**
 * @discription 创建 Dao, Mapper, Po
 * @author kimmy
 * @date 2018年9月29日 下午3:18:09
 */
public class ProjectComPonents {

	private static PoCreateManager poManager;
	private static MapperCreateManager mapperManager;
	private static DaoCreateManager daoManager;

	private static ProjectComPonentsService projectComPonentsService = new ProjectComPonentsService();

	public static void main(String[] args) throws Exception {

		// 初始化
		init();

		// // 获取表结构
		Map<String, String> params_tableConstruct = new HashMap<String, String>();
		// params_tableConstruct.put("ALL", null);
		// params_tableConstruct.put("tableName", "initialfrom");
		params_tableConstruct.put("ALL", "true");
		List<Table> tableList = projectComPonentsService.tableConstruct(params_tableConstruct);

		// 创建目标文件
		for (Table table : tableList)
			createFiles(table);
	}

	private static void init() throws Exception {

		// 初始化配置
		initConfig();

		// 管理器初始化
		poManager = new PoCreateManager();
		mapperManager = new MapperCreateManager();
		daoManager = new DaoCreateManager();

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

		// 创建 po
		poManager.matchTable(table);
		poManager.createFile();

		// 创建 mapper
		mapperManager.matchTable(table);
		mapperManager.createFile();

		// 创建 dao
		daoManager.matchTable(table);
		daoManager.createFile();
		daoManager.createImplFile();

	}
}
