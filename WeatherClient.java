import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherClient {

    public static void main(String[] args) {
        // Tokyo coordinates
        String url = "https://api.open-meteo.com/v1/forecast?latitude=35&longitude=139&current_weather=true";

        try {
            // Create HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Create GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Send request and get response as string
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            // Extract current weather data using simple string methods
            String weatherSection = json.substring(json.indexOf("\"current_weather\""));

            String temperature = extractValue(weatherSection, "temperature");
            String windspeed = extractValue(weatherSection, "windspeed");
            String time = extractValue(weatherSection, "time");

            // Print formatted output
            System.out.println("🌤️  Current Weather in Tokyo (Lat: 35, Lon: 139)");
            System.out.println("Temperature: " + temperature + "°C");
            System.out.println("Windspeed: " + windspeed + " km/h");
            System.out.println("Time: " + time);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Simple string extractor method
    private static String extractValue(String json, String key) {
        int startIndex = json.indexOf("\"" + key + "\":") + key.length() + 3;
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex); // for last element
        }
        return json.substring(startIndex, endIndex).replaceAll("\"", "").trim();
    }
}
