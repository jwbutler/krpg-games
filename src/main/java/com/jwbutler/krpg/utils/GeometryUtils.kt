package com.jwbutler.krpg.utils

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.geometry.Rectangle
import com.jwbutler.krpg.graphics.GameRenderer
import java.awt.Point
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

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
    val renderer = GameRenderer.getInstance()
    val cameraCoordinates = GameState.getInstance().getHumanPlayer().cameraCoordinates
    val tileWidth = (renderer.tileWidth * renderer.scaleRatioX).roundToInt()
    val tileHeight = (renderer.tileHeight * renderer.scaleRatioY).roundToInt()
    val x = (coordinates.x - cameraCoordinates.x) * tileWidth + (renderer.gameWidth / 2) - (tileWidth / 2)
    val y = (coordinates.y - cameraCoordinates.y) * tileHeight + (renderer.gameHeight / 2) - (tileHeight / 2)
    return Pixel(x, y)
}

// if camera is at (0, 0):
// (0, 0) -> (-TILE_WIDTH / 2, -TILE_HEIGHT / 2)
fun pixelToCoordinates(pixel: Pixel): Coordinates
{
    val cameraCoordinates = GameState.getInstance().getHumanPlayer().cameraCoordinates
    val renderer = GameRenderer.getInstance()
    val tileWidth = (renderer.tileWidth * renderer.scaleRatioX).roundToInt()
    val tileHeight = (renderer.tileHeight * renderer.scaleRatioY).roundToInt()
    val originTopLeft = Pixel(
        (renderer.gameWidth / 2) - (cameraCoordinates.x * tileWidth) - (tileWidth / 2),
        (renderer.gameHeight / 2) - (cameraCoordinates.y * tileHeight) - (tileHeight / 2)
    )

    val x = (pixel.x - originTopLeft.x) / tileWidth
    val y = (pixel.y - originTopLeft.y) / tileHeight
    return Coordinates(x, y)
}

fun rectFromPixels(first: Pixel, vararg rest: Pixel): Rectangle
{
    val rect = Rectangle(Point(first.x, first.y))
    for (pixel in rest)
    {
        rect.add(pixel.x, pixel.y)
    }
    return rect
}

fun getAverageCoordinates(coordinates: Collection<Coordinates>): Coordinates
{
    val x = coordinates.map(Coordinates::x).average().roundToInt()
    val y = coordinates.map(Coordinates::y).average().roundToInt()
    return Coordinates(x, y)
}