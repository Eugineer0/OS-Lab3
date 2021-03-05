import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    private static HttpURLConnection connection;
    private static /*NonBlocking*/ArrayList<Weather> cashed = new /*NonBlocking*/ArrayList<>();

    public static void main(String[] args)  {

        BufferedReader reader;

        StringBuffer responseContent = new StringBuffer();

        Scanner scanner = new Scanner(System.in);
        String city = scanner.nextLine().toLowerCase();

        //if(небыло такого города) {
        try {
            URL url = new URL("http://api.weatherstack.com/current" +
                    "?access_key=14eb52408a24e9e04134be4d7053ac59" +
                    "&query="+city);
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
            System.out.println(responseContent.toString());

            // if (status == HttpURLConnection.HTTP_OK) { кешируем }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        // } else { вывести соответсвующкю запись из cashed }
    }
    private static class Weather
    {
        String city;
        double temperature;
        double pressure;
        WindDirection windDirection;
        double windSpeed;
        double cloudCover;

        enum WindDirection { E, N, W, S, SW, SE, NE, NW };

        @Override
        public String toString() {
            return "Weather for " + city +
                    ":\ntemperature is " + temperature +
                    "C\npressure is " + pressure +
                    "\nwindSpeed is " + windSpeed +
                    "mps\nwindDirection is " + windDirection +
                    "\ncloudCover is " + cloudCover + "%";

        }
    }

}