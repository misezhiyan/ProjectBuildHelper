package frame.tablecreate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import frame.excutor.Excutor;
import util.PathUtil;

/**
 * @discription 获取表创建 sql
 * @author kimmy
 * @date 2018年10月31日 上午10:33:50
 */
public class tablecreateExcutor extends Excutor {

	/**
	 * @discription 读取表创建文件 创建表
	 * @author kimmy
	 * @throws Exception
	 * @date 2018年10月31日 下午1:47:32
	 */
	public static void createTable() throws Exception {
		init();

		Properties config = tablecreateCommon.config;
		String BASE_PACKAGE = config.getProperty("BASE_PACKAGE");

		File[] fileArr = null;
		// 读取方式
		String READTYPE = config.getProperty("READTYPE");
		// 指定文件读取
		if ("FILE".equals(READTYPE)) {
			String FILETYPE = config.getProperty("SCANNER_FILETYPE");
			String SCANNER_FILENAME = config.getProperty("SCANNER_FILENAME");
			String[] fileNameArr = SCANNER_FILENAME.split(",");

			fileArr = new File[fileNameArr.length];
			for (int i = 0; i < fileNameArr.length; i++) {
				String fileName = fileNameArr[i];
				String fileRealPath = BASE_PACKAGE + "/" + fileName + "." + FILETYPE;
				File file = new File(fileRealPath);
				if (!file.exists())
					throw new Exception(fileName + "." + FILETYPE + " 不存在, 或不在指定包下");
				fileArr[i] = file;
			}
		} else if ("PACAKGE".equals(READTYPE)) {
			String SCANNER_PACKAGE = config.getProperty("SCANNER_PACKAGE");
			String scannerPacakgeRealPath = BASE_PACKAGE + "/" + SCANNER_PACKAGE;

			File scannerPkg = new File(scannerPacakgeRealPath);
			fileArr = scannerPkg.listFiles();
		} else {
			return;
		}

		projectComPonentsService.createTables(fileArr);

	}

	private static void init() throws IOException {
		// 业务配置
		if (null == tablecreateCommon.config) {
			String configPath = PathUtil.businessConfigFilePath(tablecreateCommon.BusinessName);
			Properties config = new Properties();
			InputStreamReader inStream_po = new InputStreamReader(new FileInputStream(configPath), "UTF-8");
			config.load(inStream_po);
			tablecreateCommon.config = config;
		}
	}

}
