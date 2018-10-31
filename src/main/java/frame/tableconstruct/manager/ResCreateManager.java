package frame.tableconstruct.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import po.Table;
import util.FileUtil;
import util.PathUtil;
import util.StringUtil;

/**
 * @discription dao 模板配置
 * @author kimmy
 * @date 2018年10月8日 上午10:28:34
 */
public class ResCreateManager extends CreateManager {

	private String tmpl;
	private String tmplTmp;

	public ResCreateManager() throws Exception {
		super();
		if (null == tmpl) {
			String resTmplPath = getResTmplPath();
			tmpl = FileUtil.fileReadToString(resTmplPath);
		}
	}

	public ResCreateManager(Table table) throws Exception {
		super(table);
		if (null == tmpl) {
			String daoTmplPath = getDaoTmplPath();
			tmpl = FileUtil.fileReadToString(daoTmplPath);
		}
	}

	public void matchTable(Table table) {
		super.matchTable(table);
		tmplTmp = tmpl;
	}

	public void createFile() throws Exception {

		String mapperPkg = getMapperRelativePath();

		String mapperImport = "<mapper resource=\"" + PathUtil.matchLinePath(mapperPkg) + "." + table.getTABLE_NAME() + "Mapper.xml\" />";
		tmplTmp = tmplTmp.replace("${mapperImport}", mapperImport);

		// 写入文件
		// String CLASS_NAME = getCLASS_NAME();
		String pkg = getResRelativePath();
		String outFilePath = getOutFilePath(pkg);
		String outFile = PathUtil.matchLinePath(outFilePath) + "/" + "Res.res";

		File out = new File(outFile);
		String oldContent = "";
		if (out.exists())
			oldContent = FileUtil.fileReadToString(outFile);

		String resArea = resArea(oldContent, tmplTmp);

		FileUtil.writeIntoFileWithDir(outFile, resArea);
	}

	private String resArea(String oldContent, String newContent) throws Exception {

		String tagListStr = resConfig.getProperty("tagList");
		String[] tagArr = tagListStr.split(",");

		Map<String, String> oldAreaMap = tagAreas(tagArr, oldContent);
		Map<String, String> newAreaMap = tagAreas(tagArr, newContent);

		String result = "";
		for (String tagName : tagArr) {
			String oldArea = oldAreaMap.get(tagName);
			if (!StringUtil.isEmpty(oldArea)) {

				result += oldArea;
				result += "\r\n";
			}
			String newArea = newAreaMap.get(tagName);
			if (!StringUtil.isEmpty(newArea)) {
				result += newArea;
				result += "\r\n";
			}

			result = "<" + tagName + ">" + "\r\n" + result;
			result += "</" + tagName + ">";
			result += "\r\n";
		}

		return result;
	}

	private Map<String, String> tagAreas(String[] tagArr, String content) throws Exception {

		Map<String, String> textMap = new HashMap<String, String>();
		for (String tagName : tagArr) {
			String fullTag_start = "<" + tagName + ">";
			String fullTag_end = "</" + tagName + ">";

			int indexStart = content.indexOf(fullTag_start);
			int indexEnd = content.indexOf(fullTag_end);

			if (indexStart < 0 && indexEnd < 0)
				continue;

			if (indexStart > 0 && indexEnd < 0)
				throw new Exception("没有关闭标签: " + tagName);
			if (indexStart < 0 && indexEnd > 0)
				throw new Exception("没有关闭标签: " + tagName);

			textMap.put(tagName, content.substring(indexStart + fullTag_start.length() + 2, indexEnd - 2));
		}

		return textMap;
	}

}
