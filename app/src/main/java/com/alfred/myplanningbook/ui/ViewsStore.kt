package com.alfred.myplanningbook.ui

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.loggedview.BookMenuView
import com.alfred.myplanningbook.ui.loggedview.PlanningBookManagerView

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object ViewsStore {

    private var bookMenuView: BookMenuView? = null
    private var planningBookmangerView: PlanningBookManagerView? = null

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

    fun cleanLoggedViews() {
        bookMenuView = null
    }
}