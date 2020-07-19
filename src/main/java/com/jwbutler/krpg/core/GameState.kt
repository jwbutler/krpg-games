package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * Note: In general it's better not to call these methods directly.
 * For example, [Unit] has methods that wrap around [addUnit] / [removeUnit]
 */
interface GameState
{
    var ticks: Int
    fun getCoordinates(entity: Entity): Coordinates
    fun getUnit(coordinates: Coordinates): Unit?
    fun setTiles(tiles: Map<Coordinates, Tile?>)
    fun addUnit(unit: Unit, coordinates: Coordinates)
    fun removeUnit(unit: Unit)

    /**
     * Returns entities in *update* order, which may not be the same as render order
     */
    fun getEntities(): List<Entity>

    fun containsCoordinates(coordinates: Coordinates): Boolean

    companion object
    {
        private var INSTANCE: GameState? = null

        fun getInstance(): GameState
        {
            return INSTANCE ?: throw IllegalStateException()
        }

        fun initialize(): GameState
        {
            INSTANCE = GameStateImpl()
            return INSTANCE!!
        }
    }
}

private class GameStateImpl : GameState
{
    override var ticks = 0
    private val entityToCoordinates: MutableMap<Entity, Coordinates> = mutableMapOf()
    private val coordinatesToUnit: MutableMap<Coordinates, Unit?> = mutableMapOf()
    private val coordinatesToTile: MutableMap<Coordinates, Tile?> = mutableMapOf()

    override fun getCoordinates(entity: Entity) = entityToCoordinates[entity] ?: throw IllegalStateException()
    override fun getUnit(coordinates: Coordinates): Unit? = coordinatesToUnit[coordinates]

    override fun setTiles(tiles: Map<Coordinates, Tile?>)
    {
        coordinatesToTile.clear()
        coordinatesToTile.putAll(tiles)
        coordinatesToTile.forEach { coordinates, tile ->
            if (tile != null)
            {
                entityToCoordinates.put(tile, coordinates)
            }
        }
    }

    override fun addUnit(unit: Unit, coordinates: Coordinates)
    {
        check(coordinatesToTile[coordinates] != null) { "Can't add unit, no tile at ${coordinates}" }
        check(coordinatesToUnit[coordinates] == null) { "Can't add unit, another unit at ${coordinates}" }
        entityToCoordinates[unit] = coordinates
        coordinatesToUnit[coordinates] = unit
    }

    override fun removeUnit(unit: Unit)
    {
        val coordinates = entityToCoordinates[unit] ?: throw IllegalArgumentException()
        check(coordinatesToUnit[coordinates] == unit)
        entityToCoordinates.remove(unit)
        coordinatesToUnit.remove(coordinates)
    }

    override fun getEntities() = entityToCoordinates.keys.toList()
    override fun containsCoordinates(coordinates: Coordinates) = coordinatesToTile[coordinates] != null
}