package com.jwbutler.krpg.geometry

class Pixel(x: Int, y: Int) : IntPair(x, y)
{
    operator fun plus(other: IntPair): Pixel = Pixel(x + other.x, y + other.y)
    operator fun minus(other: IntPair): Pixel = Pixel(x - other.x, y - other.y)

    override fun toString(): String
    {
        return "Pixel(x=$x, y=$y)"
    }
}