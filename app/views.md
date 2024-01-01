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
    LoginView --> ResetPasswordView
    ResetPasswordView --> MainView
    LoginView --> PlanningbookMenuView:::blankStyle
    state Logged  {
        PlanningbookMenuView --> ActivitiesView
        PlanningbookMenuView --> TasksView
        PlanningbookMenuView --> ManagerPlaningBookView
        ManagerPlaningBookView --> PlanningbookMenuView
        ActivitiesView --> NewEditActivityView
        NewEditActivityView --> ActivitiesView
        ActivitiesView --> PlanningbookMenuView
        TasksView --> NewEditTaskView
        NewEditTaskView --> TasksView
        TasksView --> PlanningbookMenuView
        PlanningbookMenuView --> [*] : loggout
    }
````
