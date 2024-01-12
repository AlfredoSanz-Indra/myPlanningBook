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
                println("*** kLOG PROD **** $kclass $kmethod *******")
        }
    }

    /**
     * print line only in debug mode
     */
    fun linedbg(kclass: String, kmethod: String, kmessage: String) {

        when(level.name) {
            KlogLevel.DEBUG.name ->
                println("*** KLOG DEBUG - $kclass $kmethod -> $kmessage")
        }
    }

    fun stackTrace(kclass: String, kmethod: String, stack: Array<StackTraceElement>) {
        when(level.name) {
            KlogLevel.DEBUG.name ->
                stack.forEach{ it ->  println("*** KLOG DEBUG - $kclass $kmethod -> $it")}

            KlogLevel.PROD.name ->
                stack.forEach{ it ->  println("*** KLOG PROD - $kclass $kmethod  ******* -> $it")}
        }
    }
}