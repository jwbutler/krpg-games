package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.players.HumanPlayer
import com.jwbutler.krpg.players.Player
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * Note: In general it's better not to call these methods directly.
 * For example, [Unit] has methods that wrap around [addUnit] / [removeUnit]
 */
interface GameState
{
    // Global stuff
    var ticks: Int

    // UI stuff
    fun getCameraCoordinates(): Coordinates

    // Coordinates

    fun getAllCoordinates(): Collection<Coordinates>
    fun containsCoordinates(coordinates: Coordinates): Boolean
    fun isBlocked(coordinates: Coordinates): Boolean

    // Tiles

    fun setTiles(tiles: Map<Coordinates, Tile?>)

    // Players

    fun getPlayers(): Collection<Player>
    fun addPlayer(player: Player)

    // Entities

    fun getCoordinates(entity: Entity): Coordinates
    /**
     * Returns entities in *update* order, which may not be the same as render order
     */
    fun getEntities(): List<Entity>
    fun containsEntity(entity: Entity): Boolean

    // Units

    fun getUnit(coordinates: Coordinates): Unit?
    fun addUnit(unit: Unit, coordinates: Coordinates)
    fun removeUnit(unit: Unit)
    fun moveUnit(unit: Unit, coordinates: Coordinates)

    // Objects

    fun getObjects(coordinates: Coordinates): Collection<GameObject>
    fun addObject(`object`: GameObject, coordinates: Coordinates)
    fun removeObject(`object`: GameObject)

    // Equipment
    // State is tracked here so that GameState can be a single source of truth for the list of entities.
    // Methods in [Unit] will delegate to these.

    fun addEquipment(equipment: Equipment, unit: Unit)
    fun removeEquipment(equipment: Equipment, unit: Unit)
    fun getEquipment(unit: Unit): Map<EquipmentSlot, Equipment>

    fun addEquipment(equipment: Equipment, coordinates: Coordinates)
    fun removeEquipment(equipment: Equipment)

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

    private val players: MutableSet<Player> = mutableSetOf()
    private val entityToCoordinates: MutableMap<Entity, Coordinates> = mutableMapOf()
    private val coordinatesToUnit: MutableMap<Coordinates, Unit?> = mutableMapOf()
    private val coordinatesToTile: MutableMap<Coordinates, Tile?> = mutableMapOf()
    private val coordinatesToObjects: MutableMap<Coordinates, MutableCollection<GameObject>> = mutableMapOf()
    private val unitToEquipment: MutableMap<Unit, MutableMap<EquipmentSlot, Equipment>> = mutableMapOf()

    override fun getCameraCoordinates() = _getPlayerUnit().getCoordinates()

    override fun getAllCoordinates(): Collection<Coordinates> = coordinatesToTile.keys

    override fun containsCoordinates(coordinates: Coordinates) = coordinatesToTile[coordinates] != null

    override fun isBlocked(coordinates: Coordinates): Boolean
    {
        return coordinatesToUnit[coordinates]?.isBlocking()
            ?: coordinatesToObjects[coordinates]?.any(Entity::isBlocking)
            ?: coordinatesToTile[coordinates]?.isBlocking()
            ?: false
    }

    override fun setTiles(tiles: Map<Coordinates, Tile?>)
    {
        coordinatesToTile.clear()
        coordinatesToTile.putAll(tiles)
        coordinatesToTile.forEach { (coordinates, tile) ->
            if (tile != null)
            {
                entityToCoordinates[tile] = coordinates
            }
        }
    }

    override fun getPlayers() = players.toList()
    override fun addPlayer(player: Player)
    {
        check(!players.contains(player))
        players.add(player)
    }

    override fun getCoordinates(entity: Entity) = entityToCoordinates[entity] ?: throw IllegalStateException()

    override fun getEntities(): List<Entity>
    {
        val entities = entityToCoordinates.keys.toMutableList()
        entities.addAll(unitToEquipment.values.flatMap { it.values })
        return entities
    }

    override fun containsEntity(entity: Entity) = entityToCoordinates.containsKey(entity)

    override fun getUnit(coordinates: Coordinates): Unit? = coordinatesToUnit[coordinates]

    override fun addUnit(unit: Unit, coordinates: Coordinates)
    {
        check(coordinatesToTile[coordinates] != null) { "Can't add unit, no tile at ${coordinates}" }
        check(!isBlocked(coordinates))
        check(coordinatesToUnit[coordinates] == null) { "Can't add another unit at ${coordinates}" }
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

    override fun moveUnit(unit: Unit, coordinates: Coordinates)
    {
        check(coordinatesToTile[coordinates] != null) { "Can't add unit, no tile at ${coordinates}" }
        check(coordinatesToUnit[coordinates] == null) { "Can't add another unit at ${coordinates}" }
        check(!isBlocked(coordinates))

        coordinatesToUnit.remove(unit.getCoordinates())
        entityToCoordinates.remove(unit)
        entityToCoordinates[unit] = coordinates
        coordinatesToUnit[coordinates] = unit
    }

    override fun getObjects(coordinates: Coordinates): Collection<GameObject> = coordinatesToObjects[coordinates] ?: listOf()
    override fun addObject(`object`: GameObject, coordinates: Coordinates)
    {
        coordinatesToObjects.computeIfAbsent(coordinates) { mutableListOf() }.add(`object`)
        entityToCoordinates[`object`] = coordinates
    }

    override fun removeObject(`object`: GameObject)
    {
        check(coordinatesToObjects[`object`.getCoordinates()] != null)
        check(coordinatesToObjects[`object`.getCoordinates()]!!.contains(`object`))
        check(entityToCoordinates[`object`] != null)
        coordinatesToObjects[`object`.getCoordinates()]!!.remove(`object`)
        entityToCoordinates.remove(`object`)
    }

    override fun addEquipment(equipment: Equipment, unit: Unit)
    {
        unitToEquipment.getOrPut(unit) { mutableMapOf() }
        check(unitToEquipment[unit]!![equipment.slot] == null)
        unitToEquipment[unit]!![equipment.slot] = equipment
        equipment.setUnit(unit)
    }

    override fun removeEquipment(equipment: Equipment, unit: Unit)
    {
        check(unitToEquipment[unit] != null)
        check(unitToEquipment[unit]!![equipment.slot] != null)
        unitToEquipment[unit]!!.remove(equipment.slot)
    }

    override fun getEquipment(unit: Unit) = unitToEquipment.getOrElse(unit) { mapOf<EquipmentSlot, Equipment>() }

    override fun addEquipment(equipment: Equipment, coordinates: Coordinates)
    {
        check(equipment.getUnit() == null)
        check(entityToCoordinates[equipment] == null)
        check(!(coordinatesToObjects[coordinates]?.contains(equipment) ?: true))
        entityToCoordinates[equipment] = coordinates
        coordinatesToObjects[coordinates]!!.add(equipment)
    }

    override fun removeEquipment(equipment: Equipment)
    {
        val coordinates = equipment.getCoordinates()
        check(equipment.getUnit() == null)
        check(entityToCoordinates[equipment] != null)
        check(coordinatesToObjects[coordinates]?.contains(equipment) ?: true)
        entityToCoordinates.remove(equipment)
        coordinatesToObjects[coordinates]!!.remove(equipment)
    }

    companion object
    {
        private fun _getPlayerUnit(): Unit
        {
            return (GameState.getInstance()
                .getPlayers()
                .find { it is HumanPlayer }
                ?.getUnits()
                ?.firstOrNull()
                ?: error("Could not find player unit"))
        }
    }
}