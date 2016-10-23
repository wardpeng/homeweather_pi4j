package pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pattern
{
	public static void main(String[] args)
	{
		List<String> myStringList = new pattern().getStringList("一一11二二22三三33四四44");
		int len = myStringList.size();
		for (int j = 0; j < len; j++)
		{
			System.out.println(myStringList.get(j));
		}
	}

	/**
	 * 输入中英文混合字符串，输出List
	 * 
	 * @param myString:需要中英文交替出现，必须成对出现：以中文开始，英文结束（可以以空格结束）
	 * @return
	 */
	public List<String> getStringList(String myStr)
	{
		List<String> myStringList = new ArrayList<>();
		List<String> englishList = new ArrayList<>();
		List<String> chineseList = new ArrayList<>();

		if (myStr == "")
		{
			return null;
		}

		// 英文组合
		Pattern p = Pattern.compile("[!-~]+");
		Matcher m = p.matcher(myStr);
		// System.out.println("英文分段：");
		while (m.find())
		{
			englishList.add(m.group());
		}
		// 非英文组合
		p = Pattern.compile("[^!-~]+");
		m = p.matcher(myStr);
		// System.out.println();
		// System.out.println("中文分段：");
		while (m.find())
		{
			chineseList.add(m.group());
		}

		int len = chineseList.size();// 中英文字符长度相同
		for (int index = 0; index < len; index++)
		{
			// System.out.println();
			// System.out.println();
			myStringList.add(chineseList.get(index));
			myStringList.add(englishList.get(index));
		}

		return myStringList;
	}
}
