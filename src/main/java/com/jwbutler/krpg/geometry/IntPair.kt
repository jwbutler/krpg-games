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
        fun of(x: Int, y: Int) = object : IntPair
        {
            override val x = x
            override val y = y
        }
    }
}