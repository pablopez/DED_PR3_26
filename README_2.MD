# System Issues Manager

**Sistema de gestión interna de problemas informáticos**.

## 👨‍💻 Autor
- **Nombre:** Pablo López Jiménez
- **Email:** plopezjim@uoc.edu

---

## 🎯 1. Objetivo del Proyecto y Contexto (PEC1 & PEC2)

**Objetivo Principal:**
Informatizar y optimizar la gestión interna de problemas informáticos. La evolución del sistema se ha desarrollado en dos grandes fases:

**Fase Inicial (PR2 / PEC1):**
A computer system issue management system that enables the administration of workers, systems, components, and issues. The system facilitates task assignment to workers using a LIFO stack structure and provides queries about the overall system state.

**Expansión y Escalabilidad (PR3 / PEC2):**
Tras el éxito de la primera fase se solicita una auditoría y una ampliación de las funcionalidades para soportar un volumen de datos mayor. El alcance del proyecto se amplió para:
* Ubicar los sistemas en salas físicas y vincularlos a los usuarios de la compañía, clasificados jerárquicamente según su rol.
* Tipificar las incidencias para garantizar que son atendidas por un trabajador cualificado. 
* Introducir solicitud y resolución de asistencias.
* Introducir un sistema de valoración de asistencias.
* Implementar funciones sociales para que los trabajadores puedan seguirse entre sí.
---

## 🚫 2. Architectural Constraints

⚠️ **MANDATORY:** No classes from `java.util` are allowed to be used:
- ❌ ArrayList, LinkedList, Vector
- ❌ HashMap, Hashtable, HashSet
- ❌ TreeSet, PriorityQueue
- ❌ Stack, Dictionary, Collections

✅ **MANDATORY:** Exclusively use DSLib structures (`DSLib-2.1-7.jar`)

---

## 🏗️ 3. Arquitectura

El flujo de información se divide en tres niveles (de más alto a más bajo nivel):

1. **Capa de Fachada (`SystemIssuesPR3Impl` y `SystemIssuesPR2Impl`)**: Expone los datos al exterior y delega las peticiones a la capa de servicios.
2. **Capa de Servicios (Casos de Uso)**: Coordina los diferentes repositorios para hacer cumplir las reglas de negocio.
3. **Capa de Repositorios**: Se encarga de manejar las estructuras de datos.
4. **Capa de Modelos**: Se encarga de abstraer y modelar los datos para gestionar entidades concretas.

En la segunda fase he decidido introducir la capa de servicios para evitar un antipatrón *God Object* en el TAD SystemIssue.

Aquí tienes el documento completo y unificado, estructurado con todas las secciones que hemos trabajado (FSD, contexto de las PECs, restricciones, modelos, excepciones, tabla Big-O y deuda técnica).

Solo tienes que copiar el bloque a continuación y pegarlo directamente en tu archivo README.md:

Markdown
# 🛠️ System Issues Manager (PR2 & PR3)

Sistema avanzado de gestión de infraestructuras IT, incidencias, asistencias técnicas y red social corporativa. Desarrollado como práctica de Estructuras de Datos (DED) para la **Universitat Oberta de Catalunya (UOC)**.

## 👨‍💻 Autor
- **Nombre:** Pablo López Jiménez
- **Email:** plopezjim@uoc.edu

---

## 🎯 1. Objetivo del Proyecto y Contexto (PEC1 & PEC2)

Este proyecto nace como respuesta a la necesidad de una compañía tecnológica de informatizar y optimizar la gestión interna de incidencias en sus sistemas informáticos. La evolución del sistema se ha desarrollado en dos grandes fases:

**Fase Inicial (PR2 / PEC1):**
El objetivo primordial fue establecer el *core* de la infraestructura tecnológica. Se diseñó un sistema capaz de registrar puestos de trabajo y sistemas informáticos, desglosando cada uno en sus respectivos componentes de hardware. Ante el fallo de un sistema, el programa genera un "parte de trabajo" asociado al componente averiado y lo asigna a un trabajador especializado. Para agilizar las reparaciones, se implementó una política estricta donde los técnicos resuelven las tareas en orden inverso de llegada (LIFO).

**Expansión y Escalabilidad (PR3 / PEC2):**
Tras el éxito del programa piloto, la compañía solicitó una auditoría y una ampliación masiva de las funcionalidades para soportar un volumen de datos mayor. El alcance del proyecto se amplió para:
* **Inventario Espacial e Institucional:** Ubicar los sistemas en salas físicas y vincularlos a los usuarios, clasificados jerárquicamente según su responsabilidad directiva.
* **Especialización de Incidencias:** Tipificar los problemas (hardware, red, software) garantizando que solo el personal capacitado las atienda.
* **Módulo de Asistencias y Calidad:** Introducir "asistencias" para peticiones lógicas (ej. acceso a VPN), resueltas mediante colas de prioridad basadas en el rango del usuario y la antigüedad de la petición. Se sumó un sistema de métricas de calidad para puntuar el trato, rapidez y resolución.
* **Red Social Corporativa (Plus):** Implementación de funciones sociales mediante grafos dirigidos para que los trabajadores puedan seguirse entre sí, fomentando la comunicación rápida y el soporte cruzado.

---

## 🚫 2. Architectural Constraints

⚠️ **MANDATORY:** No classes from `java.util` are allowed to be used:
- ❌ ArrayList, LinkedList, Vector
- ❌ HashMap, Hashtable, HashSet
- ❌ TreeSet, PriorityQueue
- ❌ Stack, Dictionary, Collections

✅ **MANDATORY:** Exclusively use DSLib structures (`DSLib-2.1-7.jar`)

---

## 🏗️ 3. Arquitectura, Feature-Sliced Design (FSD) y Principios SOLID

Aprovechando la experiencia profesional en desarrollo web, el diseño de este sistema surge de una translación directa de los principios del **Feature-Sliced Design (FSD)** (habitual en arquitecturas escalables de frontend) al ecosistema de Java y a la Arquitectura en Capas.

El objetivo fundamental de esta decisión ha sido erradicar el antipatrón *God Object* (evitando que la fachada del TAD centralice toda la lógica), acotar de manera estricta las responsabilidades de las distintas capas y favorecer la mantenibilidad a largo plazo.

El flujo de información se divide en tres niveles:

1. **Capa de Fachada (`SystemIssuesPR3Impl`)**: Actúa como un único punto de entrada limpio (Orquestador). Delega las peticiones a los servicios correspondientes sin conocer los detalles de implementación subyacentes.
2. **Capa de Servicios (Casos de Uso)**: Contiene la lógica pura de negocio. Coordina las diferentes entidades y cruza datos entre dominios aislados (Ej. `AssistanceService`, `RankingService`).
3. **Capa de Repositorios**: Implementa el patrón *Strategy* (`StorageStrategy<T>`) para abstraer el almacenamiento en memoria, evitando acoplar la lógica de negocio a los detalles de las estructuras de datos.

### Principios SOLID Aplicados:
* **(S) Responsabilidad Única:** Separación total entre Modelos, Repositorios y Servicios.
* **(O) Abierto/Cerrado:** Criterios de ordenación inyectados mediante delegación funcional y Lambdas en lugar de extender clases (`Assistance.PriorityCMP`).
* **(L) Sustitución de Liskov:** Pilas personalizadas (`StackLinkedList`) refactorizadas usando **Composición sobre Herencia** para no exponer métodos vulnerables de las listas enlazadas base.
* **(D) Inversión de Dependencias:** Inyección de dependencias estática (*Poor Man's DI*) en la factoría principal, eliminando dependencias globales (*Singletons*) y favoreciendo el testing aislado.

---

## 🧩 4. Data Models y Custom Exceptions

### Modelos de Dominio (`src/main/java/uoc/ds/pr/model/`)

**Modelos Base**
- `Identifiable.java` - Interfaz que garantiza que toda entidad tenga un identificador único.
- `AbstractModel.java` - Clase abstracta base que implementa `Identifiable` y `Comparable` para permitir la ordenación y eliminación por ID en las estructuras de datos.

**Entidades**
- `System.java` : Representa un sistema informático con ID, descripción, ubicación física, componentes instalados y sala asignada.
- `Component.java` : Representa un componente (físico/lógico) con ID, marca, modelo, número de serie y su historial de averías.
- `Issue.java` : Representa un parte de trabajo (avería técnica) asociado a un componente, con ID, descripción, fecha, estado de resolución y tipo de incidencia.
- `IssueType.java` : Representa una categoría o especialidad de avería (ej. Hardware, Redes).
- `Worker.java` : Representa un técnico/trabajador. Almacena su carga de trabajo (pilas de incidencias, colas de prioridad de asistencias), métricas de resolución y el grafo de seguidores.
- `User.java` - Representa a un empleado de la compañía que utiliza los sistemas. Contiene su rol/rango (Director, Analista, etc.) y su historial de peticiones de asistencia.
- `Assistance.java` - Representa una solicitud de ayuda (no vinculada a hardware). Contiene reglas de negocio puras para su priorización funcional (Lambda `PriorityCMP` por Rango -> Fecha -> ID).
- `Room.java` - Representa una ubicación física (sala) donde se agrupan los sistemas informáticos.
- `Rating.java` - Representa la valoración (0-10 en resolución, rapidez y trato) emitida por un usuario hacia una asistencia resuelta.

### Excepciones Personalizadas (`src/main/java/uoc/ds/pr/exceptions/`)

Todas las excepciones del sistema extienden de `DSException`. Se dividen en dos categorías lógicas:

**Excepciones de Acceso a Datos (Not Found)**
Lanzadas por la capa de Repositorios cuando no se encuentra una entidad:
- `WorkerNotFoundException` : El técnico especificado no existe.
- `UserNotFoundException` : El usuario especificado no existe.
- `SystemNotFoundException` : El sistema informático no existe.
- `RoomNotFoundException` : La sala especificada no existe.
- `ComponentNotFoundException` : El componente no existe.
- `IssueNotFoundException` : El parte de trabajo no existe.
- `IssueTypeNotFoundException` : La categoría de incidencia no existe.
- `AssistanceNotFoundException` : La solicitud de asistencia no existe.
- `RatingNotFoundException` : La valoración no existe.
- `FollowerNotFoundException` : El trabajador al que se intenta seguir no existe.
- `ComponentAlreadyInstalledException` : Intento de instalar un componente que ya pertenece a un sistema.
- `IssueAlreadyAssignedException` : Intento de asignar un parte que ya tiene técnico.
- `IssueAlreadyResolvedException` : Intento de interactuar con un parte ya cerrado.
- `NoWorkerException` : Estructura vacía o no hay técnicos capacitados para un tipo de incidencia.
- `NoUserException` : Operación requerida sobre una estructura de usuarios vacía.
- `NoSystemsException` : Operación requerida sobre una estructura de sistemas vacía.
- `NoIssuesException` : Intento de extraer/resolver de una pila de partes vacía.
- `SystemHasNoComponentsException` : Consulta sobre un sistema sin hardware asociado.
- `NoAssistanceException` : Intento de extraer/resolver de una cola de asistencias vacía.
- `UserIsNotCreatorException` : Intento de valorar una asistencia por un usuario que no la solicitó.
- `AssistanceNotResolvedException` : Intento de valorar una asistencia pendiente.
- `NoFollowersException` / `NoFollowedException` : Consultas sobre grafos vacíos para un vértice concreto.

---

## 🧬 5. Estructuras de Datos y Justificación (Acorde a las PEC)

| Entidad / Relación | Estructura Utilizada | Justificación de Diseño (Conforme al Enunciado) |
|-------------------|----------------------|------------------------------------------------|
| **Trabajadores / Usuarios** | `HashTable` | Su número es muy elevado pero estable, lo que requiere un acceso rápido por identificador. |
| **Sistemas / Componentes / Asistencias** | `DictionaryAVLImpl` (AVL) | El volumen de datos es muy grande y crece lineal o constantemente. Sus operaciones de reequilibrado son ideales. |
| **Salas / Tipos / Valoraciones** | `DSListArray` (Vector) | Se estima que su tamaño es pequeño (decenas o centenas) y su volumen es estático. |
| **Incidencias pendientes** | `StackLinkedList` (Pila) | Por contrato, los partes de trabajo se deben agrupar y resolver en orden inverso de llegada (LIFO). |
| **Asistencias pendientes** | `PriorityQueue` | Su extracción depende de reglas de prioridad: primero el cargo del usuario, luego la fecha y por último el identificador. |
| **Incidencias / Asistencias Resueltas** | `DictionaryAVLImpl` (AVL) | Volumen grande y en constante aumento, almacenado con clave incremental para mantener su orden temporal de resolución. |
| **Rankings (Top 10 / Top 5)** | `OrderedVector` | Se requieren listados acotados para consultar directamente a los mejores técnicos o los usuarios más activos. |
| **Trabajadores por Tipo** | `RoundRobinList` | El encargo de asistencias se realiza según la especialidad siguiendo una asignación circular (Round Robin). |
| **Red Social Trabajadores**| `DirectedGraphImpl` | Representa de forma natural las relaciones de "seguir" (seguidor -> seguido) sin que exista una jerarquía estricta. |

---

## 🛠️ 5. Deuda Técnica y Futuras Mejoras (Auditoría PEC2)

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

## 🧪 6. Testing y Datos de Prueba

El sistema incluye una sólida base de pruebas unitarias y de integración, alimentadas por archivos `.csv` ubicados en `src/test/resources/`:

* **ArchitectureTest:** Garantiza mediante validación estática que no se violan las restricciones del entorno (ausencia de `java.util`).
* **Datos de Carga:** Archivos de Workers (28), Systems (10), Components (20), Issues (34), Users y Assistances para simulaciones con datos realistas.
* **Unit & Integration Tests:** Cobertura de excepciones de dominio (`UserIsNotCreatorException`, `IssueAlreadyAssignedException`) y excepciones de persistencia (`NotFoundException`).

---

## 💻 7. Tecnologías y Dependencias

* **Lenguaje:** Java 23
* **Gestor de dependencias:** Maven
* **Librerías Core:** DSLib 2.1-7 (UOC Data Structures)
* **Testing:** JUnit 4.13, ArchUnit 1.3.0
* **Utilidades:** Apache Commons CSV 1.10.0, SLF4J 2.0.13