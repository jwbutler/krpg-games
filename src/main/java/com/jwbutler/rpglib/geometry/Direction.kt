package com.jwbutler.rpglib.geometry

import kotlin.math.hypot
import kotlin.math.round

enum class Direction(val dx: Int, val dy: Int)
{
    N(1, -1),
    NE(1, 0),
    E(1, 1),
    SE(0, 1),
    S(-1, 1),
    SW(-1, 0),
    W(-1, -1),
    NW(0, -1);

    companion object
    {
        fun from(xy: IntPair): Direction
        {
            return values()
                .find { d -> (d.dx == xy.x && d.dy == xy.y) }
                ?: error("Could not find direction from ${xy}")
        }

        fun from(x: Int, y: Int): Direction
        {
            return values()
                .find { d -> (d.dx == x && d.dy == y) }
                ?: error("Could not find direction from ${x}, ${y}")
        }

        fun closest(xy: IntPair): Direction
        {
            val magnitude = hypot(xy.x.toDouble(), xy.y.toDouble())
            val dx = round(xy.x / magnitude).toInt()
            val dy = round(xy.y / magnitude).toInt()
            return from(dx, dy)
        }

        fun between(target: Coordinates, coordinates: Coordinates): Direction
        {
            return from(target - coordinates)
        }

        fun closestBetween(target: Coordinates, coordinates: Coordinates): Direction
        {
            return closest(target - coordinates)
        }
    }
}