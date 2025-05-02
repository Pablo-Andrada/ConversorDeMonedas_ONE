// src/ConversionRatesResponse.java


import java.util.Map;

/**
 * Modelo que refleja la respuesta JSON de la ExchangeRate-API
 * al solicitar las tasas de conversión más recientes.
 */
public record ConversionRatesResponse(
        String result,                     // "success" o "error"
        String documentation,              // URL de la documentación
        String terms_of_use,               // URL de los términos de uso
        long time_last_update_unix,        // Timestamp Unix última actualización
        String time_last_update_utc,       // Fecha UTC legible última actualización
        long time_next_update_unix,        // Timestamp Unix próxima actualización
        String time_next_update_utc,       // Fecha UTC legible próxima actualización
        String base_code,                  // Código ISO de la moneda base
        Map<String, Double> conversion_rates // Mapa de códigos de moneda → tasa
) {
    // Java genera automáticamente constructor, getters, toString(), equals(), hashCode().
}
