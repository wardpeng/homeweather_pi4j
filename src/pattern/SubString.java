package pattern;

/***
 * @author WANGYUTAO �����ַ���
 */

public class SubString
{

	// public static void main(String[] args) {
	// String str = "�Ұ���˹�ٷ�����ʦ���ϵ۷��ĵط�ʱ��������ʦ�󷨽�";
	// String str1 = "sdfdssfsfdsf��dsdafdafafdsfadas";
	// System.out.println(MySubstring(str, 14));
	// System.out.println(MySubstring(str1, 14));
	// }

	/**
	 * �ж�һ���ַ���Ascill�ַ����������ַ����纺���գ������ַ���
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c)
	{
		int k = 0x80;
		return (c / k) == 0 ? true : false;
	}

	/**
	 * �õ�һ���ַ����ĳ���,��ʾ�ĳ���,һ�����ֻ��պ��ĳ���Ϊ2,Ӣ���ַ�����Ϊ1
	 * 
	 * @param String
	 *            s ,��Ҫ�õ����ȵ��ַ���
	 * @return int, �õ����ַ�������
	 */
	public static int length(String s)
	{
		if (s == null)
		{
			return 0;
		}
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++)
		{
			len++;
			if (!isLetter(c[i]))
			{
				len++;
			}
		}
		return len;
	}

	/**
	 * �õ�һ���ַ����ĳ���,��ʾ�ĳ���,һ�����ֻ��պ��ĳ���Ϊ1,Ӣ���ַ�����Ϊ0.5
	 * 
	 * @param String
	 *            s ��Ҫ�õ����ȵ��ַ���
	 * @return int �õ����ַ�������
	 */
	public static double getLength(String s)
	{
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// ��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1
		for (int i = 0; i < s.length(); i++)
		{
			// ��ȡһ���ַ�
			String temp = s.substring(i, i + 1);
			// �ж��Ƿ�Ϊ�����ַ�
			if (temp.matches(chinese))
			{
				// �����ַ�����Ϊ1
				valueLength += 1;
			} else
			{
				// �����ַ�����Ϊ0.5
				valueLength += 0.5;
			}
		}
		// ��λȡ��
		return Math.ceil(valueLength);
	}

	/**
	 * ��ȡһ���ַ��ĳ���,��������Ӣ��,������ֲ����ã�����ȡһ���ַ�λ
	 * 
	 * @author patriotlml
	 * @param String
	 *            origin, ԭʼ�ַ���
	 * @param int
	 *            len, ��ȡ����(һ�����ֳ��Ȱ�2���)
	 * @return String, ���ص��ַ���
	 */
	public static String getSubString(String origin, int len)
	{
		if ((origin == null) || origin.equals("") || (len < 1))
		{
			return "";
		}
		byte[] strByte = new byte[len];
		int reLen = 0;
		if (len > length(origin))
		{
			return origin;
		}
		System.arraycopy(origin.getBytes(), 0, strByte, 0, len);
		int count = 0;
		for (int i = 0; i < len; i++)
		{
			int value = strByte[i];
			if (value < 0)
			{
				count++;
			}
		}
		if ((count % 2) != 0)
		{
			reLen = (len == 1) ? ++len : --len;
		}
		return new String(strByte, 0, reLen);
	}
}