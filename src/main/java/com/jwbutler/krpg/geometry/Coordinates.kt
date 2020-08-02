package com.jwbutler.krpg.geometry

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.utils.coordinatesToPixel

data class Coordinates(override val x: Int, override val y: Int) : IntPair
{
    operator fun plus(other: IntPair): Coordinates = Coordinates(x + other.x, y + other.y)
    operator fun minus(other: IntPair): Coordinates = Coordinates(x - other.x, y - other.y)
    operator fun plus(direction: Direction): Coordinates = Coordinates(x + direction.dx, y + direction.dy)

    /**
     * @return the top-left corner of the floor tile at these coordinates
     */
    fun toPixel(): Pixel = coordinatesToPixel(this)

    fun isBlocked(): Boolean
    {
        return GameState.getInstance().isBlocked(this)
    }

    override fun toString(): String
    {
        return "Coordinates(x=$x, y=$y)"
    }
}