
## Task Data Model

````mermaid
---
title: Tasks Model
---
classDiagram
    

    BigTask <|-- Task
    Task <|-- Persona
    class BigTask {
        +String name
        +String description
        +String persona
        +Date date
        +Date initDate
        +Date endDate
    }
    class Task {
        +String name
        +String description
        +String persona
        +Date date
    }
    class Persona {
        +String name
    }
````