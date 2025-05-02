// src/ConsultaTasasCambio.java

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Cliente HTTP para la ExchangeRate-API.
 * Si no halla la variable de entorno EXCHANGE_API_KEY,
 * usa la clave de ejemplo para evitar errores 403 en desarrollo.
 */
public class ConsultaTasasCambio {

    // Variable de entorno recomendada
    private static final String ENV_KEY = System.getenv("EXCHANGE_API_KEY");
    // Fallback (solo para desarrollo, no lo subas a producción)
    private static final String DEFAULT_KEY = "018441f6a7d02244f8e21bae";
    private static final String API_KEY = (ENV_KEY != null && !ENV_KEY.isBlank())
            ? ENV_KEY
            : DEFAULT_KEY;

    // Imprimimos la clave que estamos usando para diagnóstico
    // static {
    //    System.out.println("API_KEY leída: '" + API_KEY + "'");
    // }

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/"
            + API_KEY + "/latest/";

    private final HttpClient httpClient;  // Cliente HTTP reutilizable
    private final Gson       gson;        // Gson para JSON

    public ConsultaTasasCambio() {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        this.gson = new Gson();
    }

    /**
     * Solicita las tasas para la moneda indicada y devuelve el modelo.
     */
    public ConversionRatesResponse obtieneTasas(String monedaBase) {
        HttpResponse<String> response = realizaPeticion(monedaBase);

        System.out.println("Status code: " + response.statusCode());
        response.headers().map().forEach((k, v) ->
                System.out.printf("Header %s = %s%n", k, v)
        );

        String jsonBody = response.body();
        try {
            ConversionRatesResponse modelo =
                    gson.fromJson(jsonBody, ConversionRatesResponse.class);

            JsonObject root  = JsonParser.parseString(jsonBody).getAsJsonObject();
            JsonObject rates = root.getAsJsonObject("conversion_rates");
            double eurRate   = rates.get("EUR").getAsDouble();
            System.out.printf(">> Ejemplo: 1 %s = %f EUR%n",
                    modelo.base_code(), eurRate);

            return modelo;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Error parseando JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Construye y envía la petición HTTP GET.
     */
    private HttpResponse<String> realizaPeticion(String monedaBase) {
        URI uri = URI.create(BASE_URL + monedaBase);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error HTTP: código " + response.statusCode());
            }
            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Fallo en petición HTTP: " + e.getMessage(), e);
        }
    }
}
