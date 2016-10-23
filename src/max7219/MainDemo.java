package max7219;

import java.io.IOException;

/**
 * ΢ѩ��RPi LED Matrix
 * 
 * @author yancheng
 * 
 */
public class MainDemo
{

	public static void main(String[] args)
	{

		// ������(8*8)����Ļ
		Led c = new Led((short) 4);

		// ���豸
		c.open();

		// ��ת270�ȣ�ȱʡ������Ļ���������У�����Ҫ����������
		c.orientation(90);

		// DEMO1: ���������ĸ
		// c.letter((short) 0, (short) 'A', Font.CP437_FONT, false);
		// c.letter((short) 1, (short) 'Z', Font.CP437_FONT, false);
		// c.letter((short) 2, (short) '1', Font.CP437_FONT, false);
		// c.letter((short) 3, (short) '9', Font.CP437_FONT, false);
		// c.flush();

		// DEMO2: ����������֣��Ƚ�CHOU
		// c.letter((short)0, (short)0,Font.CHN_FONT,false);
		// c.letter((short)1, (short)1,Font.CHN_FONT,false);
		// c.flush();

		// DEMO3: ���һ����ĸ
		c.showMessage("Hello 0123456789$");

		try
		{
			System.in.read();
			c.close();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
