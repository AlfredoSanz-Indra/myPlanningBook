package com.alfred.myplanningbook.ui

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.ui.loggedview.BookMenuView

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object ViewsStore {

    private var bookMenuView: BookMenuView? = null

    fun getBookMenuView(): BookMenuView {
        if(bookMenuView == null) {
            Klog.line("ViewsStore", "getBookMenuView", "creating BookMenuView!")
            bookMenuView = BookMenuView()
        }
        return bookMenuView!!
    }

    fun cleanLoggedViews() {
        bookMenuView = null
    }
}