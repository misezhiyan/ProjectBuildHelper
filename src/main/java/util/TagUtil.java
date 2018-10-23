package util;

import java.util.ArrayList;
import java.util.List;

/**
 * @discription 解析标签
 * @author kimmy
 * @date 2018年10月8日 上午11:04:28
 */
public class TagUtil {

	private static String STARTTAG_START = "<";
	private static String STARTTAG_END = ">";
	private static String ENDTAG_START = "</";
	private static String ENDTAG_END = ">";

	public static void main(String[] args) {
		String content = "<PKG>\r\n" + "package ${pkg};\r\n" + "</PKG>\r\n";
		content += "<IMPORT>\r\n" + "import ${imp};\r\n" + "</IMPORT>\r\n";
		// content += "<>";

		text("PKG", content);
	}

	public static String text(String tagName, String content) {

		String resultContent = null;

		int maxLength = content.length();
		System.out.println(maxLength);

		List<Tag> rootList = new ArrayList<Tag>();
		int pos_begin = -1;
		int pos_end = -1;
		for (int i = 0; i < maxLength; i++) {
			char charAt = content.charAt(i);
			if (charAt == '<') {
				pos_begin = i;
			}
			if (charAt == '>') {
				Tag root = new Tag();
				pos_end = i;

				String tag = content.substring(pos_begin, pos_end + 1);
				pos_begin = -1;
				pos_end = -1;

				if (tag.startsWith("</"))
					root.setTagType("END");

				switch (root.getTagType()) {
				case "BEGIN":
					root.setTagName(tag.substring(1, tag.length() - 1));
					break;
				case "END":
					root.setTagName(tag.substring(2, tag.length() - 1));
					break;
				}

				rootList.add(root);
			}

		}
		for (Tag tag : rootList)
			System.out.println(tag.getTagName());

		// Pattern p = Pattern.compile(STARTTAG_START, Pattern.CASE_INSENSITIVE);
		// Matcher m = p.matcher(content);
		// int count = 0;
		// while (m.find()) {
		// // String group = m.group();
		// Tag tag = new Tag();
		// int start = m.start();
		//
		// for (int i = 0; i < (maxLength - start); i++) {
		// char charAt = content.charAt(start + i);
		// if (charAt != '>')
		// continue;
		// String tagC
		// }
		// // count++;
		// }

		return resultContent;
	}
}

class Tag {
	private Tag parentTag = null;

	private String tagType = "BEGIN";// BEGIN END

	private String tagName = "";
	private String text = "";

	public Tag getParentTag() {
		return parentTag;
	}

	public void setParentTag(Tag parentTag) {
		this.parentTag = parentTag;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName.trim();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	// Tag childTag = null;
}
