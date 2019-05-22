package com.shang.livedata

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var a: a? = a()
        println(a)
        a.let {
            a?.z
            a?.y
        }
        println(a)
    }

    class a() {
        var z: String? = "zzz"
        var y: String? = null
    }
}
