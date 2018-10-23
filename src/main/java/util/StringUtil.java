package util;

/**
 * @discription
 * @author kimmy
 * @date 2018年9月30日 上午9:56:47
 */
public class StringUtil {

	public static boolean isEmpty(String mybatisConfigPath) {

		return null == mybatisConfigPath || "".equals(mybatisConfigPath);
	}

	public static boolean isBlankEmpty(char charAt) {

		return charAt == ' ' || charAt == '	';
	}

}
