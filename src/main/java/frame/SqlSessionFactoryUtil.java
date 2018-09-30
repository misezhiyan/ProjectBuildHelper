package frame;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import util.StringUtil;

/**
 * @discription 用于单例模式获取sqlSessionFactory
 * @author kimmy
 * @date 2018年9月29日 下午4:38:00
 */
public class SqlSessionFactoryUtil {

	private static SqlSessionFactory sqlSessionFactory;
	private static String mybatisConfigPath;

	public static SqlSession getSqlSession() {

		if (StringUtil.isEmpty(mybatisConfigPath)) {

			File directory = new File("");// 参数为空
			String PROJECTREALPATH = null;
			try {
				PROJECTREALPATH = directory.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String CONFIGREALPATH = PROJECTREALPATH + "/src/main/config";
			mybatisConfigPath = CONFIGREALPATH + "/mybatis.xml";
		}
		File file = new File(mybatisConfigPath);
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());

		Reader re = null;
		try {
			re = Resources.getResourceAsReader("mybatis.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == sqlSessionFactory) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(re);
		}

		return sqlSessionFactory.openSession();
	}
}
