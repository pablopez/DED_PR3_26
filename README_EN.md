# System Issues Manager (PR2 & PR3)

Internal IT issue, assistance, infrastructure and worker social-network management system developed for the **Data Structure Design (DED)** course at the **Universitat Oberta de Catalunya (UOC)**.

## Author

- **Name:** Pablo López Jiménez
- **Email:** plopezjim@uoc.edu

---

## 1. Project Goal and Context

The goal of this project is to computerize and optimize the internal management of IT issues inside an organization. The system evolves in two main stages.

### PR2: Core issue-management system

The first stage focuses on the core technical-maintenance workflow:

- Register workers.
- Register computer systems.
- Register components installed in those systems.
- Create technical issues linked to faulty components.
- Assign issues to workers.
- Resolve assigned issues using a **LIFO** policy.

In this phase, a worker solves pending issues in reverse order of assignment, which is naturally modelled using a stack.

### PR3: Extension and scalability

The second stage extends the system with more domain features and larger data volumes:

- Physical rooms where systems are located.
- Company users with hierarchical roles.
- Issue types to ensure that each worker only handles issues matching their specialization.
- Assistance requests for non-hardware support needs.
- Automatic assistance assignment using a round-robin policy among qualified workers.
- Assistance resolution using a priority queue.
- Rating system for resolved assistances.
- Worker social network implemented with a directed graph.

---

## 2. Architectural Constraints

The project follows the data-structure restrictions defined by the course.

### Not allowed

Java collection classes from `java.util` must not be used as data structures:

- `ArrayList`
- `LinkedList`
- `Vector`
- `HashMap`
- `Hashtable`
- `HashSet`
- `TreeSet`
- `PriorityQueue`
- `Stack`
- `Dictionary`
- `Collections`

### Allowed interpretation

Utility interfaces such as:

- `java.util.Comparator`
- `java.util.function.Function`
- `java.util.function.Supplier`
- `java.util.function.BiConsumer`

are not used as data structures. They are only used to express ordering criteria or reusable behaviour. The actual storage structures are provided by **DSLib** or by project-specific utility classes.

### Required library

The project uses the UOC data-structure library:

```text
DSLib-2.1-7.jar
```

---

## 3. Architecture and Design Decisions

The architecture is organized in layers to avoid concentrating all logic inside the public TAD implementation.

```text
SystemIssuesPR2Impl / SystemIssuesPR3Impl
        ↓
Service layer
        ↓
Repository layer
        ↓
Domain models
```

### 3.1 Facade layer

`SystemIssuesPR2Impl` and `SystemIssuesPR3Impl` act as the public entry points required by the assignment interfaces.

Their responsibility is to expose the public API and delegate work to the service layer.

They should not contain data-structure logic or cross-repository coordination. This avoids turning the main TAD implementation into a *God Object*.

### 3.2 Service layer

The service layer contains use cases and business rules. It is responsible for coordinating several repositories when an operation affects more than one domain entity.

Current service responsibilities are organized as follows:

| Service | Responsibility |
|---|---|
| `CoreService` | Groups all creation/addition operations. |
| `InfrastructureService` | Handles relationships and queries involving rooms, systems and components. |
| `IssueService` | Handles issue assignment, issue resolution and solved-issue queries. |
| `WorkerIssueTypeService` | Manages the relationship between workers and issue types. |
| `AssistanceService` / `AssistanceWorkflowService` | Handles assistance assignment, resolution and rating. |
| `RankingService` | Provides top workers and top users queries. |
| `WorkerSocialNetworkService` | Handles followers, followings, recommendations and social queries. |

### 3.3 Repository layer

Repositories are responsible for:

- Storing entities.
- Searching entities by ID.
- Providing `getX(id)` methods that may return `null`.
- Providing `getXOrThrow(id)` methods when a missing entity must be converted into a domain exception.
- Maintaining auxiliary indexes such as rankings, solved issues or social graph vertices.

Repositories do not call each other. Cross-entity coordination belongs to services.

The repository layer also uses a strategy-based design to abstract different storage structures behind a common repository style.

### 3.4 Model layer

Models represent domain entities and local relationships. They contain state and simple entity-level behaviour, for example:

- `Worker.solveNextIssue()`
- `Worker.solveNextAssistance()`
- `Worker.setIssueType(...)`
- `System.setRoom(...)`
- `Assistance.setRating(...)`

When a relationship is bidirectional, the model method is responsible for keeping both sides consistent. For example, `Worker.setIssueType(...)` updates both the worker and the corresponding `IssueType`.

---

## 4. Domain Model

### Base model classes

| Class | Purpose |
|---|---|
| `Identifiable` | Ensures that every stored entity exposes a unique ID. |
| `AbstractModel` | Common base class for models. It provides the ID and natural comparison by ID. |

The natural comparison by ID is used mainly as an identity criterion in structures such as `OrderedVector.delete(...)`. Ranking order is still defined by explicit comparators.

### Main entities

| Entity | Description |
|---|---|
| `Worker` | Technician responsible for issues and assistances. Maintains pending issues, assigned assistances, solved items and rating metrics. |
| `User` | Company user who can request assistances. Has a role used for assistance priority. |
| `System` | IT system with location, components and assigned room. |
| `Component` | Hardware or logical component that can have issues. |
| `Issue` | Technical issue associated with a component and an issue type. |
| `IssueType` | Category/specialization such as hardware, software or network. Maintains workers qualified for that type. |
| `Room` | Physical room where systems are located. |
| `Assistance` | Support request not necessarily linked to hardware. Prioritized by user role, request date and ID. |
| `Rating` | User rating for a resolved assistance. Stores resolution, speed and treatment scores. |

---

## 6. Custom Exceptions

All custom exceptions extend `DSException`.

### Not-found exceptions

These exceptions represent missing entities:

- `WorkerNotFoundException`
- `UserNotFoundException`
- `SystemNotFoundException`
- `RoomNotFoundException`
- `ComponentNotFoundException`
- `IssueNotFoundException`
- `IssueTypeNotFoundException`
- `AssistanceNotFoundException`
- `RatingNotFoundException`
- `FollowerNotFoundException`

### Business-rule and state exceptions

These exceptions represent invalid operations according to the domain state:

- `ComponentAlreadyInstalledException`
- `IssueAlreadyAssignedException`
- `IssueAlreadyResolvedException`
- `WorkerNotAssignedToIssueTypeException`
- `UserIsNotCreatorException`
- `AssistanceNotResolvedException`

### Empty-result or unavailable-resource exceptions

These exceptions are used when a valid operation cannot return a result:

- `NoWorkerException`
- `NoUserException`
- `NoSystemsException`
- `NoIssuesException`
- `NoAssistanceException`
- `NoFollowersException`
- `NoFollowedException`
- `SystemHasNoComponentsException`

---

## 7. Data Structures and Justification

| Entity / Relationship | Structure | Reason |
|---|---|---|
| Workers | `HashTable` | Fast access by ID for a large and relatively stable collection. |
| Users | `HashTable` | Fast access by ID and frequent lookups from assistance operations. |
| Systems | `DictionaryAVLImpl` | Ordered dictionary with logarithmic access for a growing collection. |
| Components | `DictionaryAVLImpl` | Large and growing collection requiring efficient search. |
| Issues | `DictionaryAVLImpl` | Efficient access by ID and scalable growth. |
| Assistances | `DictionaryAVLImpl` | Growing collection with efficient access by ID. |
| Rooms | `DSListArray` | Small and mostly static collection. |
| Issue types | `DSListArray` | Small controlled collection of categories. |
| Ratings | `DSListArray` | Initially treated as a bounded/simple collection, although this is noted as future technical debt. |
| Pending issues by worker | `StackLinkedList` | LIFO resolution policy required by the assignment. |
| Assigned assistances by worker | `PriorityQueue` | Resolution depends on priority: user role, date and ID. |
| Solved issues / assistances | `DictionaryAVLImpl` | Stored with incremental keys to preserve resolution order. |
| Top workers / top users | `OrderedVector` | Bounded ranking structure for direct top-N queries. |
| Workers by issue type | `RoundRobinList` | Circular assignment among qualified workers. |
| Worker social network | `DirectedGraphImpl` | Natural representation of follower relationships. |

---

## 8. Complexity Overview

The following table summarizes the expected complexity of the most relevant operations. Exact behaviour may depend on the implementation details of DSLib structures.

| Operation | Main structure | Expected complexity |
|---|---|---|
| Search worker/user by ID | `HashTable` | Average `O(1)` |
| Search system/component/issue/assistance by ID | `DictionaryAVLImpl` | `O(log n)` |
| Add room/issue type/rating | `DSListArray` | `O(n)` search/update in the worst case |
| Push pending issue | `StackLinkedList` | `O(1)` |
| Solve next issue | `StackLinkedList` | `O(1)` |
| Assign assistance to worker | `RoundRobinList` + repository lookup | Usually `O(1)` after lookup |
| Solve next assistance | `PriorityQueue` | `O(log n)` |
| Update bounded ranking | `OrderedVector` | `O(k)`, where `k` is the ranking size |
| Get followers/followings | `DirectedGraphImpl` | Proportional to vertex degree |
| Recommendations | `DirectedGraphImpl` | Proportional to direct followers and their followers |

---

## 9. Worker Social Network Semantics

The social network is represented using a directed graph.

The method:

```java
addFollower(followed, follower)
```

creates the edge:

```text
follower -> followed
```

Therefore:

```text
A -> B
```

means:

```text
A follows B
```

### Followers and followings

| Query | Meaning |
|---|---|
| `getFollowers(B)` | Workers that point to `B`, i.e. workers who follow `B`. |
| `getFollowings(A)` | Workers pointed to by `A`, i.e. workers followed by `A`. |
| `isFollowing(A, B)` | Returns true when the edge `A -> B` exists. |

### Recommendations

The official tests expect recommendations to be based on **followers of followers**.

For example, if:

```text
W2 -> W1
W3 -> W1
W4 -> W2
W5 -> W3
W6 -> W3
```

then recommendations for `W1` are:

```text
W4, W5, W6
```

because they are second-level followers and do not already follow `W1` directly.

### Unfollowed colleagues by issue type

`getUnfollowedWorkersWithAssignedIssueType(workerId, issueTypeId)` returns workers assigned to the requested issue type who do **not** directly follow the given worker.

For `worker = W1`, a candidate `C` is included when:

```java
!isFollowing(C, W1)
```

and `C` is not `W1`.

---

## 10. Helper Classes

Helper classes are used only as test-inspection views.

They should:

- Return current system state.
- Return `null` for missing entities in `getX(id)` methods.
- Return `0` for counters when the base entity does not exist.
- Avoid business logic.
- Avoid modifying repositories.
- Avoid throwing business exceptions.

Business validation belongs to public operations and services, not to helpers.

---

## 11. Assumptions and Known Behaviour

### Direct repository getters

The project distinguishes between:

```java
getX(id)
```

and:

```java
getXOrThrow(id)
```

`getX(id)` may return `null`. It is used in helpers and in places where the assignment contract assumes the ID already exists.

`getXOrThrow(id)` is used by services when the public operation must validate existence and throw the exception defined by the interface.

### Interface limitations

Some public methods in the assignment interface do not declare all possible not-found exceptions. In those cases, the implementation follows the assignment contract and assumes that certain IDs exist before the operation is called.

A more defensive production version would add exceptions such as:

- `RoomNotFoundException`
- `IssueTypeNotFoundException`
- `AssistanceNotFoundException`

to more method signatures.

---

## 12. Testing

The project includes unit and integration tests using JUnit 4.

The test suite validates:

- Architecture restrictions, including the absence of Java collection structures.
- Entity creation and repository state.
- Issue assignment and LIFO resolution.
- Component installation.
- Room-system assignment.
- Assistance creation, assignment and priority-based resolution.
- Rating rules.
- Worker and user rankings.
- Social graph operations: followers, followings, recommendations and unfollowed colleagues.

CSV files under `src/test/resources/` are used to load realistic test data for workers, systems, components, issues, users and assistances.

---

## 13. Technical Debt and Future Improvements

### 13.1 Deep user issue queries

Some user-related issue queries may require traversing several levels:

```text
User -> Systems -> Components -> Issues
```

This can become expensive when the number of systems and components grows.

**Possible improvement:** maintain a direct issue index inside `User` or in a dedicated repository-level index. This would make reads faster at the cost of additional update work.

### 13.2 Rating scalability

Ratings are currently treated as a small or relatively simple collection. However, each resolved assistance may eventually receive a rating, so the number of ratings can grow linearly.

**Possible improvement:** replace the current sequential structure with `DictionaryAVLImpl` for logarithmic access and better scalability.

### 13.3 Workers by issue type

`RoundRobinList` is appropriate for circular assignment, but if a type accumulates a very large number of workers, searching and deletion may become less efficient.

**Possible improvement:** combine the round-robin structure with an auxiliary dictionary or hash table to speed up membership checks and removals.

### 13.4 Bounded rankings

`OrderedVector` is efficient for fixed-size top-N rankings. However, when a ranked element changes and should leave the ranking, a fully robust implementation may require either storing all ranked candidates or rebuilding the top list from a larger auxiliary index.

---

## 14. Technologies and Dependencies

- **Language:** Java
- **Build tool:** Maven
- **Core library:** DSLib 2.1-7
- **Testing:** JUnit 4.13
- **Architecture testing:** ArchUnit 1.3.0
- **CSV utilities:** Apache Commons CSV 1.10.0
- **Logging dependencies:** SLF4J
