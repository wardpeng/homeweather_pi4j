import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YahooWeather
{
	public static void main(String[] args)
	{

		try
		{
			Weather weather = new Weather("26198084");// zaoZhuang
			// System.out.println(weather);
			weather.showJsonArrayForecasts();
			// System.out.println(weather.getChannel());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		// writeFile("D:\\result.json", jsonString);
		// System.out.println(readFile("D:\\result.json"));
	}

}

class Weather
{
	private String lastBuildDate;// lastBuildDate : "Sat, 15 Oct 2016 09:29 PM
	// CST"
	private int humidity; // humidity : "95"
	private double pressure; // pressure : "34100.95"
	private double visibility; // visibility : "16.25"
	private String sunrise; // sunrise : "6:17 am"
	private String sunset; // sunset : "5:33 pm"
	private String windSpeed; // speed : "22.53"

	private JSONObject jsonObjectChannel;
	private String strJsonAllData;
	private JSONArray jsonArrayForecasts = new JSONArray();
	private Forecast[] forecastsArray = new Forecast[10];

	/**
	 * incorrding to woeid input, get valid data: jsonObjectChannel and
	 * jsonAllData
	 * 
	 * @param strWoeid
	 * @throws JSONException
	 */
	public Weather(String strWoeid) throws JSONException// 26198084
	{
		String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid="
				+ strWoeid + "%20and%20u=\"c\"&format=json";
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
		strJsonAllData = json.toString();
		setChannel(strJsonAllData);// get Channel
		setForecasts();// Get JSONArray:jsonArrayForecasts
		// acordding to jsonObjectChannel
		setOtherInfo();// Get other weather information
	}

	@Override
	public String toString()
	{
		return "strJsonAllData:" + strJsonAllData;
	}

	public JSONObject getChannel()
	{
		return jsonObjectChannel;
	}

	private void setChannel(String strJson) throws JSONException
	{

		JSONObject jsonObjectAll = new JSONObject(strJson);
		// get Channel:query->results->channel
		jsonObjectChannel = jsonObjectAll.getJSONObject("query").getJSONObject("results").getJSONObject("channel");
	}

	private void setForecasts() throws JSONException
	{
		// get forecast[]
		jsonArrayForecasts = jsonObjectChannel.getJSONObject("item").getJSONArray("forecast");
		for (int i = 0; i < forecastsArray.length; i++)
		{
			forecastsArray[i] = new Forecast(jsonArrayForecasts.getJSONObject(i));
		}
	}

	private void setOtherInfo() throws JSONException
	{
		/* get ohterinformation: channel->..... */

		JSONObject jsonObjectAtmosphere = jsonObjectChannel.getJSONObject("atmosphere");
		humidity = jsonObjectAtmosphere.getInt("humidity");
		pressure = jsonObjectAtmosphere.getDouble("pressure");
		visibility = jsonObjectAtmosphere.getDouble("visibility");

		sunrise = jsonObjectChannel.getJSONObject("astronomy").getString("sunrise");
		sunset = jsonObjectChannel.getJSONObject("astronomy").getString("sunset");

		windSpeed = jsonObjectChannel.getJSONObject("wind").getString("speed");

	}

	public void showJsonArrayForecasts() throws JSONException
	{
		for (int i = 0; i < jsonArrayForecasts.length(); i++)
		{
			JSONObject jsonObject = jsonArrayForecasts.getJSONObject(i);
			System.out.println("Forecast[" + i + "]:" + jsonObject);
		}
	}

	public void writeFile(String filePath, String content)
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

	public String readFile(String filePath)
	{
		String content = "";
		try
		{
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists())
			{
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// note:
																									// codeing
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

class Forecast
{
	private String date;// date : "15 Oct 2016"
	private int high;// high : "17"
	private int code;// code : "11"
	private int low;// low : "15"
	private String text;// text : "Showers"
	private String day;// day : "Sat"

	public Forecast(JSONObject forecastJSonObj) throws JSONException//
	{

		date = forecastJSonObj.getString("date");
		high = forecastJSonObj.getInt("high");
		low = forecastJSonObj.getInt("low");
		code = forecastJSonObj.getInt("code");
		text = forecastJSonObj.getString("text");
		day = forecastJSonObj.getString("day");
	}

	private String getForecast()
	{
		return "{" + date + "," + high + "," + low + "," + code + "," + text + "," + day + "}";
	}

	@Override
	public String toString()
	{
		return getForecast();
	}

}
