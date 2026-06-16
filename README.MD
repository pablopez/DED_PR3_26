#  System Issues Manager - PR3 - DED

Sistema avanzado de gestión de infraestructuras IT, incidencias, asistencias técnicas y red social corporativa. Desarrollado como práctica de Estructuras de Datos (DED) para la **Universitat Oberta de Catalunya (UOC)**.

## 👨‍💻 Autor
- **Nombre:** Pablo López Jiménez
- **Email:** plopezjim@uoc.edu

---

## 🎯 1. Objetivo del Proyecto y Contexto (PEC1 & PEC2)

Este proyecto nace como respuesta a la necesidad de una compañía tecnológica de informatizar y optimizar la gestión interna de incidencias en sus sistemas informáticos. La evolución del sistema se ha desarrollado en dos grandes fases:

**Fase Inicial (PR2 / PEC1):**
Se mejora la estructura del sistema inicial para soportar mayor cantidad de información. Se amplía la usabilidad del sistema inicial añadiendo nuevas entidades (salas físicas, usuarios jerarquizados, asistencias y valoración de asistencias) y características (red social entre trabajadores).

**Expansión y Escalabilidad (PR3 / PEC2):**
Se amplía el sistema para soportar mayor cantidad de información. Se añaden salas, roles jerárquicos de usuarios y tipificación de incidencias. Además, se introducen asistencias gestionadas mediante colas de prioridad, un sistema de control de calidad (Ratings) y una red social corporativa para los técnicos implementada a través de grafos dirigidosstricta donde los técnicos resuelven las tareas en orden inverso de llegada (LIFO).

---

## 🏗️ 2. Arquitectura

Esta arquitectura se vale principalmente de Facade y Strategy Pattern para acotar las responsabilidades de las distintas capas y favorecer la mantenibilidad del código.

El flujo de información se divide en en 4 niveles: Fachada, Servicios, Repositorios y Entidades.

### 2.1 Capa de Fachada (`SystemIssuesPR3Impl`)
Actúa como un único punto de entrada para consumir y gestionar los datos del sistema. Delega las peticiones a los servicios correspondientes abstrayéndose completamente de las entidades.

### 2.2 Capa de Servicios (`src/main/java/uoc/ds/pr/service/`)
Contiene la lógica de negocio. Es donde se aplican los casos de uso que luego ejecutará el TAD. Coordina las diferentes entidades y repositorios y es crucial para mantener el dominio de las distintas entidades aislado.

- `Services.java`: Aplica Facade Pattern para acceder al resto de los módulos inyectando en cada uno los repositorios que necesita para funcionar
- `CoreService.java`: Se encarga de centralizar las operaciones básicas de creación de entidades.
- `AssistanceService.java`: Gestiona las peticiones de asistencia de los usuarios:
  - Crea una `Assistance` asociada al usuario que la solicita.
  - Asigna una `Assistance` a un `Worker`.
  - Gestiona la resolución de una `Assistance`.
  - Gestiona la valoración de una `Assistance`.
- `InfrastructureService.java`: Es un manejador de todas las operaciones relacionadas con la infraestructura. Se encarga de:
  - Asignar un `System` a una `Room`.
  - Instalar un `Component` dentro de un `System`.
  - Devolver un `Iterator<System>` con todos los sistemas
  - Devolver un `Iterator<Component>` con todos los componentes pertenecientes a un sistema en concreto.
- `IssueService.java`: Administra las incidencias desde que se abren hasta que se resuelven:
  - Asigna un `Issue` a un `Worker`.
  - Gestiona la resolución del `Issue`
  - Consultar el historial de incidencias completadas por un trabajador concreto.
- `RamkingService.java`: Deveulve los rankings y estadísticas disponibles:
  - Top 10 `Worker`
  - Top 5 `User`
  - `System` con más `Components`
  - Mejor `Worker`
- `WorkerIssueTypeService.java`: maneja la especialización de los `Worker` para resolver un `Issue`:
  - Asigna a un `Worker` un `IssueType`
  - Obtiene todos los `Worker` por `IssueType`
- `WorkerSocialNetworkService.java`: gestiona la red social de `Worker`:
  - Añade un seguidor a un `Worker`
  - Obtiene un `Iterator<Worker>` con los seguidores de un `Worker`.
  - Obtiene un `Iterator<Worker>` con una recomendación para seguir.
  - Busca otros `Worker` con la misma `IssueType`.

### 2.3 Capa de Repositorios (`src/main/java/uoc/ds/pr/repository/`)
Todos los repositorios extienden `AbstractRepository` (excepto `WorkerSocialNetworkRepository` que detallaré después). Se encarga de almacenar, recuperar, actualizar y eliminar las entidades del modelo, abstrayendo a las capas superiores de la gestión de los datos. 

#### 2.3.1 Repositories (`src/main/java/uoc/ds/pr/repository/Repositories`)
Aquí se inicializan todos y cada uno de los repositorios para evitar que las clases superiores tengan que instanciar todos los repositorios 

#### 2.3.1 Storage (`src/main/java/uoc/ds/pr/storage/`)
`AbstractRepository` implementa un Strategy Pattern mediante `StorageStrategy<T>` para para abstraer el almacenamiento en memoria evitando acoplar estructuras de datos y lógica de negocio. En función de las necesidades de negocio `StorageStrategy<T>` implementará una u otra estructura de negocio garantizando un contrato único independientemente de la estrategia de almacenamiento.

#### 2.3.2 Red Social de trabajadores
Pese a que pueda parecer que rompe el patrón arquitectónico al no extender de `AbstractRepository` es una decisión justificada en la naturaleza de la estructura de datos. Tanto `AbstractRepository` como  `StorageStrategy<T>` están diseñados para resolver operaciones de tipo CRUD mientras que el grafo dirigido representa relaciones entre objetos ya gestionados por otros repositorios.

### 2.4 Capa de Modelo (`src/main/java/uoc/ds/pr/model/`)
Representa las entidades del dominio. Todas sus clases heredan de `AbstractModel` e implementan `Identifiable` para garantizar una estructura con identificador único.
- `System.java` : Representa un sistema informático su usuario, componentes (`Component`) y la sala donde está ubicado (`Room`).
- `Component.java` : Representa un componente (físico/lógico) con ID, marca, modelo, número de serie y su historial de averías.
- `Issue.java` : Representa un parte de trabajo asociado a un componente. Contiene un tipo de incidencia `IssueType`.
- `IssueType.java` : Representa una categoría del parte.
- `Worker.java` : Representa un trabajador. Almacena los partes de trabajo (`Issue`) encargados y realizados, las asistencias `Assistance` pendientes y resueltas, las valoraciones recibidas `Rating` y la especialización del usuario `IssueType`.
- `User.java` - Representa a un usuario de un sistemas. Tiene un rol y un historial de peticiones de asistencia.
- `Assistance.java` - Representa una solicitud de ayuda. Contiene reglas de negocio de prioridad implementadas para dar prioridad a una asistencia respecto a otra implementada por la Lambda `PriorityCMP` (Rol de usuario -> Fecha -> ID).
- `Room.java` - Representa la sala donde está un sistema informático.
- `Rating.java` - Representa la valoración (0-10 en resolución, rapidez y trato) emitida por un `User` para valorar a un `Worker` tras resolverse una `Assistance`.

### 2.5 Excepciones Personalizadas (`src/main/java/uoc/ds/pr/exceptions/`)
Todas las excepciones del sistema extienden de `DSException`. Cada excepción se autodefinen por su nombre. 

### 2.5 Restricciones arquitectónicas
No está permitido usar `java.util` para:
- ArrayList, LinkedList, Vector
- HashMap, Hashtable, HashSet
- TreeSet, PriorityQueue
- Stack, Dictionary, Collections
Se debe de usar exclusivamente estructuras de DSLib(`DSLib-2.1-7.jar`)
---

## 🧬 5. Estructuras de Datos y Justificación

| Entidad / Relación | Estructura Utilizada | Justificación de Diseño                                                                                     |
|-------------------|----------------------|-------------------------------------------------------------------------------------------------------------|
| **Trabajadores / Usuarios** | `HashTable` | Su número es muy elevado pero estable, lo que requiere un acceso rápido por identificador.                  |
| **Sistemas / Componentes / Asistencias** | `DictionaryAVLImpl` (AVL) | El volumen de datos es muy grande y crece linealmente.        |
| **Salas / Tipos / Valoraciones** | `DSListArray` (Vector) | Se estima que su tamaño es pequeño (decenas o centenas) y su volumen es estático.                           |
| **Incidencias pendientes** | `StackLinkedList` (Pila) | Por contrato, los partes de trabajo se deben agrupar y resolver en orden inverso de llegada (LIFO).         |
| **Asistencias pendientes** | `PriorityQueue` | Su extracción depende de reglas de prioridad                                                                |
| **Incidencias / Asistencias Resueltas** | `DictionaryAVLImpl` (AVL) | Volumen grande y en constante aumento, almacenado con clave incremental para mantener su orden              |
| **Rankings (Top 10 / Top 5)** | `OrderedVector` | Al macena el TOP N de la entidad correspondiente                                                            |
| **Trabajadores por Tipo** | `RoundRobinList` | El encargo de asistencias se realiza según la especialidad siguiendo una asignación circular (Round Robin). |
| **Red Social Trabajadores**| `DirectedGraphImpl` | Representa mediante un grafo dirigido las relaciones entre los trabajadores                                 |


---
## 🛠️ 5. Deuda Técnica (Auditoría PEC2)

El código actual asume el enunciado original. No obstante quiero dejar nota de que, basándonos en la auditoría del sistema comentada en la solución oficial de la PEC2, constan las siguientes deudas técnicas para futuras fases:

### 5.1. Optimización de consultas profundas (Partes de trabajo por Usuario)
Actualmente, obtener los partes de trabajo de un usuario requiere recorrer múltiples niveles de la estructura: **$Usuario \rightarrow Sistemas \rightarrow Componentes \rightarrow Partes$**. Esto genera una complejidad temporal costosa de **$O(log S + SU \cdot SC \cdot CP)$**.
* **Solución propuesta:** Añadir una estructura adicional (lista enlazada) directamente en el modelo `User` que almacene referencias a sus partes de trabajo. Esto penalizaría ligeramente la escritura pero reduciría la lectura a una complejidad proporcional al número de partes.

### 5.2. Escalabilidad de las Valoraciones (Ratings)
El diseño original propuso que el volumen de valoraciones sería estático. Dado que el número de asistencias crece de forma lineal y cada asistencia puede ser valorada se debe de refactorizar el código para que se trate la estructura de datos que almacena Valoraciones como una estructura con crecimiento lineal. 
* **Solución propuesta:** Refactorizar el `RatingRepository` para sustituir el vector secuencial por un Árbol AVL (`DictionaryAVLImpl`), garantizando operaciones en **$O(log N)$** frente a grandes volúmenes.

### 5.3. Saturación de Trabajadores por Tipo de Incidencia
La asignación utiliza la estructura `RoundRobinList` para el reparto equitativo. Si un tipo de incidencia es muy común, la cantidad de trabajadores capacitados puede tener un crecimiento mayor del esperado comprometiendo el rendimiento en operaciones de búsqueda .
* **Solución propuesta:** implementar un TAD basado en otro tipo de estructuras preparadas para manejar grandes cantidades, como por ejemplo una tabla de dispersión.

---

## 🧪 6. Testing

En esta sección se han omitido de las clases de pruebas proporcionadas para evitar ser redundante. A continuación pongo el foco en la colección de test que he añadido en `src/test/java/uoc/ds/pr/PR3ExtraTests.java`:
- `pr3ShouldShareStateWithInheritedPR2Methods()`: Desde un punto de vista arquitectónico, al embeber la PR2 en la PR3, existe riesgo de instanciar repositorios por duplicado. Este test comprueba que se inyectan correctamente los repositorios en los servicios manteniendo su estado en todo momento.
- `reassignWorkerToAnotherIssueTypeMustMoveWorker()`: Prueba que al reasignar un trabajador a un nuevo tipo de incidencia, se actualizan correctamente los contadores y las listas en ambas estructuras.
- `getTop10BestWorkersWithoutRatedWorkersShouldThrow()`: Asegura que se lance NoWorkerException al solicitar los rankings si todavía no se ha valorado a ningún trabajador evitando los clásicos `NullPointerException` o `IndexOutOfBoundsException`
- `rateUnknownAssistanceShouldThrowAssistanceNotResolved()`: Asegura que se lance `AssistanceNotResolvedException` al intentar puntuar una asistencia desconocida o no resuelta.
- `solveAssistanceMustRespectPriority()`: Crea colisiones artificiales para verificar que la comparación sigue la prioridad esperada (Rol de usuario -> Fecha -> ID).
---

## 💻 7. Tecnologías y Dependencias

* **Lenguaje:** Java 23
* **Gestor de dependencias:** Maven
* **Librerías Core:** DSLib 2.1-7 (UOC Data Structures)
* **Testing:** JUnit 4.13, ArchUnit 1.3.0
* **Utilidades:** Apache Commons CSV 1.10.0, SLF4J 2.0.13