package Yahoo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class RWFile
{
	public static void main(String[] args)
	{
		writeFile("D:\\result.json",
				"{\"1\":{\"1\":{\"jhinfo\":[\"Plane1\",\"zhi\",\"www.zhixing123.cn\"],\"jhrate\":[\"1-5:10.0\",\"6-100:5.0/1\"]},\"2\":{\"jhinfo\":[\"Plane2\",\"zhi\",\"www.zhixing123.cn\"],\"jhrate\":[\"1-100:100.0\"]},\"3\":{\"jhinfo\":[\"Plane3\",\"zhi\",\"www.zhixing123.cn\"],\"jhrate\":[\"1-100:150.0/7\"]}},\"2\":{\"4\":{\"jhinfo\":[\"Plane\",\"zhi\",\"www.zhixing123.cn\"],\"jhrate\":[\"365-365:1000.0\"]}}}");
		System.out.println(readFile("D:\\result.json"));
	}

	public static void writeFile(String filePath, String content)
	{
		try
		{
			// String content = "This is the content to write into file";
			File file = new File(filePath);
			// if file doesnt exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			System.out.println("Write to file Done!!!");
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static String readFile(String filePath)
	{
		String content = "";
		try
		{
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists())
			{
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// ¿¼ÂÇµ½±àÂë¸ñÊ½
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null)
				{
					content += lineTxt;
					// System.out.println(lineTxt);
				}
				read.close();
			} else
			{
				System.out.println("No file");
			}
		} catch (Exception e)
		{
			System.out.println("Error in reading file");
			e.printStackTrace();
		}
		return content;

	}
}
