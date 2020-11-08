package com.jwbutler.rpglib.geometry

import java.awt.Point

data class Pixel(override val x: Int, override val y: Int) : IntPair
{
    operator fun plus(other: IntPair): Pixel = Pixel(x + other.x, y + other.y)
    operator fun minus(other: IntPair): Pixel = Pixel(x - other.x, y - other.y)

    override fun toString(): String
    {
        return "Pixel(x=$x, y=$y)"
    }

    companion object
    {
        fun fromPoint(p: Point): Pixel
        {
            return Pixel(p.x, p.y)
        }
    }
}