package po;

import java.util.List;

/**
 * @discription 表结构
 * @author kimmy
 * @date 2018年9月29日 下午3:34:52
 */
public class Table {

	private String TABLE_NAME;
	private String TABLE_COMMENT;
	private List<Field> fieldList;

	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}

	public String getTABLE_COMMENT() {
		return TABLE_COMMENT;
	}

	public void setTABLE_COMMENT(String tABLE_COMMENT) {
		TABLE_COMMENT = tABLE_COMMENT;
	}

	public List<Field> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}

	/***************************
	 * 下方业务方法
	 *************************/

	public String getTABLE_NAME() {
		return this.TABLE_NAME.substring(0, 1).toUpperCase() + this.TABLE_NAME.substring(1);
	}
}
