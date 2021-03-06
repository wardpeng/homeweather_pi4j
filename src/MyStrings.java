
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理EnglishString和ChineseString
 * 
 * @author pengxiangqi
 *
 */
public class MyStrings
{
	public static void main(String[] args)
	{
		// new MyStrings().printByConsole(new
		// ChineseString().getChineseBuffer("大胖派我来巡山"));
		// new MyStrings().printByConsole(new
		// EnglishString().getEnglishBuffer("1234ABC"));
		// new EnglishString().transferEnglishBuffer("");

		// new MyStrings().printByConsoleEnglish(new
		// EnglishString().getEnglishBuffer("1234ABC"));

		MyStrings myStrings = new MyStrings();
		myStrings.printByConsole(myStrings.getStringBuffer("大121大胖大胖12"));

	}

	/**
	 * MyString的对外接口，输入任意一个汉字或英文字符串，返回short[]数组
	 * 
	 * @param myString
	 *            空格将会被删除
	 * @return
	 */
	public short[] getStringBuffer(String myString)
	{
		myString += "........";// 添加结束（同时保证结尾肯定为英文）
		myString.replaceAll("\\s*", "");// 删除所有的空格
		List<String> myStringList = getStringList(myString);
		short[] resultBuffer = null, firstBuffer = null, secondBuffer;
		for (int i = 0; i < myStringList.size(); i += 2)
		{
			firstBuffer = new ChineseString().getChineseBuffer(myStringList.get(i));
			secondBuffer = new EnglishString().getEnglishBuffer(myStringList.get(i + 1));
			resultBuffer = combineShortArray(resultBuffer, firstBuffer);
			resultBuffer = combineShortArray(resultBuffer, secondBuffer);
		}
		return resultBuffer;
	}

	private short[] combineShortArray(short[] firstArray, short[] secondArray)
	{
		int len1 = 0;
		if (firstArray != null)// 处理null数据
		{
			len1 = firstArray.length;
		}
		int len2 = secondArray.length;
		short[] resultArray = new short[len1 + len2];
		if (firstArray != null)
		{
			System.arraycopy(firstArray, 0, resultArray, 0, firstArray.length);
			System.arraycopy(secondArray, 0, resultArray, firstArray.length, secondArray.length);
		} else// 处理null数据
		{
			return secondArray;
		}
		return resultArray;
	}

	/**
	 * 输入中英文混合字符串，输出List
	 * 
	 * @param myString:需要中英文交替出现，必须成对出现：以中文开始，英文结束（可以以空格结束）
	 * @return
	 */
	private List<String> getStringList(String myStr)
	{
		List<String> myStringList = new ArrayList<>();
		List<String> englishList = new ArrayList<>();
		List<String> chineseList = new ArrayList<>();

		if (myStr == "")
		{
			return null;
		}

		// 英文组合
		Pattern p = Pattern.compile("[ -~]+");
		Matcher m = p.matcher(myStr);
		// System.out.println("英文分段：");
		while (m.find())
		{
			englishList.add(m.group());
		}
		// 非英文组合
		p = Pattern.compile("[^ -~]+");
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

	/**
	 * 通过控制台，按照汉字的方式两两输出英文
	 * 
	 * @param buffer
	 */
	private void printByConsole(short[] buffer)
	{
		int key[] =
		{ 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };

		int strLength = buffer.length;
		if (strLength % 32 != 0)
		{
			System.out.println("字符串缓存返回错误（不是32的倍数）");
			return;
		}
		for (int len = 0; len < strLength / 32; len++)
		{
			for (int k = 0; k < 16; k++)
			{
				for (int j = 0; j < 2; j++)
				{
					short byteDate = buffer[k * 2 + j + len * 32];
					for (int i = 0; i < 8; i++)
					{
						int flag = byteDate & key[i];
						System.out.printf("%s", flag == 0 ? " " : "●");
					}
				}
				System.out.println();
			}
			System.out.println("************************");
		}
	}

	/**
	 * 通过控制台打印输出
	 * 
	 * @param buffer
	 */
	private void printByConsoleEnglish(short[] buffer)
	{
		int key[] =
		{ 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };

		int strLength = buffer.length;
		if (strLength % 32 != 0)
		{
			System.out.println("字符串缓存返回错误（不是32的倍数）");
			return;
		}
		for (int len = 0; len < strLength / 32; len++)
		{
			for (int k = 0; k < 16; k++)
			{
				for (int j = 0; j < 2; j++)
				{
					short byteDate = buffer[k + 16 * j + len * 32];
					for (int i = 0; i < 8; i++)
					{
						int flag = byteDate & key[i];
						System.out.printf("%s", flag == 0 ? " " : "●");
					}
				}
				System.out.println();
			}
			System.out.println("************************");
		}
	}
}

class EnglishString
{
	private String englishStrings;

	/**
	 * 构造函数
	 * 
	 * 
	 */
	public EnglishString()
	{
	}

	/**
	 * 设置英文字符
	 * 
	 * @param englishStrs
	 */
	private void setStrings(String englishStrs)
	{
		int len = englishStrs.length();
		if (len % 2 == 1)// 偶数个字符
			englishStrs += " ";
		this.englishStrings = englishStrs;

	}

	/**
	 * 获取字符串的字模，一个字符是8*16，两两字符拼接成16*16 奇数个字符的最后一个字符为空格，以保证输出为16*16个点阵， 即32个字节为单位
	 * 
	 * @param englishStrs
	 * @return 输出时，两个8*16字符合并成一个16*16的输出（和汉字的扫描方式相同）
	 */
	public short[] getEnglishBuffer(String englishStrs)
	{
		setStrings(englishStrs);
		int len = this.englishStrings.length();
		short[] buffer = new short[len * 16];
		int index = 0;
		for (int i = 0; i < len; i++)
		{
			short oneCharModule[] = new short[16];
			oneCharModule = Font_8_16.value(this.englishStrings.charAt(i));
			for (int j = 0; j < 16; j++)
			{
				buffer[index + j] = oneCharModule[j];
			}
			index += 16;
		}
		// return buffer;
		return transferEnglishBuffer(buffer);// 转换字模
	}

	/**
	 * 功能：8*16字模转换成16*16字模：合并两个数字为一个“汉字”
	 * 
	 * @param originalEnglishBuffer
	 *            原始8*16点阵字模
	 * @return 输出为16*16，与汉字扫描方式相同的字模
	 */
	public short[] transferEnglishBuffer(short[] originalEnglishBuffer)
	{
		short[] resultBuffer = new short[originalEnglishBuffer.length];
		int index = 0;
		int strLength = originalEnglishBuffer.length;
		for (int len = 0; len < strLength / 32; len++)
		{
			for (int k = 0; k < 16; k++)
			{
				for (int j = 0; j < 2; j++)
				{
					resultBuffer[index++] = originalEnglishBuffer[k + j * 16 + len * 32];
				}
			}
		}
		return resultBuffer;
	}

}

/**
 * 获取指定汉字的字模数据16*16点阵，一个汉字对应31个字节
 * 
 * @author pengxiangqi
 *
 */
class ChineseString
{
	private byte[] dotfont;// 字库缓存
	private String chineseStrings;

	/**
	 * 构造函数
	 * 
	 * @param chineseStrs
	 */
	public ChineseString(String chineseStrs)
	{
		this();
		setStrings(chineseStrs);
	}

	/*
	 * 无参构造函数
	 */
	public ChineseString()
	{
		File file = new File("HZK16C");// 字库文件，放在同一目录下
		try
		{
			FileInputStream fis = new FileInputStream(file);
			dotfont = new byte[fis.available()];
			fis.read(dotfont);// 将字库内容读取到缓存中
			// System.out.println(dotfont.length);
			fis.close();
		} catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		} catch (IOException ex)
		{
		}
	}

	/**
	 * 设置中文字符
	 * 
	 * @param chineseStrs
	 */
	private void setStrings(String chineseStrs)
	{
		chineseStrings = chineseStrs;
	}

	/**
	 * 获取指定 "汉字" 字符串的字模
	 * 
	 * @param chineseStrs
	 * @return
	 */
	public short[] getChineseBuffer(String chineseStrs)
	{
		setStrings(chineseStrs);
		return getChineseBuffer();
	}

	/**
	 * 获取指定 "汉字" 字符串的字模
	 * 
	 * @param strChinese
	 * @return
	 */
	private short[] getChineseBuffer()
	{

		short[] chineseBuffers = new short[32 * chineseStrings.length()];// 一个汉字占用32个字节
		byte[] oneChinese = new byte[32];
		int bufLength = 0;

		for (int i = 0; i < chineseStrings.length(); i++)
		{
			char b = chineseStrings.charAt(i);
			String str = "" + b;
			oneChinese = getOneChineseModule(str);
			if (oneChinese != null)// 字库中没有str这个汉字，则返回null，需要处理(暂时是忽略)，否则抛出异常，程序终止
			{
				for (int j = 0; j < 32; j++)
				{
					chineseBuffers[bufLength + j] = (short) (0xff & oneChinese[j]);
				}
				bufLength += 32;
			}
		}

		return chineseBuffers;
	}

	/**
	 * 从中字库缓存中获得一个汉字的字模数据
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
	 * 获取字节编码
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

class Font_8_16
{
	/**
	 * 获取指定asciiCode对应的点阵数组
	 */
	public static short[] value(int assicCode)
	{
		short[] font = new short[16];
		int index = assicCode & 0x7f;
		for (int i = 0; i < 16; i++)
		{
			font[i] = F_8_16_FONT[index * 16 + i];
		}
		return font;
	}

	/**
	 * 裁剪后的assic的8*16字模，主要保留了字符和数字以及符号，其他以空格代替 每个字符为8*16，16个字节
	 */
	public static short[] F_8_16_FONT =
	{
			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:  ！  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x00, 0x00, 0x10, 0x10, 0x00, 0x00,

			/*--  文字:  ”  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x19, 0x19, 0x08, 0x11, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:  #  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x24, 0x24, 0x24, 0xFE, 0x48, 0x48, 0x48, 0xFE, 0x48, 0x48, 0x48, 0x00, 0x00,

			/*--  文字:  $  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x10, 0x38, 0x54, 0x54, 0x50, 0x30, 0x18, 0x14, 0x14, 0x54, 0x54, 0x38, 0x10, 0x10,

			/*--  文字:  %  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x44, 0xA4, 0xA8, 0xA8, 0xA8, 0x54, 0x1A, 0x2A, 0x2A, 0x2A, 0x44, 0x00, 0x00,

			/*--  文字:  &  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x30, 0x48, 0x48, 0x48, 0x50, 0x6E, 0xA4, 0x94, 0x88, 0x89, 0x76, 0x00, 0x00,

			/*--  文字:  ,  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x60, 0x60, 0x20, 0xC0,

			/*--  文字:  (  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x02, 0x04, 0x08, 0x08, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x08, 0x08, 0x04, 0x02, 0x00,

			/*--  文字:  )  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x40, 0x20, 0x10, 0x10, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x10, 0x10, 0x20, 0x40, 0x00,

			/*--  文字:  *  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0xD6, 0x38, 0x38, 0xD6, 0x10, 0x10, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:  +  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0x10, 0x10, 0xFE, 0x10, 0x10, 0x10, 0x10, 0x00, 0x00, 0x00,

			/*--  文字:  ,  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x60, 0x60, 0x20, 0xC0,

			/*--  文字:  -  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:  .  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x60, 0x60, 0x00, 0x00,

			/*--  文字:  /  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x01, 0x02, 0x02, 0x04, 0x04, 0x08, 0x08, 0x10, 0x10, 0x20, 0x20, 0x40, 0x40, 0x00,

			/*--  文字:  0  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x18, 0x24, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x24, 0x18, 0x00, 0x00,

			/*--  文字:  1  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x10, 0x70, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00,

			/*--  文字:  2  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x42, 0x04, 0x04, 0x08, 0x10, 0x20, 0x42, 0x7E, 0x00, 0x00,

			/*--  文字:  3  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x04, 0x18, 0x04, 0x02, 0x02, 0x42, 0x44, 0x38, 0x00, 0x00,

			/*--  文字:  4  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x04, 0x0C, 0x14, 0x24, 0x24, 0x44, 0x44, 0x7E, 0x04, 0x04, 0x1E, 0x00, 0x00,

			/*--  文字:  5  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x7E, 0x40, 0x40, 0x40, 0x58, 0x64, 0x02, 0x02, 0x42, 0x44, 0x38, 0x00, 0x00,

			/*--  文字:  6  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x1C, 0x24, 0x40, 0x40, 0x58, 0x64, 0x42, 0x42, 0x42, 0x24, 0x18, 0x00, 0x00,

			/*--  文字:  7  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x7E, 0x44, 0x44, 0x08, 0x08, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x00, 0x00,

			/*--  文字:  8  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x42, 0x24, 0x18, 0x24, 0x42, 0x42, 0x42, 0x3C, 0x00, 0x00,

			/*--  文字:  9  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x18, 0x24, 0x42, 0x42, 0x42, 0x26, 0x1A, 0x02, 0x02, 0x24, 0x38, 0x00, 0x00,

			/*--  文字:  :  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x00, 0x00,

			/*--  文字:  ;  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0x20,

			/*--  文字:  <  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x00, 0x00,

			/*--  文字:  =  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFE, 0x00, 0x00, 0x00, 0xFE, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:  >  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x00, 0x00,

			/*--  文字:  ?  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x62, 0x02, 0x04, 0x08, 0x08, 0x00, 0x18, 0x18, 0x00, 0x00,

			/*--  文字:  @  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x38, 0x44, 0x5A, 0xAA, 0xAA, 0xAA, 0xAA, 0xB4, 0x42, 0x44, 0x38, 0x00, 0x00,

			/*--  文字:  A  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x10, 0x10, 0x18, 0x28, 0x28, 0x24, 0x3C, 0x44, 0x42, 0x42, 0xE7, 0x00, 0x00,

			/*--  文字:  B  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xF8, 0x44, 0x44, 0x44, 0x78, 0x44, 0x42, 0x42, 0x42, 0x44, 0xF8, 0x00, 0x00,

			/*--  文字:  C  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3E, 0x42, 0x42, 0x80, 0x80, 0x80, 0x80, 0x80, 0x42, 0x44, 0x38, 0x00, 0x00,

			/*--  文字:  D  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xF8, 0x44, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x44, 0xF8, 0x00, 0x00,

			/*--  文字:  E  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xFC, 0x42, 0x48, 0x48, 0x78, 0x48, 0x48, 0x40, 0x42, 0x42, 0xFC, 0x00, 0x00,

			/*--  文字:  F  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xFC, 0x42, 0x48, 0x48, 0x78, 0x48, 0x48, 0x40, 0x40, 0x40, 0xE0, 0x00, 0x00,

			/*--  文字:  G  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3C, 0x44, 0x44, 0x80, 0x80, 0x80, 0x8E, 0x84, 0x44, 0x44, 0x38, 0x00, 0x00,

			/*--  文字:  H  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xE7, 0x42, 0x42, 0x42, 0x42, 0x7E, 0x42, 0x42, 0x42, 0x42, 0xE7, 0x00, 0x00,

			/*--  文字:  I  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x7C, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00,

			/*--  文字:  J  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3E, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x88, 0xF0,

			/*--  文字:  K  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xEE, 0x44, 0x48, 0x50, 0x70, 0x50, 0x48, 0x48, 0x44, 0x44, 0xEE, 0x00, 0x00,

			/*--  文字:  L  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xE0, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x42, 0xFE, 0x00, 0x00,

			/*--  文字:  M  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xEE, 0x6C, 0x6C, 0x6C, 0x6C, 0x54, 0x54, 0x54, 0x54, 0x54, 0xD6, 0x00, 0x00,

			/*--  文字:  N  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xC7, 0x62, 0x62, 0x52, 0x52, 0x4A, 0x4A, 0x4A, 0x46, 0x46, 0xE2, 0x00, 0x00,

			/*--  文字:  O  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x38, 0x44, 0x82, 0x82, 0x82, 0x82, 0x82, 0x82, 0x82, 0x44, 0x38, 0x00, 0x00,

			/*--  文字:  P  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xFC, 0x42, 0x42, 0x42, 0x42, 0x7C, 0x40, 0x40, 0x40, 0x40, 0xE0, 0x00, 0x00,

			/*--  文字:  Q  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x38, 0x44, 0x82, 0x82, 0x82, 0x82, 0x82, 0xB2, 0xCA, 0x4C, 0x38, 0x06, 0x00,

			/*--  文字:  R  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xFC, 0x42, 0x42, 0x42, 0x7C, 0x48, 0x48, 0x44, 0x44, 0x42, 0xE3, 0x00, 0x00,

			/*--  文字:  S  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x3E, 0x42, 0x42, 0x40, 0x20, 0x18, 0x04, 0x02, 0x42, 0x42, 0x7C, 0x00, 0x00,

			/*--  文字:  T  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xFE, 0x92, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x38, 0x00, 0x00,

			/*--  文字:  U  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xE7, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x3C, 0x00, 0x00,

			/*--  文字:  V  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xE7, 0x42, 0x42, 0x44, 0x24, 0x24, 0x28, 0x28, 0x18, 0x10, 0x10, 0x00, 0x00,

			/*--  文字:  W  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xD6, 0x92, 0x92, 0x92, 0x92, 0xAA, 0xAA, 0x6C, 0x44, 0x44, 0x44, 0x00, 0x00,

			/*--  文字:  X  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xE7, 0x42, 0x24, 0x24, 0x18, 0x18, 0x18, 0x24, 0x24, 0x42, 0xE7, 0x00, 0x00,

			/*--  文字:  Y  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xEE, 0x44, 0x44, 0x28, 0x28, 0x10, 0x10, 0x10, 0x10, 0x10, 0x38, 0x00, 0x00,

			/*--  文字:  Z  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x7E, 0x84, 0x04, 0x08, 0x08, 0x10, 0x20, 0x20, 0x42, 0x42, 0xFC, 0x00, 0x00,

			/*--  文字:  [  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x1E, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x1E, 0x00,

			/*--  文字:  \  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x40, 0x40, 0x20, 0x20, 0x10, 0x10, 0x10, 0x08, 0x08, 0x04, 0x04, 0x04, 0x02, 0x02,

			/*--  文字:  ]  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x78, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x78, 0x00,

			/*--  文字:  ^  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x1C, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:  -  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:  、  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x20, 0x18, 0x0C, 0x04, 0x00, 0x00,

			/*--  文字:  a  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3C, 0x42, 0x1E, 0x22, 0x42, 0x42, 0x3F, 0x00, 0x00,

			/*--  文字:  b  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xC0, 0x40, 0x40, 0x40, 0x58, 0x64, 0x42, 0x42, 0x42, 0x64, 0x58, 0x00, 0x00,

			/*--  文字:  c  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1C, 0x22, 0x40, 0x40, 0x40, 0x22, 0x1C, 0x00, 0x00,

			/*--  文字:  d  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x06, 0x02, 0x02, 0x02, 0x1E, 0x22, 0x42, 0x42, 0x42, 0x26, 0x1B, 0x00, 0x00,

			/*--  文字:  e  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3C, 0x42, 0x7E, 0x40, 0x40, 0x42, 0x3C, 0x00, 0x00,

			/*--  文字:  f  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x0F, 0x11, 0x10, 0x10, 0x7E, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00,

			/*--  文字:  g  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3E, 0x44, 0x44, 0x38, 0x40, 0x3C, 0x42, 0x42, 0x3C,

			/*--  文字:  h  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xC0, 0x40, 0x40, 0x40, 0x5C, 0x62, 0x42, 0x42, 0x42, 0x42, 0xE7, 0x00, 0x00,

			/*--  文字:  i  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x30, 0x30, 0x00, 0x00, 0x70, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00,

			/*--  文字:  j  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x0C, 0x0C, 0x00, 0x00, 0x1C, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x44, 0x78,

			/*--  文字:  k  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0xC0, 0x40, 0x40, 0x40, 0x4E, 0x48, 0x50, 0x68, 0x48, 0x44, 0xEE, 0x00, 0x00,

			/*--  文字:  l  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x70, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00,

			/*--  文字:  m  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFE, 0x49, 0x49, 0x49, 0x49, 0x49, 0xED, 0x00, 0x00,

			/*--  文字:  n  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xDC, 0x62, 0x42, 0x42, 0x42, 0x42, 0xE7, 0x00, 0x00,

			/*--  文字:  o  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x42, 0x42, 0x42, 0x3C, 0x00, 0x00,

			/*--  文字:  p  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xD8, 0x64, 0x42, 0x42, 0x42, 0x44, 0x78, 0x40, 0xE0,

			/*--  文字:  q  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1E, 0x22, 0x42, 0x42, 0x42, 0x22, 0x1E, 0x02, 0x07,

			/*--  文字:  r  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xEE, 0x32, 0x20, 0x20, 0x20, 0x20, 0xF8, 0x00, 0x00,

			/*--  文字:  s  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3E, 0x42, 0x40, 0x3C, 0x02, 0x42, 0x7C, 0x00, 0x00,

			/*--  文字:  t  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0x7C, 0x10, 0x10, 0x10, 0x10, 0x10, 0x0C, 0x00, 0x00,

			/*--  文字:  u  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xC6, 0x42, 0x42, 0x42, 0x42, 0x46, 0x3B, 0x00, 0x00,

			/*--  文字:  v  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xE7, 0x42, 0x24, 0x24, 0x28, 0x10, 0x10, 0x00, 0x00,

			/*--  文字:  w  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xD7, 0x92, 0x92, 0xAA, 0xAA, 0x44, 0x44, 0x00, 0x00,

			/*--  文字:  x  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x6E, 0x24, 0x18, 0x18, 0x18, 0x24, 0x76, 0x00, 0x00,

			/*--  文字:  y  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xE7, 0x42, 0x24, 0x24, 0x28, 0x18, 0x10, 0x10, 0xE0,

			/*--  文字:  z  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7E, 0x44, 0x08, 0x10, 0x10, 0x22, 0x7E, 0x00, 0x00,

			/*--  文字:  {  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x03, 0x04, 0x04, 0x04, 0x04, 0x04, 0x08, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x03, 0x00,

			/*--  文字:  |  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08,

			/*--  文字:  }  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x60, 0x10, 0x10, 0x10, 0x10, 0x10, 0x08, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x60, 0x00,

			/*--  文字:  ~  --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x30, 0x4C, 0x43, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,

			/*--  文字:     --*/
			/*--  新宋体12;  此字体下对应的点阵为：宽x高=8x16   --*/
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00

	}; // end of F_8_16_FONT

}
