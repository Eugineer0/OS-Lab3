import java.io.*;
import java.net.*;

public class Connection extends Thread{

    private static HttpURLConnection connection;

    public static String getWeatherString(String GETRequestString)
    {
        BufferedReader reader;

        StringBuilder responseContent = new StringBuilder();

        try {
            URL url = new URL("http://api.weatherstack.com/current" +
                    "?access_key=14eb52408a24e9e04134be4d7053ac59" +
                    "&query="+GETRequestString.toLowerCase());
            connection = (HttpURLConnection) url.openConnection();

            //request
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            reader = new BufferedReader(new InputStreamReader(status == HttpURLConnection.HTTP_OK ?  connection.getInputStream() : connection.getErrorStream()));

            String line;
            while((line = reader.readLine())!= null) {
                responseContent.append(line);
            }

            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return responseContent.toString();
    }
}
