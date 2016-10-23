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
		// TODO �Զ����ɵķ������

	}

	public void setName(String string)
	{
		// TODO �Զ����ɵķ������

	}

	public void setAddress(String string)
	{
		// TODO �Զ����ɵķ������

	}

}

public class HttpUtil
{
	public static Person getPerson(String jsonStr)
	{
		Person person = new Person();
		try
		{// ��json�ַ���ת��Ϊjson����
			JSONObject jsonObj = new JSONObject(jsonStr);
			// �õ�ָ��json key�����value����
			JSONObject personObj = jsonObj.getJSONObject("person");
			// ��ȡ֮�������������
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
		{// ��json�ַ���ת��Ϊjson����
			jsonObj = new JSONObject(jsonStr);
			// �õ�ָ��json key�����value����
			JSONArray personList = jsonObj.getJSONArray("persons");
			// ����jsonArray
			for (int i = 0; i < personList.length(); i++)
			{
				// ��ȡÿһ��json����
				JSONObject jsonItem = personList.getJSONObject(i);
				// ��ȡÿһ��json�����ֵ
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
		{// ��ȡHttpURLConnection���Ӷ���
			URL url = new URL(urlStr);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// ������������
			httpConn.setConnectTimeout(3000);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("GET");
			// ��ȡ��Ӧ��
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
		// ByteArrayOutputStream�൱���ڴ������
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// ��������ת�Ƶ��ڴ��������
		try
		{
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
			{
				out.write(buffer, 0, len);
			}
			// ���ڴ���ת��Ϊ�ַ���
			jsonStr = new String(out.toByteArray());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}
}