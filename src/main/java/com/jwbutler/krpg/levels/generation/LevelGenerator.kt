package com.jwbutler.krpg.levels.generation

import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.objects.Wall
import com.jwbutler.krpg.entities.tiles.Tile
import com.jwbutler.krpg.entities.tiles.TileType
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Dimensions
import com.jwbutler.krpg.geometry.IntPair
import com.jwbutler.krpg.levels.Level
import com.jwbutler.krpg.levels.VictoryCondition
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

private const val MIN_SECTION_WIDTH = 5
private const val MIN_SECTION_HEIGHT = 5
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

private enum class Orientation
{
    HORIZONTAL,
    VERTICAL
}

private data class Connection
(
    val first: Coordinates,
    val second: Coordinates
)
{
    init
    {
        require(first != second)
    }

    override fun equals(other: Any?): Boolean
    {
        if (other is Connection)
        {
            return (first == other.first && second == other.second)
                || (first == other.second && second == other.first)
        }
        return false
    }

    override fun hashCode(): Int
    {
        return setOf(first, second).hashCode()
    }
}

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
                tiles[coordinates] = Tile(TileType.STONE, coordinates)
                objects.computeIfAbsent(coordinates, { mutableListOf() }) += Wall()
            }
            for (coordinates in _getInterior(room))
            {
                tiles[coordinates] = Tile(TileType.STONE, coordinates)
            }
        }

        for (connection in sections.connections)
        {
            val dx = (connection.second.x - connection.first.x).sign
            val dy = (connection.second.y - connection.first.y).sign

            var coordinates = connection.first
            while (coordinates != connection.second)
            {
                tiles[coordinates] = Tile(TileType.GRASS, coordinates)
                coordinates += IntPair.of(dx, dy)
            }
            tiles[connection.second] = Tile(TileType.GRASS, connection.second)
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

    private fun _generateSections(left: Int, top: Int, width: Int, height: Int): MapSections
    {
        require(width >= MIN_SECTION_WIDTH)
        require(height >= MIN_SECTION_HEIGHT)
        val splitDirection: Orientation? = _getSplitDirection(width, height)

        return when (splitDirection)
        {
            Orientation.HORIZONTAL ->
            {
                val splitPoint = _getSplitPointX(left, width)
                val connectionPoint = _getConnectionPointX(left, width)

                val leftSections = _generateSections(left, top, splitPoint - left, height)
                val rightSections = _generateSections(splitPoint, top, width - splitPoint, height)
                val connections = leftSections.connections +
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
                val connectionPoint = _getConnectionPointY(top, height)

                val topSections = _generateSections(left, top, width, splitPoint - top)
                val bottomSections = _generateSections(splitPoint, top, width, height - splitPoint)
                val connections = topSections.connections +
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
                MapSections(
                    setOf(Rectangle(left, top, width, height)),
                    setOf(Rectangle(
                        left + SECTION_ROOM_PADDING_X,
                        top + SECTION_ROOM_PADDING_Y,
                        width - 2 * SECTION_ROOM_PADDING_X,
                        height - 2 * SECTION_ROOM_PADDING_Y
                    )),
                    setOf()
                )
            }
        }
    }

    private fun _getConnectionPointX(top: Int, height: Int): Int = -1
    private fun _getConnectionPointY(left: Int, width: Int): Int = -1

    private fun _generateRooms(sections: Collection<Rectangle>): Set<Rectangle>
    {
        return sections.mapTo(mutableSetOf()) { (left, top, width, height) ->
            Rectangle(
                left + SECTION_ROOM_PADDING_X,
                top + SECTION_ROOM_PADDING_Y,
                width - 2 * SECTION_ROOM_PADDING_X,
                height - 2 * SECTION_ROOM_PADDING_Y
            )
        }
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

    /**
     * TODO: check for off by 1
     */
    private fun _getSplitPointX(left: Int, width: Int): Int
    {
        val min = min(left + MIN_SECTION_WIDTH, width - MIN_SECTION_WIDTH)
        val max = max(left + MIN_SECTION_WIDTH, width - MIN_SECTION_WIDTH)
        return (min..max).random()
    }

    /**
     * TODO: check for off by 1
     */
    private fun _getSplitPointY(top: Int, height: Int): Int
    {
        val min = min(top + MIN_SECTION_HEIGHT, height - MIN_SECTION_HEIGHT)
        val max = max(top + MIN_SECTION_HEIGHT, height - MIN_SECTION_HEIGHT)
        return (min..max).random()
    }

    private fun _getBorder(room: Rectangle): Set<Coordinates>
    {
        val (left, top, width, height) = room
        val right = left + width
        val bottom = top + height

        val coordinates = mutableSetOf<Coordinates>()
        for (x in left..right)
        {
            coordinates += Coordinates(x, top)
            coordinates += Coordinates(x, bottom)
        }
        for (y in top..bottom)
        {
            coordinates += Coordinates(left, y)
            coordinates += Coordinates(left, y)
        }
        return coordinates
    }

    private fun _getInterior(room: Rectangle): Set<Coordinates>
    {
        val (left, top, width, height) = room
        val right = left + width
        val bottom = top + height

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