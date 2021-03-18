public class Weather {
    String city;
    double temperature;
    double pressure;
    WindDirection windDirection;
    double windSpeed;
    double cloudCover;
    enum WindDirection { E, N, W, S, SW, SE, NE, NW };

    public Weather(String city, double temperature, double pressure, WindDirection windDirection, double windSpeed, double cloudCover) {
        this.city = city;
        this.temperature = temperature;
        this.pressure = pressure;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.cloudCover = cloudCover;
    }
    static public Weather parseToWeather(String jsonString)
    {
        String city;
        double temperature;
        double pressure;
        Weather.WindDirection windDirection;
        double windSpeed;
        double cloudCover;

        String[] jsonArray = jsonString.split(",");

        city = jsonArray[1].substring(9) + " " + jsonArray[2].substring(0, jsonArray[2].length() - 1);
        temperature = Double.parseDouble(jsonArray[15].substring(14));
        pressure = Double.parseDouble(jsonArray[22].substring(11));
        windSpeed = Double.parseDouble(jsonArray[19].substring(13));
        cloudCover = Double.parseDouble(jsonArray[25].substring(13));
        windDirection = parseWD(jsonArray[21].substring(12, jsonArray[21].length() - 1));

        return new Weather(city, temperature, pressure, windDirection, windSpeed, cloudCover);
    }

    static private Weather.WindDirection parseWD(String WDStr){

        return switch (WDStr) {
            case "E" -> Weather.WindDirection.E;
            case "W" -> Weather.WindDirection.W;
            case "S" -> Weather.WindDirection.S;
            case "SW" -> Weather.WindDirection.SW;
            case "SE" -> Weather.WindDirection.SE;
            case "NE" -> Weather.WindDirection.NE;
            case "NW" -> Weather.WindDirection.NW;
            default -> Weather.WindDirection.N;
        };
    }



    @Override
    public String toString() {

        return "Weather for " + city +
                ":\nTemperature is " + temperature +
                " C\nPressure is " + pressure +
                "\nWind speed is " + windSpeed +
                " mps\nWind direction is " + windDirection +
                "\nCloud cover is " + cloudCover + "%";

    }

}
