package com.jwbutler.krpg.geometry

import com.jwbutler.krpg.graphics.GameWindow
import java.awt.Point

data class Pixel(override val x: Int, override val y: Int) : IntPair
{
    operator fun plus(other: IntPair): Pixel = Pixel(x + other.x, y + other.y)
    operator fun minus(other: IntPair): Pixel = Pixel(x - other.x, y - other.y)

    fun toCoordinates(): Coordinates = pixelToCoordinates(this)

    override fun toString(): String
    {
        return "Pixel(x=$x, y=$y)"
    }

    companion object
    {
        fun fromPoint(p: Point): Pixel
        {
            return GameWindow.getInstance().mapPixel(Pixel(p.x, p.y))
        }
    }
}