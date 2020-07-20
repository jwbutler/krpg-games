package com.jwbutler.krpg.geometry

/**
 * Base class for [Coordinates], [Pixel], etc.
 */
interface IntPair
{
    val x: Int
    val y: Int

    companion object
    {
        fun of(x: Int, y: Int): IntPair = BasicIntPair(x, y)
    }
}

private data class BasicIntPair(override val x: Int, override val y: Int) : IntPair