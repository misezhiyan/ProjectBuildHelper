package po;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import constant.Constant;
import util.FileUtil;

/**
 * @discription dao 模板配置
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class Tmpl_dao {

	private String tmpl;
	String daoConfigPath = Constant.BUSINESSCONFIGREALPATH + "/daoConfig.properties";
	String daoTmplPath = Constant.BUSINESSCONFIGREALPATH + "/tmpl/dao.tmpl";

	public Tmpl_dao() throws Exception {

		Properties properties = new Properties();
		InputStreamReader inStream = new InputStreamReader(new FileInputStream(daoConfigPath), "UTF-8");
		properties.load(inStream);

		tmpl = FileUtil.fileReadToString(daoTmplPath);
	}

}
