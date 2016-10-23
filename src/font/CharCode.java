package font;

public class CharCode
{
	public CharCode()
	{
		String str = "Œ“";
		try
		{
			byte[] b = str.getBytes("GBK");
			for (int i = 0; i < b.length; i++)
			{
				System.out.println(Integer.toHexString(b[i]));
			}
		} catch (Exception ex)
		{
		}
	}

	public static void main(String[] args)
	{
		CharCode charcode = new CharCode();
	}
}