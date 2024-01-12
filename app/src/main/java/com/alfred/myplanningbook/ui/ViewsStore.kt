package com.alfred.myplanningbook.ui

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.loggedview.BookMenuView
import com.alfred.myplanningbook.ui.loggedview.viewmodel.PlanningBookManager

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object ViewsStore {

    private var bookMenuView: BookMenuView? = null
    private var planningBookmangerView: PlanningBookManager? = null

    fun getBookMenuView(): BookMenuView {
        if(bookMenuView == null) {
            Klog.line("ViewsStore", "getBookMenuView", "creating BookMenuView!")
            bookMenuView = BookMenuView()
        }
        return bookMenuView!!
    }

    fun getPlanningBookManagerView(): PlanningBookManager {
        if(planningBookmangerView == null) {
            Klog.line("ViewsStore", "getPlanningBookManagerView", "creating planningBookmangerView!")
            planningBookmangerView = PlanningBookManager()
        }
        return planningBookmangerView!!
    }

    fun cleanLoggedViews() {
        bookMenuView = null
    }
}