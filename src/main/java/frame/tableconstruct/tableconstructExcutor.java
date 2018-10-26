package frame.tableconstruct;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import frame.excutor.Excutor;
import frame.tableconstruct.manager.DaoCreateManager;
import frame.tableconstruct.manager.MapperCreateManager;
import frame.tableconstruct.manager.PoCreateManager;
import po.Table;
import service.ProjectComPonentsService;
import util.PathUtil;

/**
 * @discription
 * @author kimmy
 * @date 2018年10月26日 上午9:53:05
 */
public class tableconstructExcutor extends Excutor {

	// 创建文件管理器
	private static PoCreateManager poManager;
	private static MapperCreateManager mapperManager;
	private static DaoCreateManager daoManager;

	private static ProjectComPonentsService projectComPonentsService = new ProjectComPonentsService();

	public static void createJAVAFiles() throws Exception {

		// 初始化
		init();

		// 获取表结构
		Map<String, String> params_tableConstruct = new HashMap<String, String>();

		Properties config = tableconstructCommon.config;
		String TABLENEEDDEAL = config.getProperty("TABLENEEDDEAL");
		if ("ALL".equals(TABLENEEDDEAL))
			params_tableConstruct.put("ALL", "true");
		else if ("MUL".equals(TABLENEEDDEAL)) {
			String TABLENAMELIST = config.getProperty("TABLENAMELIST");
			TABLENAMELIST = conbineTableParam(TABLENAMELIST);
			params_tableConstruct.put("tableName", "initialfrom");
		} else {
			return;
		}

		List<Table> tableList = projectComPonentsService.tableConstruct(params_tableConstruct);

		// 创建目标文件
		for (Table table : tableList)
			createFiles(table);
	}

	private static String conbineTableParam(String TABLENAMELIST) {

		String[] TABLENAME_ARR = TABLENAMELIST.split(",");

		return surroundSingleQuotation(TABLENAME_ARR);

	}

	private static String surroundSingleQuotation(String[] TABLENAME_ARR) {

		String result = "";
		for (String TABLENAME : TABLENAME_ARR) {
			TABLENAME = TABLENAME.trim();
			if (!TABLENAME.startsWith("\'"))
				TABLENAME = "\'" + TABLENAME;
			if (!TABLENAME.endsWith("\'"))
				TABLENAME = "\'" + TABLENAME;

			result += TABLENAME + ",";
		}

		result = result.substring(0, result.length() - 1);

		return result;
	}

	// 初始化
	private static void init() throws Exception {

		// 业务配置
		if (null == tableconstructCommon.config) {
			String configPath = PathUtil.businessConfigFilePath(tableconstructCommon.BusinessName);
			Properties config = new Properties();
			InputStreamReader inStream_po = new InputStreamReader(new FileInputStream(configPath), "UTF-8");
			config.load(inStream_po);
			tableconstructCommon.config = config;
		}

		// 管理器初始化
		poManager = new PoCreateManager();
		mapperManager = new MapperCreateManager();
		daoManager = new DaoCreateManager();

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
