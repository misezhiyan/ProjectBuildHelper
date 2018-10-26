package frame.tableconstruct.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import po.Field;
import po.Table;
import util.FileUtil;
import util.PathUtil;

/**
 * @discription po 模板配置
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class PoCreateManager extends CreateManager {
	private String tmpl;
	private String tmplTmp;

	public PoCreateManager() throws Exception {
		super();
		if (null == tmpl) {
			String poTmplPath = getPoTmplPath();
			tmpl = FileUtil.fileReadToString(poTmplPath);
		}
	}

	public PoCreateManager(Table table) throws Exception {
		super(table);
		if (null == tmpl) {
			String poTmplPath = getPoTmplPath();
			tmpl = FileUtil.fileReadToString(poTmplPath);
		}
	}

	public void matchTable(Table table) {
		super.matchTable(table);
		tmplTmp = tmpl;
	}

	public void createFile() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String author = poConfig.getProperty("author");
		tmplTmp = tmplTmp.replace("${author}", author);

		String TABLE_COMMENT = table.getTABLE_COMMENT();
		tmplTmp = tmplTmp.replace("${discription}", TABLE_COMMENT);

		// String TABLE_NAME = table.getTABLE_NAME();
		String CLASS_NAME = getCLASS_NAME();
		tmplTmp = tmplTmp.replace("${className}", CLASS_NAME);

		tmplTmp = tmplTmp.replace("${date}", sdf.format(new Date()));

		List<Field> fieldList = table.getFieldList();
		String filedArea = filedArea(fieldList);

		tmplTmp = tmplTmp.replace("${filedArea}", filedArea);

		Set<String> importList = importList(table);
		String importArea = formmatImportArea(importList);
		tmplTmp = tmplTmp.replace("${import}", importArea);

		String pkg = getPoRelativePath();
		tmplTmp = tmplTmp.replace("${package}", PathUtil.matchPointPath(pkg));

		// 写入文件
		String outFilePath = getOutFilePath(pkg);
		FileUtil.writeIntoFile(outFilePath, CLASS_NAME, "java", tmplTmp);
	}

	private Set<String> importList(Table table) {

		Set<String> importList = new HashSet<String>();

		// 固定引入
		String listImport = "import java.util.List;";
		// 字段类型引入
		List<Field> fieldList = table.getFieldList();
		for (Field field : fieldList) {
			String DATA_TYPE = field.getDATA_TYPE();
			DATA_TYPE = matchJAVAType(DATA_TYPE);
			DATA_TYPE = matchFullJAVAType(DATA_TYPE);

			String dbTypeImport = "import " + DATA_TYPE + ";";

			importList.add(dbTypeImport);
		}

		importList.add(listImport);

		return importList;
	}

	private String filedArea(List<Field> fieldList) {

		// 字段区域
		String oneFiledArea = oneFiledArea();
		String filedArea = "";
		// 方法区域
		String oneFiledMethodArea = oneFiledMethodArea();
		String filedMethodArea = "";

		filedArea += "\r\n";
		for (Field field : fieldList) {
			String COLUMN_COMMENT = field.getCOLUMN_COMMENT();
			String DATA_TYPE = field.getDATA_TYPE();
			String COLUMN_NAME = field.getCOLUMN_NAME();

			// 字段区域
			String oneFiledAreaTemp = oneFiledArea;
			oneFiledAreaTemp = oneFiledAreaTemp.replace("${fieldComment}", COLUMN_COMMENT);

			String fieldType = matchJAVAType(DATA_TYPE);
			oneFiledAreaTemp = oneFiledAreaTemp.replace("${fieldType}", fieldType);
			oneFiledAreaTemp = oneFiledAreaTemp.replace("${fieldName}", COLUMN_NAME);

			// 方法区域
			String oneFiledMethodAreaTemp = oneFiledMethodArea;
			oneFiledMethodAreaTemp = oneFiledMethodAreaTemp.replace("${fieldType}", fieldType);

			String methodName = COLUMN_NAME.substring(0, 1).toUpperCase() + COLUMN_NAME.substring(1);
			oneFiledMethodAreaTemp = oneFiledMethodAreaTemp.replace("${fieldMethodName}", methodName);
			oneFiledMethodAreaTemp = oneFiledMethodAreaTemp.replace("${fieldName}", COLUMN_NAME);

			filedArea += oneFiledAreaTemp;
			filedMethodArea += oneFiledMethodAreaTemp;
		}
		// 总区域
		String filedAllArea = filedArea + "\r\n" + filedMethodArea;

		return filedAllArea;
	}

	private String oneFiledArea() {

		String oneFiledArea = "";

		String changeLine = "\r\n";
		String space = "	";

		oneFiledArea += space + "// ${fieldComment}" + changeLine;
		oneFiledArea += space + "private ${fieldType} ${fieldName};" + changeLine;

		return oneFiledArea;
	}

	private String oneFiledMethodArea() {

		String oneFiledMethodArea = "";

		String changeLine = "\r\n";
		String space = "	";

		oneFiledMethodArea += space + "public ${fieldType} get${fieldMethodName}() {" + changeLine;
		oneFiledMethodArea += space + "	return ${fieldName};" + changeLine;
		oneFiledMethodArea += space + "}" + changeLine;

		oneFiledMethodArea += changeLine;

		oneFiledMethodArea += space + "public void set${fieldMethodName}(${fieldType} ${fieldName}) {" + changeLine;
		oneFiledMethodArea += space + "	${fieldName} = ${fieldName};" + changeLine;
		oneFiledMethodArea += space + "}" + changeLine;

		oneFiledMethodArea += changeLine;

		return oneFiledMethodArea;
	}

}
