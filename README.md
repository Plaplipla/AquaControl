# 🧪 AquaControl - Medición de Cloro en Línea

**Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles  
**Evaluación:** Parcial 2 (Encargo)  
**Integrantes:**
* Cristóbal Zúñiga
* Valeria Rodríguez

## 📝 Descripción del Proyecto
AquaControl es una aplicación móvil nativa para Android diseñada para digitalizar la medición y el monitoreo de la calidad del agua (cloro libre, pH, temperatura y turbidez) en granjas avícolas. La aplicación reemplaza el uso de planillas de papel o Excel, permitiendo a los operarios registrar mediciones en terreno (incluso sin conexión a internet) y a los supervisores/jefaturas gestionar desviaciones, alertas y reportes de manera centralizada.

## 🚀 Funcionalidades Principales

* **Sistema de Roles Diferenciados:**
    * **Operario:** Registra mediciones manuales, adjunta evidencia fotográfica y ve solo sus granjas asignadas.
    * **Supervisor:** Monitorea puntos, atiende alertas críticas y gestiona desviaciones (validando que los nuevos niveles de agua estén dentro del rango permitido).
    * **Jefatura:** Consulta reportes consolidados, gráficos comparativos, historial global (24h, 7d, 30d) y auditoría de acciones correctivas.
* **Persistencia y Modo Offline:** Utiliza la base de datos Room (SQLite) para almacenar la información localmente. Si no hay internet, los datos se guardan como "Pendientes" y se pueden sincronizar con un botón.
* **Recursos Nativos:** Integración con Cámara y Galería para adjuntar evidencia fotográfica a las mediciones y resoluciones.
* **Formularios Validados:** Validación estricta de rangos químicos (Cloro 3.0 - 5.0 ppm, pH 6.5 - 8.0) antes de permitir cerrar una desviación.
* **Diseño UI/UX:** Interfaz moderna con Jetpack Compose, animaciones de retroalimentación, tema personalizado y diseño adaptable.

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje:** Kotlin
* **Interfaz Gráfica:** Jetpack Compose + Material 3
* **Arquitectura:** MVVM (Model-View-ViewModel)
* **Base de Datos Local:** Room (SQLite)
* **Navegación:** Navigation Compose
* **Gestión de Estado:** StateFlow y Corrutinas
* **Librerías adicionales:** Coil (carga de imágenes), DataStore Preferences (sesión).

## 💻 Requisitos e Instalación

Para ejecutar este proyecto localmente, necesitas:
* **Android Studio** (Versión Hedgehog 2023.1.1 o superior recomendada).
* **JDK 17** configurado en Android Studio.
* Un emulador de Android (API 24 o superior) o un dispositivo físico.

### Pasos para ejecutar:
1. Clona este repositorio con el comando de git clone.
2. Abre el proyecto en Android Studio.
3. Espera a que Gradle sincronice todas las dependencias.
4. Ejecuta la aplicación presionando el botón **Run (▶️)**.

## 🔑 Credenciales de Prueba

Para probar las diferentes vistas y funcionalidades, utiliza las siguientes credenciales en la pantalla de Login:

| Rol | Correo | Contraseña |
| :--- | :--- | :--- |
| **Operario** | `operario@empresa.cl` | `123456` |
| **Supervisor** | `supervisor@empresa.cl` | `123456` |
| **Jefatura** | `jefatura@empresa.cl` | `123456` |

---
*Proyecto desarrollado para la asignatura DSY1105 de la Escuela de Informática y Telecomunicaciones.*

---