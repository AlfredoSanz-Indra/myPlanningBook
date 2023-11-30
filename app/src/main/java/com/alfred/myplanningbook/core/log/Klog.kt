package com.alfred.myplanningbook.core.log

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object Klog {

    private var level: KlogLevel = KlogLevel.DEBUG;

    fun updateLevel(newState: KlogLevel) {
        level = newState
    }
    fun line(kmessage: String) {
        when(level.name) {
            KlogLevel.DEBUG.name ->
                println("*** KLOG DEBUG - $kmessage")
            KlogLevel.PROD.name ->
                println("*** KLOG PROD ***********")
        }
    }

    fun line(kclass: String, kmethod: String, kmessage: String) {

        when(level.name) {
            KlogLevel.DEBUG.name ->
                println("*** KLOG DEBUG - $kclass $kmethod -> $kmessage")

            KlogLevel.PROD.name ->
                println("*** kLOG PROD ***********")
        }
    }
}