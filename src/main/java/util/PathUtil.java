package util;

import java.io.IOException;

/**
 * @discription 常量
 * @author kimmy
 * @date 2018年9月30日 上午9:33:39
 */
public class PathUtil {

	// 本地路径 == 为加载配置文件
	public static String LOCAL_PATH;

	// // 配置文件路径
	// public static String CONFIG_REALPATH;
	// // 业务(po, mapper, dao)配置文件路径
	// public static String BUSINESS_CONFIG_REALPATH;
	// // 输出文件基本路径
	// public static String RESULT_FILEPATH;
	// // 工程名称
	// public static String PROJECTNAME;
	// // 工程文件基础路径
	// public static String BASEPATH;

	{
		try {
			LOCAL_PATH = init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String init() throws IOException {
		// File directory = new File("");
		// String path = directory.getCanonicalPath();
		String path = PathUtil.class.getClassLoader().getResource("").getPath();
		return path;
	}

	// 拼接路径
	public static String matchPath(String fileName, String... pathArr) {

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
	public static String matchLinePath(String path) {

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
	public static String matchPointPath(String path) {

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

	/**********************************
	 * 框架功能
	 ********************************/
	// 配置基础路径
	public static String businessConfigPath(String businessName) {

		String configPath = LOCAL_PATH;
		configPath = matchLinePath(configPath + "/" + businessName);
		return configPath;
	}

	// 配置文件路径
	public static String businessConfigFilePath(String businessName) {

		return businessConfigFilePath(businessName, "properties");
	}

	public static String businessConfigFilePath(String businessName, String fileType) {
		String configPath = businessConfigPath(businessName);
		String configFilePath = configPath + "/" + businessName + "." + fileType;

		return configFilePath;
	}

}
