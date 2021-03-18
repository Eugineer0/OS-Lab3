import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server extends Thread{
    private final int PORT = 8080;
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    public void run() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Weather active on " + PORT);
            boolean shutdown = true;

            CopyOnWriteArrayList<String> cashedRequests = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<Weather> cashedWeather = new CopyOnWriteArrayList<>();


            while (shutdown) {
                Socket socket = server.accept();
                InputStream is = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line;
                System.out.println(in.readLine());

                // looks for post data
                int postDataI = -1;
                while ((line = in.readLine()) != null && (line.length() != 0)) {
                    System.out.println(line);
                    if (line.contains("Content-Length:")) {
                        postDataI = Integer.parseInt(line
                                .substring(
                                        line.indexOf("Content-Length:") + 16));
                    }
                }
                StringBuilder postData = new StringBuilder();
                for (int i = 0; i < postDataI; i++) {
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
                out.println("<H2>Request: " + (city = (postData.length() == 0? postData.toString() : postData.toString().substring(5))) + "</H2>");
                out.println("<form name=\"input\"  method=\"post\">");
                out.println("City: <input type=\"text\" name=\"City\"><input type=\"submit\" value=\"Submit\"></form>");
                city = city.toLowerCase();
                int index;
                out.println((city.length() == 0? "there will be information about weather" :
                        (index = cashedRequests.indexOf(city, 0)) > -1? getFromArray((cashedWeather.get(index).toString() + "\nget from cash").split("\n")) :
                                getFromArray((currentRequest = Weather.parseToWeather(Connection.getWeatherString(city))).toString().split("\n"))));

                if(currentRequest != null){
                    cashedRequests.add(city);
                    cashedWeather.add(currentRequest);
                }

                out.close();
                socket.close();
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String getFromArray(String[] strings){
    StringBuilder stringBuilder = new StringBuilder();
        for (String str: strings) {
            stringBuilder.append("<p>").append(str).append("</p>");
        }


        return stringBuilder.toString();
    }

}