
## Task Data Model

````mermaid
---
title: Tasks Model
---
classDiagram

    Task "0" --> "*" Activity
    Activity "*" --> "1" Person
    Task "*" --> "1" Person
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