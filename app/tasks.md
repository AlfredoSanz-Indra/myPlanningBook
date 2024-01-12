
## Task Data Model

````mermaid
---
title: Tasks Model
---
classDiagram

    Task "0" --> "*" Activity
    Activity "*" --> "1" Person
    Task "*" --> "1" Person
    Task "*" --> "1" TaskType
    Owner "*" --> "*" PlanningBook
    class PlanningBook {
        -String id
        +Number idOwner
        +String name
    }
    class Owner  {
        -String id
        +String name
        +String email
        +String activePlanningBook
        +String[] planningBooks
    }
    class Task {
        -String id
        +String idPlanningBook
        +String idPersona
        +String idType
        +String name
        +String description
        +Date date
        +Date initDate
        +Date endDate
    }
    class Activity {
        -String id
        +String idPlanningBook
        +String idTask
        +String idPersona
        +String name
        +String description
        +Date date
    }
    class Person {
        -String id
        +String name
    }

    class TaskType {
        "childsAuto, childsNoAuto"
        -String id
        -String desc
    }
````