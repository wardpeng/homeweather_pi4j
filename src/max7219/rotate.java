package max7219;

public class rotate
{

	public static void main(String[] args)
	{
		byte[] test =
		{ (byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x11 };
		rotate r = new rotate();
		r.printByConsoleEnglish(test);
		test = r._rotate_8_8(test);
		test = r._rotate_8_8(test);
		test = r._rotate_8_8(test);
		r.printByConsoleEnglish(test);

	}

	private void printByConsoleEnglish(byte[] buffer)
	{
		int key[] =
		{ 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };
		for (int len = 0; len < 8; len++)
		{
			short byteDate = buffer[len];
			for (int i = 0; i < 8; i++)
			{
				int flag = byteDate & key[i];
				System.out.printf("%s", flag == 0 ? " " : "��");
			}
			System.out.println();
		}
		System.out.println("************************");
	}

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

}
