public class Weather {
    String city;
    double temperature;
    double pressure;
    double windSpeed;
    double cloudCover;
    String windDirection;

    public Weather(String city, double temperature, double pressure, String windDirection, double windSpeed, double cloudCover) {
        this.city = city;
        this.temperature = temperature;
        this.pressure = pressure;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.cloudCover = cloudCover;
    }
    static public Weather parseWeather(String jsonString)
    {
        String city;
        double temperature;
        double pressure;
        String windDirection;
        double windSpeed;
        double cloudCover;

        String[] jsonArray = jsonString.split(",");

        city = jsonArray[1].substring(9) + " " + jsonArray[2].substring(0, jsonArray[2].length() - 1);
        temperature = Double.parseDouble(jsonArray[15].substring(14));
        pressure = Double.parseDouble(jsonArray[22].substring(11));
        windSpeed = Double.parseDouble(jsonArray[19].substring(13));
        cloudCover = Double.parseDouble(jsonArray[25].substring(13));
        windDirection = jsonArray[21].substring(12, jsonArray[21].length() - 1);

        return new Weather(city, temperature, pressure, windDirection, windSpeed, cloudCover);
    }

    @Override
    public String toString() {
        return  "Weather for " + city + ":\n" +
                "Temperature is " + temperature + " C\n" +
                "Pressure is " + pressure + " mBar\n" +
                "Wind speed is " + windSpeed + " mps\n" +
                "Wind direction is " + windDirection + "\n" +
                "Cloud cover is " + cloudCover + "%\n";

    }

}
