package com.jwbutler.krpg.geometry

class Coordinates(x: Int, y: Int) : IntPair(x, y)
{
    operator fun plus(other: IntPair): Coordinates = Coordinates(x + other.x, y + other.y)
    operator fun minus(other: IntPair): Coordinates = Coordinates(x - other.x, y - other.y)

    override fun toString(): String
    {
        return "Coordinates(x=$x, y=$y)"
    }
}