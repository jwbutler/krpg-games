package com.jwbutler.krpg.utils

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.geometry.TILE_HEIGHT
import com.jwbutler.krpg.geometry.TILE_WIDTH
import com.jwbutler.krpg.graphics.GameWindow
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

fun getAdjacentCoordinates(coordinates: Coordinates): Set<Coordinates>
{
    val adjacentCoordinates = mutableSetOf<Coordinates>()
    for (dy in -1..1)
    {
        for (dx in -1..1)
        {
            val candidate = Coordinates(coordinates.x + dx, coordinates.y + dy)
            if (!candidate.isBlocked())
            {
                adjacentCoordinates.add(candidate)
            }
        }
    }
    val state = GameState.getInstance()
    return adjacentCoordinates
        .filter(state::containsCoordinates)
        .filter { it != coordinates }
        .toSet()
}

fun getAdjacentUnblockedCoordinates(coordinates: Coordinates): Set<Coordinates>
{
    return getAdjacentCoordinates(coordinates).filter { !it.isBlocked() }.toSet()
}

fun manhattanDistance(first: Coordinates, second: Coordinates): Int
{
    return abs(first.x - second.x) + abs(first.y + second.y)
}

fun manhattanDistance(first: Entity, second: Entity): Int
{
    return manhattanDistance(first.getCoordinates(), second.getCoordinates())
}

fun hypotenuse(first: Coordinates, second: Coordinates): Double
{
    val dx = first.x - second.x
    val dy = first.y - second.y
    return sqrt((dx*dx + dy*dy).toDouble())
}

/**
 * @return the top-left corner of the tile that would be at (x, y)
 */
fun coordinatesToPixel(coordinates: Coordinates): Pixel
{
    val cameraCoordinates = GameState.getInstance().getCameraCoordinates()
    val x = (coordinates.x - cameraCoordinates.x) * TILE_WIDTH + (GameWindow.WIDTH / 2) - (TILE_WIDTH / 2)
    val y = (coordinates.y - cameraCoordinates.y) * TILE_HEIGHT + (GameWindow.HEIGHT / 2) - (TILE_HEIGHT / 2)
    return Pixel(x, y)
}

// if camera is at (0, 0):
// (0, 0) -> (-TILE_WIDTH / 2, -TILE_HEIGHT / 2)
fun pixelToCoordinates(pixel: Pixel): Coordinates
{
    val cameraCoordinates = GameState.getInstance().getCameraCoordinates()
    val originTopLeft = Pixel(
        (GameWindow.WIDTH / 2) - (cameraCoordinates.x * TILE_WIDTH) - (TILE_WIDTH / 2),
        (GameWindow.HEIGHT / 2) - (cameraCoordinates.y * TILE_HEIGHT) - (TILE_HEIGHT/ 2)
    )

    val x = (pixel.x - originTopLeft.x) / TILE_WIDTH
    val y = (pixel.y - originTopLeft.y) / TILE_HEIGHT
    return Coordinates(x, y)
}