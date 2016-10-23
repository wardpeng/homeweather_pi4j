package font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class testHzk16
{
	private byte[] dotfont;

	public testHzk16()
	{
		File file = new File("hzk16s");
		System.out.println("hzk16f:");
		try
		{
			FileInputStream fis = new FileInputStream(file);
			dotfont = new byte[fis.available()];
			fis.read(dotfont);
			System.out.println(dotfont.length);
			fis.close();
		} catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		} catch (IOException ex)
		{
		}

	}

	public void printHz16()
	{
		byte[] an = str2bytes("∏≤");
		int buffer[] = new int[32];
		int key[] =
		{ 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };

		int highByte = an[0] & 0xff;
		int lowByte = an[1] & 0xff;
		int offset = (94 * (highByte - 0xa0 - 1) + (lowByte - 0xa0 - 1)) * 32;
		System.out.print("Œ“ offset:");
		System.out.println(offset);

		for (int i = 0; i < 32; i++)
		{
			byte b = dotfont[offset++];
			buffer[i] = b;
			// System.out.println(Integer.toHexString(b));
		}
		System.out.println();
		for (int k = 0; k < 16; k++)
		{
			for (int j = 0; j < 2; j++)
			{
				for (int i = 0; i < 8; i++)
				{
					int flag = buffer[k * 2 + j] & key[i];
					System.out.printf("%s", flag == 0 ? " " : "°Ò");
				}
			}
			System.out.println();
		}
	}

	byte[] str2bytes(String s)
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

	public static void main(String[] args)
	{
		testHzk16 test = new testHzk16();
		test.printHz16();
	}
}