package font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * ��ȡָ�����ֵ���ģ����16*16����һ�����ֶ�Ӧ31���ֽ�
 * 
 * @author pengxiangqi
 *
 */
public class ChineseStringFromFont
{
	private byte[] dotfont;// �ֿ⻺��
	private String chineseStrings;

	public static void main(String[] args)
	{

		ChineseStringFromFont testChinese = new ChineseStringFromFont("����������Ѳɽ");
		byte[] buffer = testChinese.getChineseBuffer();
		testChinese.printByConsole(buffer);
	}

	/**
	 * ���캯��
	 * 
	 * @param chineseStrs
	 */
	public ChineseStringFromFont(String chineseStrs)
	{
		this();
		chineseStrings = chineseStrs;
	}

	public ChineseStringFromFont()
	{
		File file = new File("hzk16s");// �ֿ��ļ�������ͬһĿ¼��
		try
		{
			FileInputStream fis = new FileInputStream(file);
			dotfont = new byte[fis.available()];
			fis.read(dotfont);// ���ֿ����ݶ�ȡ��������
			System.out.println(dotfont.length);
			fis.close();
		} catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		} catch (IOException ex)
		{
		}
	}

	public void setStrings(String chineseStrs)
	{
		chineseStrings = chineseStrs;
	}

	/**
	 * ͨ������̨��ӡ���
	 * 
	 * @param buffer
	 */
	public void printByConsole(byte[] buffer)
	{
		int key[] =
		{ 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };

		int strLength = buffer.length;
		if (strLength % 32 != 0)
		{
			System.out.println("�ַ������淵�ش��󣨲���32�ı�����");
			return;
		}
		for (int len = 0; len < strLength / 32; len++)
		{
			for (int k = 0; k < 16; k++)
			{
				for (int j = 0; j < 2; j++)
				{
					for (int i = 0; i < 8; i++)
					{
						int flag = buffer[k * 2 + j + len * 32] & key[i];
						System.out.printf("%s", flag == 0 ? " " : "��");
					}
				}
				System.out.println();
			}
			System.out.println("************************");
		}
	}

	/**
	 * ��ȡָ�� "����" �ַ�������ģ
	 * 
	 * @param strChinese
	 * @return
	 */
	public byte[] getChineseBuffer()
	{
		byte[] chineseBuffers = new byte[32 * chineseStrings.length()];// һ������ռ��32���ֽ�
		byte[] oneChinese = new byte[32];
		int bufLength = 0;

		for (int i = 0; i < chineseStrings.length(); i++)
		{
			char b = chineseStrings.charAt(i);
			String str = "" + b;
			oneChinese = getOneChineseModule(str);
			for (int j = 0; j < 32; j++)
			{
				chineseBuffers[bufLength + j] = oneChinese[j];
			}
			bufLength += 32;
		}

		return chineseBuffers;
	}

	/**
	 * �����ֿ⻺���л��һ�����ֵ���ģ����
	 * 
	 * @param one
	 */
	private byte[] getOneChineseModule(String one)
	{
		if (one.length() != 1)
		{
			return null;
		}
		byte[] an = str2bytes(one);
		if (an.length < 2)
		{
			return null;
		}
		byte bufferModule[] = new byte[32];
		int highByte = an[0] & 0xff;
		int lowByte = an[1] & 0xff;
		int offset = (94 * (highByte - 0xa0 - 1) + (lowByte - 0xa0 - 1)) * 32;
		for (int i = 0; i < 32; i++)
		{
			byte b = dotfont[offset++];
			bufferModule[i] = b;
			// System.out.println(Integer.toHexString(b));
		}
		return bufferModule;
	}

	/**
	 * ��ȡ�ֽڱ���
	 * 
	 * @param s
	 * @return
	 */
	private byte[] str2bytes(String s)
	{
		if (null == s || "".equals(s))
		{
			return null;
		}
		byte[] abytes = null;
		try
		{
			abytes = s.getBytes("gb2312");
		} catch (UnsupportedEncodingException ex)
		{
		}
		return abytes;
	}
}
