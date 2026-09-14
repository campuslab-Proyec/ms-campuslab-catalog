# MS CampusLab Notify

Microservicio encargado del procesamiento y envío de notificaciones dentro del ecosistema CampusLab. Consume eventos y mensajes en segundo plano (publicados por servicios como el catálogo) para despachar alertas, correos o logs de notificación.

## 🛠️ Tecnologías Utilizadas

* **Java** 21+ / Java 25
* **Spring Boot** 4.x / 3.x
* **Spring AMQP / RabbitMQ** (Consumo de eventos de mensajería)
* **Jackson** (Deserialización de mensajes JSON)
* **Java Mail Sender** (Opcional, si gestiona envío de emails)
* **Apache Maven** (Gestor de dependencias y construcción)

## 📋 Requisitos Previos

Asegúrate de contar con lo siguiente instalado y en ejecución en tu entorno local:

1. **JDK 21** o superior instalado.
2. **RabbitMQ Server** activo en sus puertos predeterminados (`5672` / `15672`).
3. **Maven** instalado (o usar el wrapper `./mvnw` / `mvnw.cmd` incluido en el proyecto).

## 🚀 Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd ms-campuslab-notify
Configurar el servidor de mensajería:
Verifica la configuración de conexión con RabbitMQ en el archivo src/main/resources/application.yaml:

YAML
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
Compilar el proyecto:

Bash
mvn clean compile
Ejecutar la aplicación:

Bash
mvn spring-boot:run
El microservicio iniciará la escucha activa de la cola/exchange configurada en RabbitMQ.