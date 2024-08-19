package com.alfred.myplanningbook.ui

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.loggedview.ActivitiesManagerView
import com.alfred.myplanningbook.ui.loggedview.BookMenuView
import com.alfred.myplanningbook.ui.loggedview.PlanningBookManagerView
import com.alfred.myplanningbook.ui.loggedview.TasksManagerView

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object ViewsStore {

    private var bookMenuView: BookMenuView? = null
    private var planningBookmangerView: PlanningBookManagerView? = null
    private var tasksManagerView: TasksManagerView? = null
    private var activitiesManagerView: ActivitiesManagerView? = null

    fun getBookMenuView(): BookMenuView {
        if(bookMenuView == null) {
            Klog.line("ViewsStore", "getBookMenuView", "creating BookMenuView!")
            bookMenuView = BookMenuView()
        }
        return bookMenuView!!
    }

    fun getPlanningBookManagerView(): PlanningBookManagerView {
        if(planningBookmangerView == null) {
            Klog.line("ViewsStore", "getPlanningBookManagerView", "creating planningBookmangerView!")
            planningBookmangerView = PlanningBookManagerView()
        }
        return planningBookmangerView!!
    }

    fun getTasksManagerView(): TasksManagerView {
        if(tasksManagerView == null) {
            Klog.line("ViewsStore", "getTasksManagerView", "creating TasksManagerView!")
            tasksManagerView = TasksManagerView()
        }
        return tasksManagerView!!
    }

    fun getActivitiesManagerView(): ActivitiesManagerView {
        if(activitiesManagerView == null) {
            Klog.line("ViewsStore", "getActivitiesManagerView", "creating ActivitiesManagerView!")
            activitiesManagerView = ActivitiesManagerView()
        }
        return activitiesManagerView!!
    }

    fun cleanLoggedViews() {
        bookMenuView = null
        planningBookmangerView = null
        tasksManagerView = null
        activitiesManagerView = null
    }
}