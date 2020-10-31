package com.jwbutler.rpglib.core

import com.jwbutler.rpglib.levels.Level
import com.jwbutler.krpg.players.HumanPlayer
import com.jwbutler.krpg.players.Player
import com.jwbutler.rpglib.entities.Entity
import com.jwbutler.rpglib.entities.equipment.Equipment
import com.jwbutler.rpglib.entities.equipment.EquipmentSlot
import com.jwbutler.rpglib.entities.objects.GameObject
import com.jwbutler.rpglib.entities.tiles.Tile
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.hypotenuse
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

internal class GameStateImpl : GameState
{
    override var ticks = 0
    private var level: Level? = null

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
    override fun getUnit(equipment: Equipment): Unit? = unitToEquipment.entries
        .find { (unit, slotToEquipment) -> slotToEquipment.values.contains(equipment) }
        ?.key

    override fun addUnit(unit: Unit, coordinates: Coordinates, player: Player)
    {
        check(coordinatesToTile[coordinates] != null) { "Can't add unit, no tile at ${coordinates}" }
        check(!isBlocked(coordinates))
        check(coordinatesToUnit[coordinates] == null) { "Can't add another unit at ${coordinates}" }
        entityToCoordinates[unit] = coordinates
        coordinatesToUnit[coordinates] = unit
        if (!(playerToUnits.getOrDefault(player, listOf<Unit>()).contains(unit)))
        {
            playerToUnits[player]!! += unit
        }
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
        val slotToEquipment = unitToEquipment.computeIfAbsent(unit) { mutableMapOf() }
        check(slotToEquipment[equipment.slot] == null)
        slotToEquipment[equipment.slot] = equipment
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

    /**
     * TODO: Migrate some of this to GameEngine
     */
    override fun loadLevel(level: Level)
    {
        val playerUnits = coordinatesToUnit.values.filter { it.getPlayer().isHuman }
        val playerUnitEquipment = unitToEquipment.entries
            .filter { (unit, _) -> playerUnits.contains(unit) }
            .map { it.key to it.value }
            .toMap()

        entityToCoordinates.clear()
        coordinatesToTile.clear()
        coordinatesToObjects.clear()
        coordinatesToUnit.clear()
        unitToEquipment.clear()
        playerToUnits.values.forEach { it.clear() }

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
                unit.addEquipment(equipment)
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
                playerUnitEquipment.getOrDefault(unit, emptyMap())
            )
        }

        this.level = level
    }

    override fun getLevel(): Level = level!!

    override fun addPlayerUnit(unit: Unit, player: Player, coordinates: Coordinates, equipmentMap: Map<EquipmentSlot, Equipment>)
    {
        val candidateCoordinates = GameState.getInstance()
            .getAllCoordinates()
            .filter { !it.isBlocked() }
            .sortedBy { hypotenuse(it, coordinates) }

        val targetCoordinates = candidateCoordinates.getOrNull(0) ?: throw IllegalStateException(
            "Couldn't place units"
        )
        addUnit(unit, targetCoordinates, player)

        for ((slot, equipment) in equipmentMap)
        {
            unit.addEquipment(equipment)
        }
    }
}