package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.Unit
import com.jwbutler.krpg.geometry.Coordinates
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * Note: In general it's better not to call these methods directly.
 * For example, [Unit] has methods that wrap around [addUnit] / [removeUnit]
 */
interface GameState
{
    fun getCoordinates(entity: Entity): Coordinates
    fun getUnit(coordinates: Coordinates): Unit?
    fun addUnit(unit: Unit, coordinates: Coordinates)
    fun removeUnit(unit: Unit)

    companion object
    {
        private var INSTANCE = GameStateImpl()
        fun getInstance(): GameState = INSTANCE

        fun initializeForTests()
        {
            INSTANCE = GameStateImpl()
        }
    }
}

private class GameStateImpl : GameState
{
    private val entityToCoordinates: MutableMap<Entity, Coordinates> = mutableMapOf()
    private val coordinatesToUnit: MutableMap<Coordinates, Unit?> = mutableMapOf()

    override fun getCoordinates(entity: Entity) = entityToCoordinates[entity] ?: throw IllegalStateException()
    override fun getUnit(coordinates: Coordinates): Unit? = coordinatesToUnit[coordinates]

    override fun addUnit(unit: Unit, coordinates: Coordinates)
    {
        require(coordinatesToUnit[coordinates] == null)
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
}