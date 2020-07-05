package com.jwbutler.krpg.geometry

class Coordinates(x: Int, y: Int) : IntPair(x, y)
{
    fun plus(other: IntPair): Coordinates = Coordinates(x + other.x, y + other.y)
    fun minus(other: IntPair): Coordinates = Coordinates(x - other.x, y - other.y)
}