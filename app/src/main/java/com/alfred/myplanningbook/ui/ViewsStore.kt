package com.alfred.myplanningbook.ui

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.loggedview.ActivitiesManagerView
import com.alfred.myplanningbook.ui.loggedview.BookMenuView
import com.alfred.myplanningbook.ui.loggedview.PlanningBookManagerView
import com.alfred.myplanningbook.ui.loggedview.TasksManagerView
import com.alfred.myplanningbook.ui.loggedview.library.LibraryView
import com.alfred.myplanningbook.ui.loggedview.vehicles.VehiclesDetailView
import com.alfred.myplanningbook.ui.loggedview.vehicles.VehiclesView

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object ViewsStore {

    private var bookMenuView: BookMenuView? = null
    private var planningBookmangerView: PlanningBookManagerView? = null
    private var tasksManagerView: TasksManagerView? = null
    private var activitiesManagerView: ActivitiesManagerView? = null
    private var libraryView: LibraryView? = null
    private var vehiclesView: VehiclesView? = null
    private var vehiclesDetailView: VehiclesDetailView? = null

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

    fun getLibraryView(): LibraryView {
        if(libraryView == null) {
            Klog.line("ViewsStore", "getLibraryView", "creating LibraryrView!")
            libraryView = LibraryView()
        }
        return libraryView!!
    }

    fun getVehiclesView(): VehiclesView {
        if(vehiclesView == null) {
            Klog.line("ViewsStore", "getVehiclesView", "creating VehiclesView!")
            vehiclesView = VehiclesView()
        }
        return vehiclesView!!
    }

    fun getVehiclesDetailView(): VehiclesDetailView {
        if(vehiclesDetailView == null) {
            Klog.line("ViewsStore", "getVehiclesDetailView", "creating VehiclesDetailView!")
            vehiclesDetailView = VehiclesDetailView()
        }
        return vehiclesDetailView!!
    }

    fun cleanLoggedViews() {
        bookMenuView = null
        planningBookmangerView = null
        tasksManagerView = null
        activitiesManagerView = null
        vehiclesView = null
        vehiclesDetailView = null
    }
}