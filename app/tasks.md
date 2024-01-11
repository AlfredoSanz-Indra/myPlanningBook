
## Task Data Model

````mermaid
---
title: Tasks Model
---
classDiagram

    Task "0" --> "*" Activity
    Activity "*" --> "1" Person
    Task "*" --> "1" Person
    Owner "*" --> "1" PlanningBook
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
        -Number id
        +Number idPersona
        +String name
        +String description
        +Date date
        +Date initDate
        +Date endDate
    }
    class Activity {
        -Number id
        +Number idTask
        +Number idPersona
        +String name
        +String description
        +Date date
    }
    class Person {
        -Number id
        +String name
    }
````