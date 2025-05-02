// src/Principal.java

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Consola interactiva del conversor de monedas.
 * - Fase 8: filtra monedas soportadas
 * - Fase 9: realiza conversión y almacena historial
 * - Fase 10: menú, validación de opciones y muestra de historial
 */
public class Principal {

    /** Registro de cada conversión con timestamp. */
    public static record ConversionRecord(
            String fromCurrency,
            String toCurrency,
            double originalAmount,
            double convertedAmount,
            LocalDateTime timestamp
    ) {}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el código de la moneda base (ej. USD, EUR, ARS): ");
        String baseCurrency = scanner.nextLine().trim().toUpperCase();

        ConsultaTasasCambio servicio = new ConsultaTasasCambio();
        ConversionRatesResponse response;
        try {
            response = servicio.obtieneTasas(baseCurrency);
        } catch (RuntimeException e) {
            System.err.println("¡Error obteniendo tasas!: " + e.getMessage());
            scanner.close();
            return;
        }

        // Fase 8: monedas iniciales soportadas
        String[] supported = {"ARS","BOB","BRL","CLP","COP","USD"};

        // Historial de conversiones (opcional)
        List<ConversionRecord> history = new ArrayList<>();

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- MENÚ DE CONVERSIÓN ---");
            for (int i = 0; i < supported.length; i++) {
                double rate = response.conversion_rates().getOrDefault(supported[i], 0.0);
                System.out.printf("%d. %s (tasa: %f)%n", i+1, supported[i], rate);
            }
            System.out.printf("%d. Salir%n", supported.length + 1);
            System.out.print("Seleccione una opción válida: ");

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                continue;
            }

            if (opcion == supported.length + 1) {
                exit = true;
                System.out.println("Gracias por usar el conversor. Hasta luego!");
                break;
            }
            if (opcion < 1 || opcion > supported.length) {
                System.out.println("Opción inválida. Intente de nuevo.");
                continue;
            }

            String target = supported[opcion - 1];
            double rate   = response.conversion_rates().getOrDefault(target, 0.0);

            // Fase 9: pedir monto y convertir
            System.out.printf("Ingrese monto en %s: ", baseCurrency);
            double amount;
            try {
                amount = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Monto inválido. Intente de nuevo.");
                continue;
            }
            double converted = amount * rate;
            System.out.printf("%.2f %s = %.2f %s%n",
                    amount, baseCurrency, converted, target);

            // Agregar al historial con timestamp
            history.add(new ConversionRecord(
                    baseCurrency, target, amount, converted, LocalDateTime.now()
            ));
        }

        // Mostrar historial al finalizar
        if (!history.isEmpty()) {
            System.out.println("\n=== Historial de Conversiones ===");
            for (ConversionRecord rec : history) {
                System.out.printf("[%s] %.2f %s → %.2f %s%n",
                        rec.timestamp(), rec.originalAmount(),
                        rec.fromCurrency(), rec.convertedAmount(),
                        rec.toCurrency());
            }
        }

        scanner.close();
    }
}
