## My planning book App

````mermaid
---
title: Views navigation
---
stateDiagram-v2
    classDef blankStyle font-style:italic,font-weight:bold,fill:white,color:black
    
    [*] --> MainView
    MainView --> LoginView
    MainView --> RegisterView
    RegisterView --> MainView
    LoginView --> BookList:::blankStyle
    state Logged  {
        BookList --> TaskList
        BookList --> NewBook
        BookList --> EditShareBook
        TaskList --> NewTask
        TaskList --> EditTask    
        BookList --> [*]
    }
````
