package com.jwbutler.krpg.core

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.IntPair
import kotlin.math.round
import kotlin.math.sqrt

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
            return Direction.values()
                .find { d -> (d.dx == xy.x && d.dy == xy.y) }
                ?: error("Could not find direction from ${xy}")
        }

        fun closest(xy: IntPair): Direction
        {
            val magnitude = sqrt((xy.x * xy.x + xy.y * xy.y).toDouble())
            val dx = round(xy.x / magnitude).toInt()
            val dy = round(xy.y / magnitude).toInt()
            return Direction.from(IntPair.of(dx, dy))
        }

        fun between(target: Coordinates, coordinates: Coordinates): Direction
        {
            return Direction.from(target - coordinates)
        }

        fun closestBetween(target: Coordinates, coordinates: Coordinates): Direction
        {
            return Direction.closest(target - coordinates)
        }
    }
}