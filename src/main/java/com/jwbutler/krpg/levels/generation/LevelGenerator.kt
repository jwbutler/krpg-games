package com.jwbutler.krpg.levels.generation

import com.jwbutler.krpg.entities.objects.Wall
import com.jwbutler.krpg.entities.tiles.RPGTileType
import com.jwbutler.rpglib.entities.objects.GameObject
import com.jwbutler.rpglib.entities.tiles.Tile
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Dimensions
import com.jwbutler.rpglib.geometry.IntPair
import com.jwbutler.rpglib.levels.Level
import com.jwbutler.rpglib.levels.VictoryCondition
import kotlin.math.sign

private const val MIN_SECTION_WIDTH = 8
private const val MIN_SECTION_HEIGHT = 8
private const val SECTION_ROOM_PADDING_X = 1
private const val SECTION_ROOM_PADDING_Y = 1
private const val FRACTION_OF_SECTIONS_WITH_ROOMS = 0.5

interface LevelGenerator
{
    fun generate(
        dimensions: Dimensions,
        victoryCondition: VictoryCondition
    ): Level

    companion object
    {
        fun create(): LevelGenerator = LevelGeneratorImpl()
    }
}

private data class Rectangle
(
    val left: Int,
    val top: Int,
    val width: Int,
    val height: Int
)
{
    val right = left + width - 1
    val bottom = top + height - 1
}

private enum class Orientation
{
    HORIZONTAL,
    VERTICAL;

    companion object
    {
        fun from(dx: Int, dy: Int): Orientation
        {
            if (dx != 0 && dy == 0)
            {
                return HORIZONTAL
            }
            else if (dy != 0 && dx == 0)
            {
                return VERTICAL
            }
            else
            {
                throw IllegalArgumentException()
            }
        }
    }
}

/**
 * The two values are the min and max possible locations for the joining point.
 */
private data class Connection
(
    val min: Coordinates,
    val max: Coordinates
)

private data class MapSections
(
    val sections: Set<Rectangle>,
    val rooms: Set<Rectangle>,
    val connections: Set<Connection>
)

private class LevelGeneratorImpl : LevelGenerator
{
    override fun generate(
        dimensions: Dimensions,
        victoryCondition: VictoryCondition
    ): Level
    {
        val (width, height) = dimensions

        val sections = _generateSections(0, 0, width, height)
        val tiles = mutableMapOf<Coordinates, Tile>()
        val objects = mutableMapOf<Coordinates, MutableCollection<GameObject>>()

        for (room in sections.rooms)
        {
            for (coordinates in _getBorder(room))
            {
                tiles[coordinates] =
                    Tile(RPGTileType.STONE, coordinates)
                objects.computeIfAbsent(coordinates, { mutableListOf() }) += Wall()
            }
            for (coordinates in _getInterior(room))
            {
                tiles[coordinates] =
                    Tile(RPGTileType.STONE, coordinates)
            }
        }

        for (connection in sections.connections)
        {
            val connectionCoordinates = _getConnectionCoordinates(sections, connection)
            for (coordinates in connectionCoordinates)
            {
                tiles[coordinates] =
                    Tile(RPGTileType.GRASS, coordinates)
                objects.remove(coordinates)
            }
        }

        val units = listOf<Level.UnitData>()
        val startPosition = Coordinates(0, 0)

        return Level(
            tiles = tiles,
            units = units,
            objects = objects,
            startPosition = startPosition,
            victoryCondition = victoryCondition
        )
    }

    private fun _getConnectionCoordinates(sections: MapSections, connection: Connection): Set<Coordinates>
    {
        val coordinatesSet = mutableSetOf<Coordinates>()

        val dx = (connection.max.x - connection.min.x).sign
        val dy = (connection.max.y - connection.min.y).sign
        val orientation = Orientation.from(dx, dy)

        var midCoordinates = connection.min
        while (true)
        {
            val (x, y) = midCoordinates
            val firstSection = sections.sections.find {
                when (orientation)
                {
                    Orientation.HORIZONTAL ->
                        it.left + it.width == x &&
                            it.top <= y &&
                            it.top + it.height - 1 >= y
                    Orientation.VERTICAL ->
                        it.top + it.height == y &&
                            it.left <= x &&
                            it.left + it.width - 1 >= x
                }
            }
            val secondSection = sections.sections.find {
                when (orientation)
                {
                    Orientation.HORIZONTAL ->
                        it.left == x &&
                            it.top <= y &&
                            it.top + it.height - 1 >= y
                    Orientation.VERTICAL ->
                        it.top == y &&
                            it.left <= x &&
                            it.left + it.width - 1 >= x
                }
            }
            if (firstSection != null && secondSection != null)
            {
                val firstRoom = sections.rooms.find {
                    it.left == firstSection.left + 1 && it.top == firstSection.top + 1
                }!!
                val secondRoom = sections.rooms.find {
                    it.left == secondSection.left + 1 && it.top == secondSection.top + 1
                }!!

                val firstCoordinates = when (orientation)
                {
                    Orientation.HORIZONTAL -> Coordinates(
                        firstRoom.right,
                        y
                    )
                    Orientation.VERTICAL -> Coordinates(
                        x,
                        firstRoom.bottom
                    )
                }
                val secondCoordinates = when (orientation)
                {
                    Orientation.HORIZONTAL -> Coordinates(
                        secondRoom.left,
                        y
                    )
                    Orientation.VERTICAL -> Coordinates(
                        x,
                        secondRoom.top
                    )
                }
                var currentCoordinates = firstCoordinates
                val dx2 = (secondCoordinates.x - firstCoordinates.x).sign
                val dy2 = (secondCoordinates.y - firstCoordinates.y).sign

                while (true)
                {
                    coordinatesSet += currentCoordinates

                    if (currentCoordinates == secondCoordinates)
                    {
                        break
                    }
                    currentCoordinates += IntPair.of(dx2, dy2)
                }
                return coordinatesSet
            }

            if (midCoordinates == connection.max)
            {
                throw RuntimeException()
            }
            midCoordinates += IntPair.of(dx, dy)
        }
    }

    private fun _generateSections(left: Int, top: Int, width: Int, height: Int): MapSections
    {
        println("generating ${left} ${top} ${width} ${height}")
        if (width == 0 || height == 0)
        {
            error("Fuck")
        }
        require(width >= MIN_SECTION_WIDTH)
        require(height >= MIN_SECTION_HEIGHT)
        val splitDirection: Orientation? = _getSplitDirection(width, height)

        when (splitDirection)
        {
            Orientation.HORIZONTAL ->
            {
                val splitPoint = _getSplitPointX(left, width)
                val connectionPoint = _getConnectionPointX(top, height)

                val leftSections = _generateSections(left, top, splitPoint - left, height)
                val rightSections = _generateSections(splitPoint, top, left + width - splitPoint, height)
                val connections =
                    leftSections.connections +
                    rightSections.connections +
                    Connection(
                        Coordinates(splitPoint - 1, connectionPoint),
                        Coordinates(splitPoint + 1, connectionPoint)
                    )

                return MapSections(
                    leftSections.sections + rightSections.sections,
                    leftSections.rooms + rightSections.rooms,
                    connections
                )
            }
            Orientation.VERTICAL ->
            {
                val splitPoint = _getSplitPointY(top, height)
                val connectionPoint = _getConnectionPointY(left, width)

                val topSections = _generateSections(left, top, width, splitPoint - top)
                val bottomSections = _generateSections(left, splitPoint, width, top + height - splitPoint)
                val connections =
                    topSections.connections +
                    bottomSections.connections +
                    Connection(
                        Coordinates(connectionPoint, splitPoint - 1),
                        Coordinates(connectionPoint, splitPoint + 1)
                    )

                return MapSections(
                    topSections.sections + bottomSections.sections,
                    topSections.rooms + bottomSections.rooms,
                    connections
                )
            }
            null ->
            {
                // Base case: a single section with a room and no connections
                return MapSections(
                    setOf(Rectangle(left, top, width, height)),
                    setOf(
                        Rectangle(
                            left + SECTION_ROOM_PADDING_X,
                            top + SECTION_ROOM_PADDING_Y,
                            width - 2 * SECTION_ROOM_PADDING_X,
                            height - 2 * SECTION_ROOM_PADDING_Y
                        )
                    ),
                    setOf()
                )
            }
        }
    }

    private fun _getConnectionPointX(top: Int, height: Int): Int
    {
        return ((top + 1)..(top + height - 2)).random()
    }

    private fun _getConnectionPointY(left: Int, width: Int): Int
    {
        return ((left + 1)..(left + width - 2)).random()
    }

    private fun _getSplitDirection(width: Int, height: Int): Orientation?
    {
        val orientations = mutableListOf<Orientation>()

        if (width >= MIN_SECTION_WIDTH * 2)
        {
            orientations += Orientation.HORIZONTAL
        }

        if (height >= MIN_SECTION_HEIGHT * 2)
        {
            orientations += Orientation.VERTICAL
        }

        if (orientations.isNotEmpty())
        {
            return orientations.random()
        }

        return null
    }

    private fun _getSplitPointX(left: Int, width: Int): Int
    {
        val min = left + MIN_SECTION_WIDTH
        val max = left + width - MIN_SECTION_WIDTH
        return (min..max).random()
    }

    private fun _getSplitPointY(top: Int, height: Int): Int
    {
        val min = top + MIN_SECTION_HEIGHT
        val max = top + height - MIN_SECTION_HEIGHT
        return (min..max).random()
    }

    private fun _getBorder(room: Rectangle): Set<Coordinates>
    {
        val (left, top) = room
        // Ugh, Kotlin destructuring sucks
        val right = room.right
        val bottom = room.bottom

        val coordinates = mutableSetOf<Coordinates>()
        for (x in left..right)
        {
            coordinates += Coordinates(x, top)
            coordinates += Coordinates(x, bottom)
        }
        for (y in top..bottom)
        {
            coordinates += Coordinates(left, y)
            coordinates += Coordinates(right, y)
        }
        return coordinates
    }

    private fun _getInterior(room: Rectangle): Set<Coordinates>
    {
        val (left, top) = room
        // Ugh, Kotlin destructuring sucks
        val right = room.right
        val bottom = room.bottom

        val coordinates = mutableSetOf<Coordinates>()
        for (y in (top + 1)..(bottom - 1))
        {
            for (x in (left + 1)..(right - 1))
            {
                coordinates += Coordinates(x, y)
            }
        }
        return coordinates
    }
}