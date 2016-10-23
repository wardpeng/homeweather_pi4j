package jsonDemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Person
{

	public void setId(int int1)
	{
		// TODO 自动生成的方法存根

	}

	public void setName(String string)
	{
		// TODO 自动生成的方法存根

	}

	public void setAddress(String string)
	{
		// TODO 自动生成的方法存根

	}

}

public class HttpUtil
{
	public static Person getPerson(String jsonStr)
	{
		Person person = new Person();
		try
		{// 将json字符串转换为json对象
			JSONObject jsonObj = new JSONObject(jsonStr);
			// 得到指定json key对象的value对象
			JSONObject personObj = jsonObj.getJSONObject("person");
			// 获取之对象的所有属性
			person.setId(personObj.getInt("id"));
			person.setName(personObj.getString("name"));
			person.setAddress(personObj.getString("address"));
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return person;
	}

	public static List<Person> getPersons(String jsonStr)
	{
		List<Person> list = new ArrayList<Person>();

		JSONObject jsonObj;
		try
		{// 将json字符串转换为json对象
			jsonObj = new JSONObject(jsonStr);
			// 得到指定json key对象的value对象
			JSONArray personList = jsonObj.getJSONArray("persons");
			// 遍历jsonArray
			for (int i = 0; i < personList.length(); i++)
			{
				// 获取每一个json对象
				JSONObject jsonItem = personList.getJSONObject(i);
				// 获取每一个json对象的值
				Person person = new Person();
				person.setId(jsonItem.getInt("id"));
				person.setName(jsonItem.getString("name"));
				person.setAddress(jsonItem.getString("address"));
				list.add(person);
			}
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static String getJsonContent(String urlStr)
	{
		try
		{// 获取HttpURLConnection连接对象
			URL url = new URL(urlStr);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// 设置连接属性
			httpConn.setConnectTimeout(3000);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("GET");
			// 获取相应码
			int respCode = httpConn.getResponseCode();
			if (respCode == 200)
			{
				return ConvertStream2Json(httpConn.getInputStream());
			}
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private static String ConvertStream2Json(InputStream inputStream)
	{
		String jsonStr = "";
		// ByteArrayOutputStream相当于内存输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// 将输入流转移到内存输出流中
		try
		{
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
			{
				out.write(buffer, 0, len);
			}
			// 将内存流转换为字符串
			jsonStr = new String(out.toByteArray());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}
}