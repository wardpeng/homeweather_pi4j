package Yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Url
{

	public static void main(String[] args)
	{
		String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid=26198084%20and%20u=\"c\"&format=json";
		String json = loadJson(url);
		System.out.println(json);
	}

	public static String loadJson(String url)
	{
		StringBuilder json = new StringBuilder();
		try
		{
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null)
			{
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return json.toString();
	}

}
