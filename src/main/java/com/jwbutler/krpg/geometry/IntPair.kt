package com.jwbutler.krpg.geometry

/**
 * Base class for [Coordinates], [Pixel], etc.
 */
open class IntPair(val x: Int, val y: Int)
{
    override fun toString(): String
    {
        return "IntPair(x=$x, y=$y)"
    }
}