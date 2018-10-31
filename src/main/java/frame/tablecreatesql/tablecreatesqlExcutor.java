package frame.tablecreatesql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import frame.excutor.Excutor;
import util.FileUtil;
import util.PathUtil;

/**
 * @discription 获取表创建 sql
 * @author kimmy
 * @date 2018年10月31日 上午10:33:50
 */
public class tablecreatesqlExcutor extends Excutor {

	/**
	 * @discription 创建 表创建sql文件
	 * @author kimmy
	 * @throws Exception
	 * @date 2018年10月31日 上午10:38:56
	 */
	public static void createTABLESqlCreateFile() throws Exception {
		// 初始化
		init();

		// 启用配置
		Map<String, List<String>> params_tablecreatesql = new HashMap<String, List<String>>();
		Properties config = tablecreatesqlCommon.config;
		String TABLENEEDDEAL = config.getProperty("TABLENEEDDEAL");
		// 全表输出
		if ("ALL".equals(TABLENEEDDEAL))
			params_tablecreatesql.put("tableName", null);
		// 指定表输出
		else if ("MUL".equals(TABLENEEDDEAL)) {
			String TABLENAMELIST_Str = config.getProperty("TABLENAMELIST");
			List<String> TABLENAMELIST = strToList(TABLENAMELIST_Str);
			params_tablecreatesql.put("tableName", TABLENAMELIST);
		} else {
			return;
		}

		// 获取创建sql
		Map<String, String> sqlCreateList = projectComPonentsService.sqlCreateList(params_tablecreatesql);
		String OUTFILEMODE = config.getProperty("OUTFILEMODE");
		// 统一到一个文件中
		if ("ONE".equals(OUTFILEMODE)) {

			intoOneFile(sqlCreateList);
			// 每个表一个文件
		} else if ("MUL".equals(OUTFILEMODE)) {

			intoMulFile(sqlCreateList);
		}

	}

	private static void intoOneFile(Map<String, String> sqlCreateList) throws Exception {

		Properties config = tablecreatesqlCommon.config;
		String RESULTFILEPATH = config.getProperty("RESULTFILEPATH");
		String OUTFILENAME = config.getProperty("OUTFILENAME");

		String result = "";

		Set<String> keySet = sqlCreateList.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = sqlCreateList.get(key);

			result += value + "\r\n;";
			result += "\r\n";
		}

		String finalOutFileRealPath = RESULTFILEPATH + "/" + OUTFILENAME + ".sqlcreate";
		FileUtil.appendToFile(finalOutFileRealPath, result);
	}

	private static void intoMulFile(Map<String, String> sqlCreateList) throws Exception {

		Properties config = tablecreatesqlCommon.config;
		String RESULTFILEPATH = config.getProperty("RESULTFILEPATH");

		Set<String> keySet = sqlCreateList.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = sqlCreateList.get(key);

			FileUtil.writeIntoFile(RESULTFILEPATH, key, "sqlcreate", value);
		}

	}

	private static List<String> strToList(String TABLENAMELIST_Str) {

		String[] TABLENAMELIST_Arr = TABLENAMELIST_Str.split(",");
		return arrToList(TABLENAMELIST_Arr);
	}

	private static List<String> arrToList(String[] arr) {
		List<String> resultList = new ArrayList<String>();
		for (String str : arr)
			resultList.add(str);
		return resultList;
	}

	private static void init() throws IOException {

		// 业务配置
		if (null == tablecreatesqlCommon.config) {
			String configPath = PathUtil.businessConfigFilePath(tablecreatesqlCommon.BusinessName);
			Properties config = new Properties();
			InputStreamReader inStream_po = new InputStreamReader(new FileInputStream(configPath), "UTF-8");
			config.load(inStream_po);
			tablecreatesqlCommon.config = config;
		}
	}

}
