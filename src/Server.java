import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server extends Thread{
    private final int PORT = 8080;
    private final int capacity = 64;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void run() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Weather active on " + PORT);
            boolean shutdown = true;

            ConcurrentLinkedQueue<String> cachedRequestsQueue = new ConcurrentLinkedQueue<>();
            ConcurrentLinkedQueue<Weather> cachedWeatherQueue = new ConcurrentLinkedQueue<>();

            CopyOnWriteArrayList<String> cachedRequests = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<Weather> cachedWeather = new CopyOnWriteArrayList<>();

            while (shutdown) {
                Socket socket = server.accept();
                InputStream is = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
               
                String line;
                // looks for post data
                int contentLength = -1;
                while ((line = in.readLine()) != null && (line.length() != 0)) {
                    System.out.println(line);
                    if (line.contains("Content-Length")) {
                        contentLength = getNumFromStr(line);
                    }
                }
                StringBuilder postData = new StringBuilder();
                for (int i = 0; i < contentLength; i++) {
                    int intParser = in.read();
                    postData.append((char) intParser);
                }

                Weather currentRequest = null;
                String city;

                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html; charset=utf-8");
                out.println("Server: Weather Server");
                // this blank line signals the end of the headers
                out.println("");
                // Send the HTML page
                out.println("<H1>Welcome to the Weather server</H1>");
                out.println("<H2>Request: " + (city = (contentLength < 0 ? postData.toString() : postData.substring(5))) + "</H2>");
                out.println("<form name=\"input\"  method=\"post\">");
                out.println("City: <input type=\"text\" name=\"City\"><input type=\"submit\" value=\"Submit\"><input type=\"submit\" value=\"Clear\"></form>");
                city = city.toLowerCase();

                int index;
                out.println((city.length() == 0 ? "there will be information about weather" :
                        (index = cachedRequests.indexOf(city, 0)) > -1 ? getFromArray((cachedWeather.get(index).toString() + "\nget from cache").split("\n")) :
                                getFromArray((currentRequest = Weather.parseWeather(Connection.getWeatherString(city))).toString().split("\n"))));

                if(currentRequest != null) {
                    if(cachedRequests.size() > capacity) {
                        cachedRequests.remove(0);
                        cachedWeather.remove(0);
                    }
                    cachedRequests.add(city);
                    cachedWeather.add(currentRequest);
                }

                out.close();
                socket.close();
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static int getNumFromStr(String str)
    {
        String regex = "[1-9][0-9]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        matcher.find();
        return Integer.parseInt(matcher.group());
    }

    static String getFromArray(String[] strings){
    StringBuilder stringBuilder = new StringBuilder();
        for (String str: strings) {
            stringBuilder.append("<p>").append(str).append("</p>");
        }


        return stringBuilder.toString();
    }

}