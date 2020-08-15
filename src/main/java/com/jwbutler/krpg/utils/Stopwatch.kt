package com.jwbutler.krpg.utils

class Stopwatch(private val name: String)
{
    var times = mutableListOf<Pair<String, Long>>()
        .also { it.add("start" to System.currentTimeMillis()) }

    fun log(description: String): Stopwatch
    {
        times.add(description to System.currentTimeMillis())
        return this
    }

    fun print(min: Int)
    {
        log("")
        val lines = mutableListOf<String>()
        val total = times.last().second - times[0].second
        if (total < min)
        {
            return
        }
        lines += "$name: $total ms"
        for (i in 0 until times.lastIndex)
        {
            val delta = times[i + 1].second - times[i].second
            lines += "    ${times[i].first}: $delta ms"
        }
        println(lines.joinToString("\n"))
    }

    fun print()
    {
        print(0)
    }
}