package pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pattern
{
	public static void main(String[] args)
	{
		List<String> myStringList = new pattern().getStringList("һһ11����22����33����44");
		int len = myStringList.size();
		for (int j = 0; j < len; j++)
		{
			System.out.println(myStringList.get(j));
		}
	}

	/**
	 * ������Ӣ�Ļ���ַ��������List
	 * 
	 * @param myString:��Ҫ��Ӣ�Ľ�����֣�����ɶԳ��֣������Ŀ�ʼ��Ӣ�Ľ����������Կո������
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

		// Ӣ�����
		Pattern p = Pattern.compile("[!-~]+");
		Matcher m = p.matcher(myStr);
		// System.out.println("Ӣ�ķֶΣ�");
		while (m.find())
		{
			englishList.add(m.group());
		}
		// ��Ӣ�����
		p = Pattern.compile("[^!-~]+");
		m = p.matcher(myStr);
		// System.out.println();
		// System.out.println("���ķֶΣ�");
		while (m.find())
		{
			chineseList.add(m.group());
		}

		int len = chineseList.size();// ��Ӣ���ַ�������ͬ
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
