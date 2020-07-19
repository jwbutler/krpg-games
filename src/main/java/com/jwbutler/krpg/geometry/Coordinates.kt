package com.jwbutler.krpg.geometry

import com.jwbutler.krpg.graphics.GameWindow

class Coordinates(x: Int, y: Int) : IntPair(x, y)
{
    operator fun plus(other: IntPair): Coordinates = Coordinates(x + other.x, y + other.y)
    operator fun minus(other: IntPair): Coordinates = Coordinates(x - other.x, y - other.y)

    /**
     * @return the top-left corner of the floor tile at these coordinates
     */
    fun toPixel(): Pixel
    {
        val tileWidth = 32  // made up
        val tileHeight = 24 // made up
        val x = (GameWindow.WIDTH / 2) - (tileWidth / 2) + ((this.x - this.y) * tileWidth / 2)
        val y = (this.x + this.y) * tileHeight / 2
        return Pixel(x, y)
    }

    override fun toString(): String
    {
        return "Coordinates(x=$x, y=$y)"
    }
}