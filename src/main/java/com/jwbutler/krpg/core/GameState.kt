package com.jwbutler.krpg.core

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.levels.Level
import com.jwbutler.krpg.players.HumanPlayer
import com.jwbutler.krpg.players.Player
import com.jwbutler.krpg.utils.hypotenuse
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

/**
 * Note that `addX()` methods will add the entity to all corresponding maps - both internally and externally.
 * For example, `addUnit()` also links the unit to the corresponding player.
 */
interface GameState
{
    // Global stuff
    var ticks: Int

    // Coordinates

    fun getAllCoordinates(): Collection<Coordinates>
    fun containsCoordinates(coordinates: Coordinates): Boolean
    fun isBlocked(coordinates: Coordinates): Boolean

    // Tiles

    fun addTile(tile: Tile, coordinates: Coordinates)

    // Players

    fun getPlayers(): Collection<Player>
    fun getHumanPlayer(): HumanPlayer
    fun addPlayer(player: Player)
    fun getPlayer(unit: Unit): Player

    // Entities

    fun getCoordinates(entity: Entity): Coordinates
    /**
     * Returns entities in *update* order, which may not be the same as render order
     */
    fun getEntities(): List<Entity>
    fun containsEntity(entity: Entity): Boolean

    // Units

    fun getUnits(): Collection<Unit>
    fun getUnits(player: Player): List<Unit>
    fun getUnit(coordinates: Coordinates): Unit?
    fun addUnit(unit: Unit, coordinates: Coordinates, player: Player)
    fun removeUnit(unit: Unit)
    fun moveUnit(unit: Unit, coordinates: Coordinates)
    fun addPlayerUnit(unit: Unit, player: Player, coordinates: Coordinates, equipment: Map<EquipmentSlot, Equipment>)

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

    // Levels
    fun loadLevel(level: Level)

    companion object : SingletonHolder<GameState>(::GameStateImpl)
}

private class GameStateImpl : GameState
{
    override var ticks = 0

    private val playerToUnits = mutableMapOf<Player, MutableList<Unit>>()
    private val entityToCoordinates = mutableMapOf<Entity, Coordinates>()
    private val coordinatesToUnit = mutableMapOf<Coordinates, Unit>()
    private val coordinatesToTile = mutableMapOf<Coordinates, Tile>()
    private val coordinatesToObjects = mutableMapOf<Coordinates, MutableCollection<GameObject>>()
    private val unitToEquipment = mutableMapOf<Unit, MutableMap<EquipmentSlot, Equipment>>()

    override fun getAllCoordinates(): Collection<Coordinates> = coordinatesToTile.keys

    override fun containsCoordinates(coordinates: Coordinates) = coordinatesToTile[coordinates] != null

    override fun isBlocked(coordinates: Coordinates): Boolean
    {
        return coordinatesToUnit[coordinates]?.isBlocking()
            ?: coordinatesToObjects[coordinates]?.any(Entity::isBlocking)
            ?: coordinatesToTile[coordinates]?.isBlocking()
            ?: false
    }

    override fun addTile(tile: Tile, coordinates: Coordinates)
    {
        check(coordinatesToTile[coordinates] == null)
        check(entityToCoordinates[tile] == null)
        coordinatesToTile[coordinates] = tile
        entityToCoordinates[tile] = coordinates
    }

    override fun getPlayers() = playerToUnits.keys.toList()
    override fun getHumanPlayer(): HumanPlayer
    {
        return playerToUnits.keys
            .filterIsInstance<HumanPlayer>()
            .first()
    }

    override fun addPlayer(player: Player)
    {
        check(!playerToUnits.containsKey(player))
        playerToUnits[player] = mutableListOf()
    }

    override fun getPlayer(unit: Unit): Player
    {
        for ((player, units) in playerToUnits)
        {
            if (units.contains(unit))
            {
                return player
            }
        }
        throw IllegalStateException()
    }

    override fun getCoordinates(entity: Entity) = entityToCoordinates[entity] ?: throw IllegalStateException()

    override fun getEntities(): List<Entity>
    {
        val entities = entityToCoordinates.keys.toMutableList()
        entities.addAll(unitToEquipment.values.flatMap { it.values })
        return entities
    }

    override fun containsEntity(entity: Entity) = entityToCoordinates.containsKey(entity)

    override fun getUnits() = entityToCoordinates.keys.filterIsInstance<Unit>()
    override fun getUnits(player: Player) = playerToUnits[player]!!

    override fun getUnit(coordinates: Coordinates): Unit? = coordinatesToUnit[coordinates]

    override fun addUnit(unit: Unit, coordinates: Coordinates, player: Player)
    {
        check(coordinatesToTile[coordinates] != null) { "Can't add unit, no tile at ${coordinates}" }
        check(!isBlocked(coordinates))
        check(coordinatesToUnit[coordinates] == null) { "Can't add another unit at ${coordinates}" }
        check(!playerToUnits[player]!!.contains(unit))
        entityToCoordinates[unit] = coordinates
        coordinatesToUnit[coordinates] = unit
        playerToUnits[player]!! += unit
    }

    override fun removeUnit(unit: Unit)
    {
        val coordinates = entityToCoordinates[unit] ?: throw IllegalArgumentException()
        val player = getPlayer(unit)
        check(coordinatesToUnit[coordinates] == unit)
        entityToCoordinates.remove(unit)
        coordinatesToUnit.remove(coordinates)
        playerToUnits[player]!!.remove(unit)
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
        check(!(coordinatesToObjects[coordinates]?.contains(equipment) ?: false))
        entityToCoordinates[equipment] = coordinates
        coordinatesToObjects.computeIfAbsent(coordinates) { mutableListOf() }
            .add(equipment)
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

    override fun loadLevel(level: Level)
    {
        val playerUnits = coordinatesToUnit.values.filter { it.getPlayer().isHuman }
        val playerUnitEquipment = unitToEquipment.entries
            .filter { (unit, _) -> playerUnits.contains(unit) }

        entityToCoordinates.clear()
        coordinatesToTile.clear()
        coordinatesToObjects.clear()
        coordinatesToUnit.clear()

        for ((coordinates, tile) in level.tiles)
        {
            addTile(tile, coordinates)
        }

        for (unitData in level.units)
        {
            val (unit, coordinates, player, equipmentMap) = unitData
            addUnit(unit, coordinates, player)
            for ((slot, equipment) in equipmentMap)
            {
                addEquipment(equipment, unit)
            }
        }

        for ((coordinates, objects) in level.objects)
        {
            for (`object` in objects)
            {
                addObject(`object`, coordinates)
            }
        }

        for (unit in playerUnits)
        {
            addPlayerUnit(
                unit,
                getHumanPlayer(),
                level.startPosition,
                unitToEquipment.getOrDefault(unit, mapOf())
            )
        }
    }

    override fun addPlayerUnit(unit: Unit, player: Player, coordinates: Coordinates, equipmentMap: Map<EquipmentSlot, Equipment>)
    {
        val candidateCoordinates = GameState.getInstance()
            .getAllCoordinates()
            .filter { !it.isBlocked() }
            .sortedBy { hypotenuse(it, coordinates) }

        val targetCoordinates = candidateCoordinates.getOrNull(0) ?: throw IllegalStateException("Couldn't place units")
        addUnit(unit, targetCoordinates, player)

        for ((slot, equipment) in equipmentMap)
        {
            addEquipment(equipment, unit)
        }
    }
}