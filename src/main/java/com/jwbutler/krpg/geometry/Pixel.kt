package com.jwbutler.krpg.geometry

data class Pixel(override val x: Int, override val y: Int) : IntPair
{
    operator fun plus(other: IntPair): Pixel = Pixel(x + other.x, y + other.y)
    operator fun minus(other: IntPair): Pixel = Pixel(x - other.x, y - other.y)

    override fun toString(): String
    {
        return "Pixel(x=$x, y=$y)"
    }
}