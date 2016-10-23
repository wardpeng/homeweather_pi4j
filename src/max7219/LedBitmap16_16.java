package max7219;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import font.MyStrings;

/**
 * Matrix 7219 LED ��ʾ
 * 
 * @author yancheng
 * 
 * 
 */
public class LedBitmap16_16
{

	private static final boolean debug = false;

	public static class Constants
	{
		public static byte MAX7219_REG_NOOP = 0x0;
		public static byte MAX7219_REG_DIGIT0 = 0x1;
		public static byte MAX7219_REG_DIGIT1 = 0x2;
		public static byte MAX7219_REG_DIGIT2 = 0x3;
		public static byte MAX7219_REG_DIGIT3 = 0x4;
		public static byte MAX7219_REG_DIGIT4 = 0x5;
		public static byte MAX7219_REG_DIGIT5 = 0x6;
		public static byte MAX7219_REG_DIGIT6 = 0x7;
		public static byte MAX7219_REG_DIGIT7 = 0x8;
		public static byte MAX7219_REG_DECODEMODE = 0x9;
		public static byte MAX7219_REG_INTENSITY = 0xA;
		public static byte MAX7219_REG_SCANLIMIT = 0xB;
		public static byte MAX7219_REG_SHUTDOWN = 0xC;
		public static byte MAX7219_REG_DISPLAYTEST = 0xF;
	}

	protected static final short NUM_DIGITS = 8;// ��ʾһ���ַ���ģʹ��8���ֽ�
	protected static final short NUM_DIGITS_16 = 32;// 16*16�ĵ���ʹ��32���ֽ���ʾһ�� ��Ԫ

	// �ص�������Ļ��>=1��
	protected short cascaded = 1;
	// ��ת����0,90,180,270��
	protected int orientation;
	// ������ʾ���ֽ����飨��������
	protected byte[] buffer;
	// SPI�豸
	protected SpiDevice spi;

	public static void main(String[] args)
	{
		LedBitmap16_16 led = new LedBitmap16_16((short) 8);
		// ������(8*8)����Ļ

		// ���豸
		led.open();

		// ��ת270�ȣ�ȱʡ������Ļ���������У�����Ҫ����������
		led.orientation(90);
		led.showMessag_16_16("��3��");

		try
		{
			System.in.read();
			led.close();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// short[] buffer = new short[32];
		// LedBitmap16_16 led = new LedBitmap16_16((short) 8);
		// for (int i = 0; i < buffer.length; i++)
		// {
		// buffer[i] = (short) i;
		// System.out.print(buffer[i]);
		// }
		// short[] new_buffer = led.SetOddScreen(buffer);
		// System.out.println("*******");
		// for (int i = 0; i < new_buffer.length; i++)
		// {
		// new_buffer[i] = (short) i;
		// }
		//
		// short[] new_buffer_128 = new short[128];
		// System.arraycopy(new_buffer, 0, new_buffer_128, 0, 64);
		// System.arraycopy(new_buffer, 0, new_buffer_128, 64, 64);
		//
		// byte[] scr = led.splitIntoTwoScreen(new_buffer_128);
		// for (int i = 0; i < scr.length; i++)
		// {
		// System.out.print(" ");
		// System.out.print(scr[i]);
		// }
	}

	public LedBitmap16_16(short cascaded)
	{
		this.orientation = 0;
		this.cascaded = cascaded;
		this.buffer = new byte[NUM_DIGITS * this.cascaded];

		try
		{
			if (!debug)
			{
				this.spi = SpiFactory.getInstance(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED,
						SpiDevice.DEFAULT_SPI_MODE);

				command(Constants.MAX7219_REG_SCANLIMIT, (byte) 0x7);
				command(Constants.MAX7219_REG_DECODEMODE, (byte) 0x0);
				command(Constants.MAX7219_REG_DISPLAYTEST, (byte) 0x0);
				// command(Constants.MAX7219_REG_SHUTDOWN, (byte) 0x1);

				this.brightness((byte) 3);
				// this.clear();
			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ����ָ�� data ���Ĵ��� register
	 */
	public void command(byte register, byte data) throws Exception
	{

		int len = 2 * this.cascaded;
		byte[] buf = new byte[len];

		for (int i = 0; i < len; i += 2)
		{
			buf[i] = register;
			buf[i + 1] = data;
		}

		this._write(buf);
	}

	/**
	 * �����Ļ
	 */
	public void clear()
	{

		try
		{
			for (int i = 0; i < this.cascaded; i++)
			{
				for (short j = 0; j < NUM_DIGITS; j++)
				{
					this._setbyte(i, (short) (j + Constants.MAX7219_REG_DIGIT0), (byte) 0x00);
				}
			}
			this.flush();

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * ������ת
	 */
	public void rotate_left(boolean redraw)
	{
		byte t = this.buffer[NUM_DIGITS * this.cascaded - 1];
		for (int i = NUM_DIGITS * this.cascaded - 1; i > 0; i--)
		{
			this.buffer[i] = this.buffer[i - 1];
		}
		this.buffer[0] = t;

		if (redraw)
			this.flush();
	}

	public void rotate_right(boolean redraw)
	{
		byte t = this.buffer[0];
		for (int i = 0; i < NUM_DIGITS * this.cascaded - 1; i++)
		{
			this.buffer[i] = this.buffer[i + 1];
		}
		this.buffer[NUM_DIGITS * this.cascaded - 1] = t;

		if (redraw)
			this.flush();
	}

	/**
	 * �����ƶ�
	 */
	public void scroll_left(boolean redraw)
	{
		for (int i = 0; i < NUM_DIGITS * this.cascaded - 1; i++)
		{
			this.buffer[i] = this.buffer[i + 1];
		}
		this.buffer[NUM_DIGITS * this.cascaded - 1] = 0x0;

		if (redraw)
			this.flush();
	}

	public void scroll_right(boolean redraw)
	{
		for (int i = NUM_DIGITS * this.cascaded - 1; i > 0; i--)
		{
			this.buffer[i] = this.buffer[i - 1];
		}
		this.buffer[0] = 0x0;

		if (redraw)
			this.flush();
	}

	/**
	 * ��ת
	 */
	public void orientation(int angle)
	{
		this.orientation(angle, true);
	}

	public void orientation(int angle, boolean redraw)
	{
		if (angle != 0 && angle != 90 && angle != 180 && angle != 270)
			return;

		this.orientation = angle;
		if (redraw)
			this.flush();
	}

	/**
	 * ��ʾ�ַ�
	 */
	public void letter(short deviceId, short asciiCode)
	{
		this.letter(deviceId, asciiCode, Font.CP437_FONT, true);
	}

	public void letter(short deviceId, short asciiCode, short[][] font)
	{
		this.letter(deviceId, asciiCode, font, true);
	}

	public void letter(short deviceId, short asciiCode, short[][] font, boolean redraw)
	{
		short[] values = Font.value(font, asciiCode);

		short col = Constants.MAX7219_REG_DIGIT0;
		for (short value : values)
		{
			if (col > Constants.MAX7219_REG_DIGIT7)
				return;

			this._setbyte(deviceId, col, (byte) (value & 0xff));
			col += 1;
		}

		if (redraw)
			this.flush();
	}

	/**
	 * ʹ��16*32����2*4�������ʾ16*16��С��Ԫ�ĺ��ֵȣ�������
	 * 
	 * ע�⣺1234��Ӳ������5678��ʵ�ʺ��߷���ǰ������
	 * 
	 * @param text
	 * @param font
	 */
	public void showMessag_16_16(String text)
	{
		/* ����MyString�ĺ�������ȡ������ģ���棬ͬʱ�����ָ */
		short[] values = new MyStrings().getStringBuffer(text);

		/* ����values�����ݣ������ݳ���Ϊ64������ż������Ļ�ֶ� */
		values = SetOddScreen(values);

		// /* ����8*length����ȵĴ洢�ռ� * 2 */
		// byte[] src = new byte[NUM_DIGITS * text.length() * 2];

		/* ��values�е����ݷָ���������б��浽src�� */
		byte[] src = splitIntoTwoScreen(values);
		// for (int i = 0; i < text.length(); i++)// �����ģ����
		// {
		// char c = text.charAt(i);
		// short[] values = Font.value(font, c);
		// for (int j = 0; j < values.length; j++)
		// {
		// src[i * NUM_DIGITS + j] = (byte) (values[j] & 0xff);
		// }
		// }

		for (int i = 0; i < src.length; i++)// һ�η��ͻ����е�����
		{
			try
			{
				Thread.sleep(100);
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}

			this.scroll_left(false);
			this.buffer[this.buffer.length - 1] = src[i];// ������ת�Ƶ������У������ͻ���

			this.flush();

		}
	}

	/*
	 * ��16*16��������ݷָ��������������Ԫ��ʾ��
	 * 
	 * ���ָ1256һ�飬3478һ�顣
	 */
	// _screen:------>val:---------->_scr:
	// 1|2|3|4---------> 0-15*|32-47*---> 0- 7| 8-15|16-23|24-31
	// 5|6|7|8--------->16-31*|48-63*--->32-29|40-47|48-55|56-63
	//
	//
	//
	private byte[] splitIntoTwoScreen(short[] val)
	{
		if (val.length % 64 != 0)
			return null;
		int len = val.length;
		byte[] src = new byte[len];

		for (int i = 0; i < src.length / 64; i++)
		{
			/* ʵ�����ݲ��Լ�excel��� */
			/* 1 2����Ļ���� */
			for (int j = 0; j < 8; j++)// scr:0- 7| 8-15<-----value:0-15*
			{
				src[j + i * 64] = (byte) (0xff & val[2 * j + i * 64]);// 1#��Ļ
			}
			for (int j = 8; j < 16; j++)// scr:0- 7| 8-15<-----value:0-15*
			{
				src[j + i * 64] = (byte) (0xff & val[2 * j - 15 + i * 64]);// 2#
			}

			/* 3 4����Ļ���� */
			for (int j = 16; j < 24; j++)// scr:16-23|24-31<-----val:32-47*
			{
				src[j + i * 64] = (byte) (0xff & val[j * 2 + i * 64]);// 3#
			}
			for (int j = 24; j < 32; j++)// scr:16-23|24-31<-----val:32-47*
			{
				src[j + i * 64] = (byte) (0xff & val[2 * j - 15 + i * 64]);// 4#��j-24)*2+33=2*j-15
			}

			/* 5 6����Ļ���� */
			for (int j = 32; j < 40; j++)// scr:32-39|40-47<-----16-31*
			{
				src[j + i * 64] = (byte) (0xff & val[2 * j - 48 + i * 64]);// 5#
			}
			for (int j = 40; j < 48; j++)// scr:32-39|40-47<-----16-31*
			{
				src[j + i * 64] = (byte) (0xff & val[2 * j - 63 + i * 64]);// 6#
																			// 2*j-63
			}

			/* 7 8����Ļ����:��val��ֱ�ӷ��͵���Ӧ��src�� */
			for (int j = 48; j < 56; j++)// scr:48-55|56-63<-----val:48-63*
			{
				src[j + i * 64] = (byte) (0xff & val[2 * j - 48 + i * 64]);// 7#
			}
			for (int j = 56; j < 64; j++)// scr:48-55|56-63<-----val:48-63*
			{
				src[j + i * 64] = (byte) (0xff & val[2 * j - 63 + i * 64]);// 8#
																			// 2*j-63
			}

		}

		return src;
	}

	public short[] SetOddScreen(short[] val)
	{
		short[] add_val = new short[32];// ��ʼֵ��0
		short[] odd_val = new short[val.length + 32];

		if (val.length % 64 != 0)// ����64�ı�������32�ı�����
		{
			System.arraycopy(val, 0, odd_val, 0, val.length);
			System.arraycopy(add_val, 0, odd_val, val.length, add_val.length);
			return odd_val;
		} else
			return val;
	}

	/**
	 * 8*8��ʾ��������
	 */
	public void showMessag_8_8(String text)

	{
		this.showMessage(text, Font.CP437_FONT);
	}

	public void showMessage(String text, short[][] font)
	{
		for (int i = 0; i < this.cascaded; i++)// ���ÿհ�����
			text += ' ';

		byte[] src = new byte[NUM_DIGITS * text.length()];// ����8*length����ȵĴ洢�ռ�

		for (int i = 0; i < text.length(); i++)// �����ģ����
		{
			char c = text.charAt(i);
			short[] values = Font.value(font, c);
			for (int j = 0; j < values.length; j++)
			{
				src[i * NUM_DIGITS + j] = (byte) (values[j] & 0xff);
			}
		}

		for (int i = 0; i < src.length; i++)// һ�η��ͻ����е�����
		{
			try
			{
				Thread.sleep(100);
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}

			this.scroll_left(false);
			this.buffer[this.buffer.length - 1] = src[i];// ������ת�Ƶ������У������ͻ���

			this.flush();

		}

	}

	/**
	 * ������������д���豸
	 */
	public void flush()
	{
		try
		{
			byte[] buf = this.buffer;

			if (this.orientation > 0)
			{
				buf = this._rotate(buf);
			}

			for (short pos = 0; pos < NUM_DIGITS; pos++)
			{
				this._write(this._values(pos, buf));
			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * �������� intensity�ķ�ΧΪ0<=?<16
	 */
	public void brightness(byte intensity)
	{
		try
		{
			if (intensity < 0 || intensity > 15)
				return;

			this.command(Constants.MAX7219_REG_INTENSITY, intensity);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �ر��豸
	 */
	public void close()
	{
		try
		{
			this.clear();
			this.command(Constants.MAX7219_REG_SHUTDOWN, (byte) 0x0);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * ���豸
	 */
	public void open()
	{
		try
		{
			this.command(Constants.MAX7219_REG_SHUTDOWN, (byte) 0x1);
			this.clear();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	//
	//
	//////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * ��bufд���豸
	 */
	private void _write(byte[] buf) throws Exception
	{
		if (!debug)
		{
			// for(byte b:buf) System.out.print(b+",");
			// System.out.println("");

			this.spi.write(buf);
		} else
		{
			for (byte b : buf)
				System.out.print(b + ",");
			System.out.println("");
		}
	}

	/**
	 * ������Ļ���٣��������д����ֽ����� ���ظ�ʽΪ [position,data,position1,data1],����������ĻΪ����
	 */
	private byte[] _values(short position, byte[] buf) throws Exception
	{
		int len = 2 * this.cascaded;
		byte[] ret = new byte[len];

		for (int i = 0; i < this.cascaded; i++)
		{
			ret[2 * i] = (byte) ((position + Constants.MAX7219_REG_DIGIT0) & 0xff);
			ret[2 * i + 1] = buf[(i * NUM_DIGITS) + position];

		}
		return ret;
	}

	/**
	 * д���ݵ���ʾ������
	 */
	private void _setbyte(int deviceId, short position, byte value)
	{
		int offset = deviceId * NUM_DIGITS + position - Constants.MAX7219_REG_DIGIT0;
		this.buffer[offset] = value;
	}

	/**
	 * 8*8��������ת
	 */
	private byte[] _rotate_8_8(byte[] buf)
	{
		byte[] result = new byte[8];
		for (int i = 0; i < 8; i++)
		{ // ������������
			short b = 0;
			short t = (short) ((0x01 << i) & 0xff); // ��������������һ����ҪȡԴ���ĸ�bit
			for (int j = 0; j < 8; j++)
			{
				int d = 7 - i - j; // ����һ����λ�ĸ�������i�йأ��п���Ϊ����
				if (d > 0)
					b += (short) ((buf[j] & t) << d);
				else
					b += (short) ((buf[j] & t) >> (-1 * d));
			}
			result[i] = (byte) b;
		}

		return result;
	}

	/**
	 * ��ʾ��������ת,ÿ����Ļ������ת
	 */
	private byte[] _rotate(byte[] buf)
	{
		byte[] result = new byte[this.buffer.length];
		for (int i = 0; i < this.cascaded * NUM_DIGITS; i += NUM_DIGITS)
		{
			byte[] tile = new byte[NUM_DIGITS];
			for (int j = 0; j < NUM_DIGITS; j++)
			{
				tile[j] = buf[i + j];
			}
			int k = this.orientation / 90;
			for (int j = 0; j < k; j++)
			{
				tile = this._rotate_8_8(tile);
			}
			for (int j = 0; j < NUM_DIGITS; j++)
			{
				result[i + j] = tile[j];
			}

		}

		return result;
	}

}
