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
                println("*** DEBUG- $kmessage")
            KlogLevel.INFO.name ->
                println("*** INFO- $kmessage")
        }
    }

    fun line(kclass: String, kmethod: String, kmessage: String) {

        when(level.name) {
            KlogLevel.DEBUG.name ->
                println("*** DEBUG- $kclass $kmethod -> $kmessage")

            KlogLevel.INFO.name ->
                println("*** INFO- $kclass $kmethod -> $kmessage")
        }
    }
}