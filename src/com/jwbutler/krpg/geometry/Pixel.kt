package com.jwbutler.krpg.geometry

class Pixel(x: Int, y: Int) : IntPair(x, y)
{
    fun plus(other: IntPair): Pixel = Pixel(x + other.x, y + other.y)
    fun minus(other: IntPair): Pixel = Pixel(x - other.x, y - other.y)
}