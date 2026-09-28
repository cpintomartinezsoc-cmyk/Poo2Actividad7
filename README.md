<img width="488" height="157" alt="image" src="https://github.com/user-attachments/assets/9687512f-3b36-46c6-aacc-45c480cae33b" />

# 🗄️ Actividad Formativa – Conectando aplicaciones Java con bases de datos mediante JDBC

---

## 👤 Datos del estudiante

**Nombre:** Camilo Pinto

**Carrera:** Analista Programador

**Asignatura:** Desarrollo Orientado a Objetos II

**Semana:** 7

**Caso:** SpeedFast

---

## 📌 Descripción

En esta actividad se continúa desarrollando el sistema de entregas **SpeedFast**, incorporando **persistencia de datos** mediante una base de datos **MySQL** y la API **JDBC**.

En las semanas anteriores, los pedidos se almacenaban en memoria (`ArrayList`), por lo que se perdían al cerrar la aplicación. Ahora, la información de **pedidos**, **repartidores** y **entregas** se guarda y consulta directamente en la base de datos.

La interfaz gráfica (Swing) desarrollada en la semana 6 se integró con la base de datos, de modo que los formularios registran la información en tiempo real y las tablas (`JTable`) muestran los datos almacenados.

---

## 🛠️ Tecnologías utilizadas

* **Java 17+**
* **Maven**
* **MySQL 8** y **MySQL Workbench**
* **JDBC** (`mysql-connector-j` 9.3.0)
* **Java Swing** (interfaz gráfica)
* **IntelliJ IDEA**

---

## 🗃️ Modelo de base de datos

La base de datos `speedfast_db` está compuesta por tres tablas:

| Tabla | Descripción |
|-------|-------------|
| `repartidor` | Almacena los repartidores registrados. |
| `pedido` | Almacena los pedidos con su dirección, tipo y estado. |
| `entrega` | Relaciona un pedido con un repartidor, registrando la fecha y la hora. |

### Relaciones

* Un **repartidor** puede realizar muchas **entregas**.
* Un **pedido** puede tener una o varias **entregas**.
* Cada **entrega** se asocia a un **pedido** y a un **repartidor** mediante llaves foráneas.

### Script SQL

```sql
CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE repartidor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(150) NOT NULL,
    tipo VARCHAR(30) NOT NULL,     -- COMIDA | ENCOMIENDA | EXPRESS
    estado VARCHAR(20) NOT NULL    -- PENDIENTE | EN_REPARTO | ENTREGADO
);

CREATE TABLE entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_repartidor INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_repartidor) REFERENCES repartidor(id)
);
```

---

## 🏗️ Estructura del proyecto

```text
src/main/java
│
├── cl.duoc
│   │
│   ├── conexion
│   │   └── ConexionBD.java
│   │
│   ├── controlador
│   │   ├── PedidoControlador.java
│   │   └── RepartidorControlador.java
│   │
│   ├── dao
│   │   ├── EntregaDAO.java
│   │   ├── PedidoDAO.java
│   │   └── RepartidorDAO.java
│   │
│   ├── interfaz
│   │   ├── Cancelable.java
│   │   ├── Despachable.java
│   │   └── Rastreable.java
│   │
│   ├── modelo
│   │   ├── Entrega.java
│   │   ├── EstadoPedido.java
│   │   ├── Pedido.java
│   │   ├── PedidoComida.java
│   │   ├── PedidoEncomienda.java
│   │   ├── PedidoExpress.java
│   │   └── Repartidor.java
│   │
│   ├── tareas
│   │   └── TareaEntrega.java
│   │
│   └── vistas
│       ├── VentanaListaPedidos.java
│       ├── VentanaPrincipal.java
│       ├── VentanaRegistroPedido.java
│       └── VentanaRepartidores.java
│
└── org.example
    └── Main.java
```

---

## 📦 Descripción de las clases

### 🔌 Conexión

#### `ConexionBD`

Gestiona la conexión con la base de datos MySQL mediante `DriverManager`. Además, incluye el método `cerrar()`, que libera de forma segura los recursos (`ResultSet`, `PreparedStatement` y `Connection`).

### 🧩 Modelo

#### `Pedido`

Clase abstracta que representa un pedido de SpeedFast. Contiene el identificador, la dirección de entrega, el estado y el repartidor asignado. Define el método abstracto `getTipo()`, que indica el valor que se guarda en la columna `tipo` de la base de datos.

#### `PedidoComida`, `PedidoEncomienda` y `PedidoExpress`

Clases hijas de `Pedido` que representan cada tipo de pedido (`COMIDA`, `ENCOMIENDA` y `EXPRESS`).

#### `EstadoPedido`

Enum que controla los estados de cada pedido:

* `PENDIENTE`
* `EN_REPARTO`
* `ENTREGADO`

#### `Repartidor`

Representa a un repartidor registrado en la tabla `repartidor`.

#### `Entrega`

Representa la relación entre un pedido y un repartidor, con la fecha y la hora de la entrega.

### 🗂️ DAO (Data Access Object)

#### `PedidoDAO`

* `guardar(Pedido pedido)`: inserta un pedido usando `PreparedStatement` y recupera el ID generado por `AUTO_INCREMENT`.
* `listarTodos()`: devuelve todos los pedidos usando `ResultSet`.
* `buscarPorId(int id)`: busca un pedido por su identificador.
* `actualizarEstado(int id, EstadoPedido estado)`: actualiza el estado del pedido.

#### `RepartidorDAO`

* `guardar(Repartidor repartidor)`: registra un nuevo repartidor.
* `listarTodos()`: devuelve una `List<Repartidor>` con los repartidores almacenados.

#### `EntregaDAO`

* `guardar(Entrega entrega)`: registra la relación entre un pedido y un repartidor, junto con la fecha y la hora.

Todos los DAO utilizan `try-catch-finally` para manejar errores (`SQLException`) y cerrar correctamente las conexiones.

### 🎮 Controlador

#### `PedidoControlador`

Actúa como intermediario entre las vistas y los DAO. En la semana 6 utilizaba un `ArrayList` y ahora trabaja directamente con la base de datos. También registra la entrega al asignar un repartidor e inicia el hilo que simula el reparto.

#### `RepartidorControlador`

Permite registrar y listar repartidores desde la interfaz.

### 🧵 Tareas

#### `TareaEntrega`

Implementa `Runnable` y simula el proceso de entrega en un hilo independiente. Cambia el estado del pedido a `EN_REPARTO`, espera unos segundos y luego lo cambia a `ENTREGADO`, actualizando la base de datos en cada paso.

> Esta clase reemplaza a la antigua clase `Repartidor` de la semana 6, ya que ahora `Repartidor` corresponde a una entidad del modelo.

### 🖥️ Vistas

#### `VentanaPrincipal`

Menú principal con acceso a todas las funciones del sistema.

#### `VentanaRegistroPedido`

Formulario para registrar pedidos en la base de datos. El ID lo genera MySQL automáticamente.

#### `VentanaListaPedidos`

Muestra en un `JTable` los pedidos almacenados, con su tipo, estado y repartidor asignado.

#### `VentanaRepartidores`

Permite registrar repartidores y verlos en un `JTable`.

### Interfaces

El proyecto mantiene las interfaces utilizadas en las actividades anteriores:

* `Cancelable`
* `Despachable`
* `Rastreable`

Estas interfaces permiten mantener la continuidad del sistema SpeedFast desarrollado durante las semanas anteriores.

---

## ⚙️ Configuración

1. Ejecutar el script SQL en **MySQL Workbench** para crear la base de datos `speedfast_db` y sus tablas.
2. Abrir el proyecto en **IntelliJ IDEA** y cargar las dependencias de Maven (`mysql-connector-j`).
3. En la clase `ConexionBD`, configurar el usuario y la contraseña de MySQL:

```java
private static final String USER = "root";
private static final String PASSWORD = "tu_contraseña";
```

---

## ▶️ Ejecución

Se ejecuta la clase `Main`, que abre la ventana principal del sistema.

Flujo de uso:

1. **Repartidores:** registrar uno o más repartidores.
2. **Registrar Pedido:** ingresar la dirección y el tipo de pedido. El sistema muestra el ID generado.
3. **Asignar Repartidor:** ingresar el ID del pedido y seleccionar un repartidor desde la lista. Se registra la entrega en la base de datos y se inicia un hilo que simula el reparto.
4. **Listar Pedidos:** consultar los pedidos almacenados y ver cómo su estado cambia de `PENDIENTE` a `EN_REPARTO` y luego a `ENTREGADO`.

Los datos pueden verificarse en MySQL Workbench con:

```sql
SELECT * FROM speedfast_db.pedido;
SELECT * FROM speedfast_db.repartidor;
SELECT * FROM speedfast_db.entrega;
```

---

## ✅ Conclusión

Con esta actividad, el sistema SpeedFast deja de trabajar solo en memoria y pasa a tener **persistencia real de datos**. Gracias a JDBC y al patrón **DAO**, la lógica de acceso a la base de datos queda separada de la interfaz gráfica, lo que hace que el código sea más ordenado, mantenible y seguro. El uso de `PreparedStatement` ayuda a prevenir la inyección SQL, y el manejo de excepciones con cierre de recursos en `finally` garantiza un acceso controlado a la base de datos.
