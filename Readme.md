# Conversor de Monedas Java

**Descripción**

Proyecto de un conversor de monedas en Java 17 que:

* Consulta tasas de cambio en tiempo real desde la ExchangeRate-API (v6).
* Permite filtrar un conjunto de monedas (ARS, BOB, BRL, CLP, COP, USD).
* Realiza conversiones interactivas en consola.

Este README explica cómo configurar, compilar y ejecutar la aplicación, así como su estructura y utilidad.

---

## Contenido

1. [Características](#características)
2. [Requisitos](#requisitos)
3. [Instalación](#instalación)
4. [Uso](#uso)
5. [Estructura de Archivos](#estructura-de-archivos)
6. [Configuración de API Key](#configuración-de-api-key)
7. [Licencia y Créditos](#licencia-y-créditos)

---

## Características

* Cliente HTTP basado en `HttpClient` de Java 11+
* Manejo de redirecciones, timeouts y validación de estado HTTP
* Deserialización de JSON a objetos Java con Gson
* Menú en consola con validación de entradas
* Ejemplo de parseo manual con `JsonParser`

---

## Requisitos

* Java Development Kit (JDK) 17
* [Gson 2.13.1](https://github.com/google/gson) (JAR incluido en `/lib`)
* Conexión a internet para consumir la API de ExchangeRate

---

## Instalación

1. Clona o descarga este repositorio en tu máquina.
2. Asegúrate de la siguiente estructura:

   ```
   ONE_ChallengeConversorMonedas/
   ├── lib/
   │   └── gson-2.13.1.jar
   └── src/    
       ├── ConversionRatesResponse.java
       ├── ConsultaTasasCambio.java
       └── Principal.java
   ```
3. Marca `src/` como Source Root en IntelliJ IDEA (o configura tu IDE).
4. Añade `gson-2.13.1.jar` al classpath o a las dependencias de tu módulo.

---

## Uso

### Compilación (línea de comandos)

```bash
# Desde la carpeta raíz del proyecto
dkdir bin
javac -cp lib/gson-2.13.1.jar -d bin src/conversor/*.java
```

### Ejecución

```bash
java -cp "bin;lib/gson-2.13.1.jar" conversor.Principal
```

1. Ingresa el código de la moneda base (ej. `USD`).
2. Elige la moneda destino del menú.
3. Escribe el monto a convertir.
4. Obtén el resultado formateado.

---

## Estructura de Archivos

* **src/**

    * `ConversionRatesResponse.java` → Modelo `record` para el JSON de la API.
    * `ConsultaTasasCambio.java` → Cliente HTTP, gestión de solicitudes y parseo.
    * `Principal.java` → Menú interactivo y lógica de conversión.
* **lib/**

    * `gson-2.13.1.jar` → Biblioteca para JSON.

---

## Configuración de API Key

> **Nota:** La API Key incluida en el código (`018441f6a7d02244f8e21bae`) es únicamente de ejemplo.

Para usar tu propia API Key de ExchangeRate-API:

1. Regístrate en [https://www.exchangerate-api.com/](https://www.exchangerate-api.com/) y obtén tu clave.

2. Guarda la clave en una variable de entorno (recomendado) o en un archivo de configuración local (`config.properties` o similar) que **no** subas al repositorio.

3. En `ConsultaTasasCambio.java`, reemplaza la constante:

   ```java
   // Ejemplo de clave de ejemplo: cámbiala por tu variable de entorno o propiedad
   private static final String API_KEY = System.getenv("EXCHANGE_API_KEY");
   ```

4. Asegúrate de exportar la variable antes de ejecutar tu aplicación:

   **Windows (PowerShell)**

   ```powershell
   $Env:EXCHANGE_API_KEY = "TU_CLAVE_AQUI"
   java -cp "bin;lib/gson-2.13.1.jar" conversor.Principal
   ```

   **Linux/macOS**

   ```bash
   export EXCHANGE_API_KEY="TU_CLAVE_AQUI"
   java -cp "bin:lib/gson-2.13.1.jar" conversor.Principal
   ```

---

## Licencia y Créditos

* Proyecto desarrollado para el desafío ONE Program.
* API de ExchangeRate: [https://www.exchangerate-api.com/](https://www.exchangerate-api.com/)
* JSON parsing: [Gson](https://github.com/google/gson)

---

*¡Gracias por revisar este proyecto!*
