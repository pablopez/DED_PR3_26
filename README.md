## PR3

## Author
- name: Pablo López
- e-mail: plopezjim@uoc.edu

# DED PR3 — SystemIssues

Esta práctica pretende implementar la solución oficial de la PEC2 de la asignatura.   

La práctica tiene como objetivo:
- refactorizar el código de la solución oficial de la PR2 para que funcione con las nuevas modificaciones
- implementa el TAD `SystemIssues`

El diseño se ha realizado siguiendo los criterios del temario: 
- elección explícita de TADs
- separación entre modelo/repositorio/fachada
- uso de la DSLib de la asignatura evitando de colecciones de `java.util` para almacenar datos del dominio.

Además he decidido, añadir una capa de servicio entre el TAD y las implementaciones para evitar que los repositorios 
trabajaran entre sí en el propio TAD.

---

## 1. Objetivo del proyecto

El sistema permite gestionar:

- Trabajadores.
- Usuarios.
- Sistemas informáticos.
- Componentes.
- Partes/incidencias de trabajo.
- Salas.
- Tipos de incidencia.
- Asistencias.
- Valoraciones.
- Rankings.
- Relaciones sociales entre trabajadores.

Las operaciones principales se agrupan en dos bloques:

1. **PR2**: alta y gestión de trabajadores, sistemas, componentes e incidencias.
2. **PR3**: gestión de salas, usuarios, tipos de incidencia, asistencias, valoraciones, rankings y parte plus con grafos.

---

## 2. Organización del código

La estructura principal del proyecto es:

```text
uoc.ds.pr
├── exceptions
├── model
├── pr2
├── pr3
├── repository
├── service
└── util
```

### `model`

Contiene las entidades del dominio:

```text
AbstractModel
Identifiable
Worker
User
System
Component
Issue
IssueType
Room
Assistance
Rating
```

Cada entidad persistible hereda de `AbstractModel`, que proporciona la propiedad de tener un identificador común mediante `getId()`.

### `repository`

Contiene los repositorios y estrategias de almacenamiento:

```text
Repository<T>
StorageStrategy<T>
AbstractRepository<T>

HashStorageStrategy<T>
AVLStorageStrategy<T>
VectorStorageStrategy<T>

WorkerRepository
UserRepository
SystemRepository
ComponentRepository
IssueRepository
IssueTypeRepository
RoomRepository
AssistanceRepository
RatingRepository
WorkerSocialNetworkRepository
```

### `util`

Contiene estructuras auxiliares propias:

```text
DSList
DSListArray
DSLinkedList
StackLinkedList
OrderedVector
RoundRobinList
```

Estas clases complementan las estructuras de la DSLib de la asignatura.

### `pr2` y `pr3`

Contienen las interfaces y las implementaciones principales del TAD:

```text
SystemIssues
SystemIssuesPR2Impl
SystemIssuesHelper

SystemIssuesPR3
SystemIssuesPR3Impl
SystemIssuesHelperPR3
```

`SystemIssuesPR3Impl` extiende `SystemIssuesPR2Impl`, reutilizando la funcionalidad previa y añadiendo las operaciones nuevas de PR3.

---

## 3. Decisión general de diseño

Se ha usado una arquitectura por capas sencilla:

```text
SystemIssuesPR2Impl / SystemIssuesPR3Impl
    Coordina operaciones del TAD.

Repositories
    Gestionan almacenamiento, búsqueda e índices auxiliares.

Models
    Representan entidades y relaciones directas.

Utils
    Implementan TADs auxiliares o adaptadores.

Helpers
    Permiten inspeccionar el estado interno desde los tests.
```

La fachada `SystemIssuesPR3Impl` coordina varias entidades y repositorios. Los repositorios no deberían instanciarse de nuevo fuera del sistema principal, porque eso rompería la unicidad del estado.

---

## 4. Modelos principales

### `AbstractModel`

Todas las entidades persistibles heredan de `AbstractModel`.

```java
public abstract class AbstractModel implements Identifiable, Comparable<AbstractModel>
```

Decisiones:

- Todas las entidades tienen un `id`.
- El `id` es `final`, por lo que no cambia después de crear el objeto.
- La comparación natural (`compareTo`) se realiza por `id`.

Esto ayuda a mantener coherencia entre los repositorios y las entidades. También permite que estructuras como `OrderedVector.delete(...)`, que internamente usa `Comparable`, puedan eliminar elementos por identidad basada en el identificador.

### `Worker`

Representa un trabajador.

Estructuras internas:

```text
Stack<Issue> issues
DictionaryAVLImpl<Integer, Issue> solvedIssues
AssignedAssistanceQueue assignedAssistances
DictionaryAVLImpl<String, Assistance> solvedAssistances
DSLinkedList<Rating> rates
```

Decisiones:

- Las incidencias pendientes se gestionan como pila porque se resuelven en orden LIFO.
- Las incidencias resueltas se guardan en un AVL con clave incremental entera, para poder devolverlas en orden de resolución.
- Las asistencias pendientes se gestionan con una cola de prioridad.
- Las asistencias resueltas se guardan por id.
- Las valoraciones se almacenan en lista enlazada y se mantiene un acumulador `ratingsSum` para calcular la media sin recorrer todas las valoraciones.

### `User`

Representa un usuario que crea asistencias.

Estructura interna:

```text
DSLinkedList<Assistance> assistances
```

Se guardan las asistencias creadas por el usuario y se mantiene el contador mediante `size()`.

### `System`

Representa un sistema informático.

Estructuras internas:

```text
DSLinkedList<Component> components
Room room
User user
```

Decisiones:

- Los componentes instalados se guardan en una lista enlazada.
- La sala y el usuario se mantienen con apuntadores directos.
- La relación `System -> Room` se mantiene desde `System.setRoom(...)`.
- Si un sistema cambia de sala, se elimina de la sala anterior y se añade a la nueva.

### `Component`

Representa un componente físico o lógico.

Estructuras internas:

```text
System system
DSLinkedList<Issue> issues
```

El componente mantiene apuntador directo al sistema donde está instalado y una lista de incidencias asociadas.

### `Issue`

Representa un parte de trabajo o incidencia.

Relaciones principales:

```text
Component component
IssueType issueType
Worker worker
boolean resolved
```

Decisiones:

- El componente se mantiene por apuntador directo.
- El tipo de incidencia se mantiene por apuntador directo.
- El trabajador asignado se mantiene por apuntador directo.
- El estado `resolved` indica si la incidencia ya ha sido resuelta.

### `IssueType`

Representa un tipo de incidencia.

Estructura interna:

```text
RoundRobinList<Worker> workers
```

Decisiones:

- Los trabajadores asociados al tipo de incidencia se guardan en una `RoundRobinList`.
- La asignación automática de asistencias se realiza siguiendo round robin.
- Cuando un trabajador cambia de tipo, se elimina del tipo anterior y se añade al nuevo.

### `Assistance`

Representa una asistencia creada por un usuario.

Relaciones principales:

```text
User user
IssueType issueType
Room room
Worker worker
Rating rating
boolean resolved
```

Decisiones:

- El usuario, la sala, el tipo de incidencia y el trabajador se guardan como apuntadores directos.
- La asistencia cambia de estado cuando se asigna, se resuelve y se valora.
- La valoración queda asociada directamente a la asistencia.

### `Rating`

Representa una valoración de una asistencia resuelta.

Campos principales:

```text
Assistance assistance
LocalDate date
int resolutionScore
int speedScore
int treatmentScore
```

Decisiones:

- Cada puntuación se valida en el rango `[0, 10]`.
- La media se calcula como la media aritmética de resolución, rapidez y trato.
- La valoración se asocia a una asistencia y se usa para actualizar el rating global del trabajador.

---

## 5. Repositorios y estrategias de almacenamiento

El proyecto utiliza el patrón **Strategy** para desacoplar los repositorios de la estructura concreta usada para almacenar los datos.

### Interfaz `StorageStrategy<T>`

Define las operaciones básicas de almacenamiento:

```text
get(id)
put(element)
remove(id)
contains(id)
size()
values()
```

### `AbstractRepository<T>`

Implementa la lógica común de los repositorios:

```text
getById
save
removeById
contains
size
values
addElement
getByIdOrThrow
```

`addElement(...)` centraliza el patrón:

```text
si no existe -> crear y guardar
si existe    -> actualizar
```

De esta forma, no se repite la misma lógica en todos los repositorios.

### Estrategias concretas

| Estrategia | TAD usado | Uso |
|---|---|---|
| `HashStorageStrategy` | `HashTable` | Acceso rápido por id |
| `AVLStorageStrategy` | `DictionaryAVLImpl` | Datos grandes y crecientes |
| `VectorStorageStrategy` | `DSListArray` | Datos pequeños o acotados |

---

## 6. Estructuras elegidas por entidad

| Entidad / relación | Estructura |
|---|---|
| Trabajadores | `HashTable` |
| Usuarios | `HashTable` |
| Sistemas | `DictionaryAVLImpl` |
| Componentes | `DictionaryAVLImpl` |
| Incidencias | `DictionaryAVLImpl` |
| Asistencias | `DictionaryAVLImpl` |
| Salas | `DSListArray` |
| Tipos de incidencia | `DSListArray` |
| Valoraciones | `DSListArray` |
| Trabajadores por tipo | `RoundRobinList` |
| Incidencias pendientes de trabajador | `StackLinkedList` |
| Incidencias resueltas de trabajador | `DictionaryAVLImpl<Integer, Issue>` |
| Asistencias pendientes de trabajador | `PriorityQueue` |
| Asistencias resueltas de trabajador | `DictionaryAVLImpl` |
| Valoraciones de trabajador | `DSLinkedList` |
| Top trabajador por incidencias | Apuntador directo |
| Sistema con más componentes | Apuntador directo |
| Top 10 trabajadores valorados | `OrderedVector` |
| Top 5 usuarios con más asistencias | `OrderedVector` |
| Red social de trabajadores | `DirectedGraphImpl` |

Estas elecciones siguen el criterio del temario: usar estructuras con acceso rápido cuando predomina la búsqueda, estructuras ordenadas cuando interesa iterar por clave, colas de prioridad cuando la extracción depende de prioridad, pilas cuando se requiere LIFO, y grafos cuando se representan relaciones dirigidas.

---

## 7. Operaciones PR2

### Añadir trabajador

```java
addWorker(workerId, name, address)
```

Usa `WorkerRepository`. Si el trabajador no existe, se crea. Si ya existe, se actualiza.

Además, el trabajador se registra como vértice en la red social de trabajadores.

### Añadir sistema

```java
addSystem(systemId, description, location, userId)
```

Busca el usuario y crea o actualiza el sistema. El sistema queda asociado al usuario mediante apuntador directo.

### Añadir componente

```java
addComponent(componentId, trademark, model, serial)
```

Crea o actualiza un componente.

### Instalar componente en sistema

```java
installComponentToSystem(componentId, systemId)
```

Asocia un componente a un sistema. El sistema mantiene la lista de componentes y el componente mantiene apuntador directo al sistema.

También se actualiza el apuntador al sistema con más componentes.

### Crear incidencia

```java
createIssue(issueId, componentId, issueTypeId, description, dateTime)
```

Crea una incidencia asociada a un componente y a un tipo de incidencia.

### Asignar incidencia

```java
assignIssue(issueId, workerId)
```

Asigna una incidencia a un trabajador. La incidencia queda asociada al trabajador y el trabajador la apila en su pila de incidencias pendientes.

### Resolver incidencia

```java
solveIssue(workerId)
```

El trabajador resuelve la última incidencia asignada. Se usa una pila, por lo que el orden es LIFO.

La incidencia resuelta se guarda en un AVL con clave incremental para poder recuperar las incidencias en orden de resolución.

### Obtener trabajador con más incidencias resueltas

```java
getTopWorker()
```

Se mantiene un apuntador directo al trabajador que más incidencias ha resuelto, actualizándolo cada vez que se resuelve una incidencia.

### Obtener sistema con más componentes

```java
getSystemWithMostComponents()
```

Se mantiene un apuntador directo al sistema con más componentes, actualizándolo al instalar componentes.

---

## 8. Operaciones PR3

### Añadir sala

```java
addRoom(roomId, name)
```

Crea o actualiza una sala.

### Añadir usuario

```java
addUser(userId, name, role, phone)
```

Crea o actualiza un usuario.

### Añadir tipo de incidencia

```java
addIssueType(issueTypeId, name)
```

Crea o actualiza un tipo de incidencia.

### Asignar sistema a sala

```java
assignSystemToRoom(systemId, roomId)
```

Busca el sistema y la sala. Luego actualiza la relación mediante `system.setRoom(room)`.

La relación se mantiene bidireccionalmente:

```text
System -> Room
Room -> sistemas asociados
```

### Asignar trabajador a tipo de incidencia

```java
assignWorkerToIssueType(workerId, issueTypeId)
```

El trabajador guarda apuntador directo al tipo de incidencia. Si ya tenía otro tipo, se elimina del anterior y se añade al nuevo.

El tipo de incidencia mantiene sus trabajadores en una `RoundRobinList`.

### Añadir asistencia

```java
addAssistance(assistanceId, userId, issueTypeId, roomId, date, description)
```

Crea una asistencia asociada a:

```text
User
IssueType
Room
```

Después, el usuario añade la asistencia a su lista de asistencias. El `UserRepository` actualiza el ranking de usuarios con más asistencias.

### Asignar asistencia

```java
assignAssistance(assistanceId)
```

Busca la asistencia, obtiene su tipo de incidencia y selecciona el siguiente trabajador mediante round robin.

Después:

```text
assistance.setWorker(worker)
worker.addAssignedAssistance(assistance)
```

### Resolver asistencia

```java
solveAssistance(workerId)
```

El trabajador resuelve la asistencia pendiente con mayor prioridad.

La prioridad se define en `AssignedAssistanceQueue`:

1. Mayor rango del rol del usuario.
2. Fecha más antigua.
3. Identificador menor.

Internamente se usa `PriorityQueue`.

### Valorar asistencia

```java
rateAssistance(ratingId, userId, assistanceId, date, resolutionScore, speedScore, treatmentScore)
```

La operación valida que:

```text
la asistencia esté resuelta
el usuario que valora sea el creador de la asistencia
```

Luego:

```text
crea o actualiza Rating
asocia Rating a Assistance
actualiza rating del Worker
actualiza ranking topWorkers
```

### Top 10 trabajadores mejor valorados

```java
getTop10BestWorkers()
```

Se usa un `OrderedVector<Worker>` en `WorkerRepository`.

El ranking se actualiza cuando un trabajador recibe una valoración. Antes de actualizar el rating, se elimina del vector mediante `delete`, se actualiza el trabajador y se reintroduce en el vector.

El criterio de orden se define con:

```java
Comparator.comparingDouble(Worker::getGlobalRating)
```

No se han añadido desempates extra para respetar el comportamiento esperado por los tests.

### Top 5 usuarios con más asistencias

```java
getTop5UsersWithMostAssistances()
```

Se usa un `OrderedVector<User>` en `UserRepository`.

El ranking se actualiza cuando un usuario crea una asistencia.

El criterio de orden se define con:

```java
Comparator.comparingInt(User::getAssistancesCount)
```

No se añaden desempates adicionales por el mismo motivo: los tests esperan el orden resultante de esta comparación y del comportamiento de `OrderedVector`.

---

## 9. Parte plus: red social entre trabajadores

La parte plus se implementa con:

```java
DirectedGraphImpl<Worker, String>
```

en `WorkerSocialNetworkRepository`.

### Dirección de las aristas

La relación se representa así:

```text
follower -> followed
```

Por ejemplo:

```java
addFollower("W1", "W2")
```

significa:

```text
W2 sigue a W1
```

y se crea la arista:

```text
W2 -> W1
```

### Operaciones implementadas

```text
addFollower
getFollowers
getFollowings
recommendations
getUnfollowedWorkersWithAssignedIssueType
```

### Seguidores

```java
getFollowers(worker)
```

Devuelve los vértices que tienen una arista hacia `worker`.

### Seguidos

```java
getFollowings(worker)
```

Devuelve los vértices a los que apunta `worker`.

### Recomendaciones

```java
getRecommendations(worker)
```

Busca seguidores de los seguidores, evitando:

```text
el propio worker
trabajadores que ya siguen directamente al worker
duplicados
```

### Trabajadores no seguidos por tipo de incidencia

```java
getUnfollowedWorkersWithAssignedIssueType(worker, workersByIssueType)
```

Recorre los trabajadores de un tipo de incidencia y devuelve aquellos que:

```text
no son el propio worker
no siguen ya al worker
no están duplicados en el resultado
```

---

## 10. Helpers

Los helpers se usan como mecanismos de inspección para los tests.

```text
SystemIssuesHelperImpl
SystemIssuesHelperPR3Impl
```

No forman parte de la lógica principal del TAD.

Responsabilidad del helper:

```text
consultar estado
devolver objetos
devolver contadores
```

No debería:

```text
crear entidades
modificar repositorios
ejecutar reglas de negocio
```

En general:

```text
getX(id) puede devolver null
numX(...) devuelve un contador
numXByY(...) puede devolver 0 si no existe la entidad base
```

Las operaciones públicas del TAD son las encargadas de validar y lanzar excepciones cuando el contrato lo exige.

---

## 11. Excepciones

Se distinguen dos tipos de excepciones.

### Excepciones de acceso a datos

Son apropiadas para repositorios:

```text
WorkerNotFoundException
UserNotFoundException
RoomNotFoundException
SystemNotFoundException
ComponentNotFoundException
IssueNotFoundException
IssueTypeNotFoundException
AssistanceNotFoundException
RatingNotFoundException
```

Los repositorios ofrecen métodos `getXOrThrow(...)` para este propósito.

### Excepciones de reglas de negocio

Suelen lanzarse desde la fachada o desde métodos de repositorio que encapsulan una operación de dominio:

```text
IssueAlreadyAssignedException
IssueAlreadyResolvedException
ComponentAlreadyInstalledException
NoIssuesException
NoWorkerException
NoAssistanceException
NoUserException
NoFollowersException
NoFollowedException
UserIsNotCreatorException
AssistanceNotResolvedException
```

---

## 12. Supuestos sobre `getById(...)`

La implementación distingue entre:

```text
getX(id)
getXOrThrow(id)
```

### `getX(id)`

Puede devolver `null`.

Se usa para:

```text
helpers
consultas auxiliares
casos donde el contrato o el test garantiza que el registro existe
```

### `getXOrThrow(id)`

Lanza la excepción correspondiente si el registro no existe.

Se usa en operaciones públicas del TAD cuando la interfaz declara una excepción de tipo `NotFound`.

### Supuesto documentado

En algunas operaciones, los tests y el enunciado asumen que determinados ids existen. En esos casos se usa `getX(id)` directamente. Esta decisión simplifica la implementación, pero implica una precondición: si se pasa un id inexistente fuera de los casos previstos por los tests, podría producirse un `null`.

En una versión más robusta, todas las operaciones públicas comprobarían explícitamente estos casos y mapearían cada ausencia a una excepción específica.

---

## 13. Uso de `OrderedVector`

`OrderedVector` se utiliza para rankings acotados:

```text
top 10 trabajadores mejor valorados
top 5 usuarios con más asistencias
```

Este TAD ordena usando un `Comparator`, pero su método `delete(...)` depende de que el elemento implemente `Comparable`.

Para resolverlo, `AbstractModel` implementa `Comparable<AbstractModel>` comparando por `id`.

Esto implica:

```text
Comparator del repositorio -> criterio de orden del ranking
compareTo por id           -> criterio de identidad para delete
```

Esta separación permite que:

```text
WorkerRepository ordene por rating
UserRepository ordene por número de asistencias
OrderedVector.delete elimine por id
```

---

## 14. Complejidades principales

Las complejidades dependen de las estructuras utilizadas.

### Acceso por id

| Entidad | Estructura | Complejidad esperada |
|---|---|---|
| Worker | HashTable | O(1) promedio |
| User | HashTable | O(1) promedio |
| System | AVL | O(log n) |
| Component | AVL | O(log n) |
| Issue | AVL | O(log n) |
| Assistance | AVL | O(log n) |
| Room | Vector | O(n) |
| IssueType | Vector | O(n) |
| Rating | Vector | O(n) |

### Operaciones relevantes

| Operación | Coste aproximado |
|---|---|
| Añadir worker | O(1) promedio |
| Añadir user | O(1) promedio |
| Añadir system | O(log S) |
| Añadir component | O(log C) |
| Crear issue | O(log C + log I) |
| Asignar issue | O(log I + log W) |
| Resolver issue | O(log R) |
| Añadir assistance | O(log A + U) |
| Asignar assistance | O(log A + 1) |
| Resolver assistance | O(log P) |
| Valorar assistance | O(log A + T) |
| Top 10 workers | O(10) al iterar |
| Top 5 users | O(5) al iterar |
| Followers/followings | O(grado del vértice) |
| Recommendations | O(grado + segundo nivel) |

Notas:

- `S`, `C`, `I`, `A` representan cantidades de sistemas, componentes, incidencias y asistencias.
- `P` representa asistencias pendientes de un trabajador.
- `T` representa el tamaño acotado del vector de ranking.
- En la práctica, `TOP_N_BEST_WORKERS` y `TOP_N_USERS_MOST_ASSISTANCES` son constantes pequeñas.

---

## 15. No uso de colecciones Java

Por motivos pedagógicos de la asignatura, no se usan colecciones de `java.util` para almacenar datos del dominio.

Se utilizan estructuras de la DSLib y estructuras propias:

```text
HashTable
DictionaryAVLImpl
PriorityQueue
DirectedGraphImpl
LinkedList
Iterator
DSListArray
DSLinkedList
RoundRobinList
StackLinkedList
OrderedVector
```

Sí se usan interfaces del lenguaje como:

```text
java.util.Comparator
java.util.function.Function
java.util.function.Supplier
java.util.function.BiConsumer
```

Estas no sustituyen estructuras de datos; se usan para parametrizar comportamiento.

---

## 16. Decisiones importantes

### Repositorios con estrategias

Se evita que `AbstractRepository` dependa de una estructura concreta. Cada repositorio elige su estrategia de almacenamiento.

Ventaja:

```text
misma API de repositorio
distintas estructuras internas
menor duplicación
más facilidad para cambiar almacenamiento
```

### Apuntadores directos

Se usan referencias directas entre entidades para evitar búsquedas repetidas.

Ejemplos:

```text
Assistance -> User
Assistance -> Worker
Assistance -> Room
Assistance -> IssueType
System -> Room
Component -> System
Issue -> Component
```

Ventaja:

```text
consultas O(1) entre objetos relacionados
código más directo
menos dependencias entre repositorios
```

### Índices auxiliares

Se mantienen índices para evitar recorridos frecuentes:

```text
topWorker
systemWithMostComponents
topWorkers
topAssistedUsers
ratingsSum
```

### Rating global

El rating del trabajador no se recalcula recorriendo todas las valoraciones. Se mantiene un acumulador:

```text
ratingsSum
```

La media se calcula como:

```text
ratingsSum / número de valoraciones
```

Esto permite obtener el rating global en O(1).

### Incidencias resueltas ordenadas

Las incidencias resueltas por trabajador se guardan en un AVL con clave incremental entera.

Esto permite devolverlas en orden de resolución, que es distinto al orden lexicográfico del id.

---

## 17. Limitaciones y mejoras posibles

### Robustez ante ids inexistentes

Algunos métodos usan `getX(id)` porque se asume que el contrato o los tests garantizan la existencia del registro. Una versión más defensiva usaría siempre `getXOrThrow(...)` en operaciones públicas.

### Rankings acotados

`topWorkers` y `topAssistedUsers` son vectores acotados. Esto es eficiente y encaja con los tests, pero si un elemento dentro del top empeora mucho, podría ser necesario reconstruir el ranking completo para garantizar que entra un candidato externo.

### `RatingRepository`

Actualmente el repositorio permite actualizar una valoración si se reutiliza el mismo `ratingId`. Si el dominio considerase cada valoración como evento inmutable, sería más correcto crear siempre una nueva valoración o usar una clave interna incremental.

### Helpers

Los helpers son vistas de consulta. En una versión más estricta podrían depender únicamente de la instancia de `SystemIssuesPR2Impl` / `SystemIssuesPR3Impl`, en vez de guardar referencias directas a repositorios.

### Multihilo

La implementación está pensada para ejecución secuencial. En un entorno multihilo habría que sincronizar operaciones que modifican rankings, acumuladores y grafos.

---

## 18. Resumen final

La práctica implementa el TAD `SystemIssues` usando una combinación de estructuras secuenciales y no lineales:

```text
HashTable      -> acceso rápido por id
AVL            -> datos grandes y ordenables
Vector         -> datos pequeños/acotados
Stack          -> resolución LIFO de incidencias
PriorityQueue  -> resolución priorizada de asistencias
RoundRobinList -> asignación equitativa por tipo de incidencia
OrderedVector  -> rankings
DirectedGraph  -> red social de trabajadores
```

El diseño busca equilibrar:

```text
claridad
cumplimiento del temario
eficiencia Big-O
encapsulación
compatibilidad con los tests
```

La separación en modelos, repositorios, estrategias de almacenamiento, utilidades y fachada permite mantener el código organizado y facilitar futuras ampliaciones.

