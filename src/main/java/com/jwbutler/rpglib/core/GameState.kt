package com.jwbutler.rpglib.core

import com.jwbutler.rpglib.entities.Entity
import com.jwbutler.rpglib.entities.tiles.Tile
import com.jwbutler.rpglib.entities.equipment.Equipment
import com.jwbutler.rpglib.entities.equipment.EquipmentSlot
import com.jwbutler.rpglib.entities.objects.GameObject
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.levels.Level
import com.jwbutler.krpg.players.Player

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
    fun getHumanPlayer(): Player
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
    fun getUnit(equipment: Equipment): Unit?
    fun addUnit(unit: Unit, coordinates: Coordinates, player: Player)
    fun removeUnit(unit: Unit)
    fun moveUnit(unit: Unit, coordinates: Coordinates)
    fun addPlayerUnit(unit: Unit, player: Player, coordinates: Coordinates, equipmentMap: Map<EquipmentSlot, Equipment>)

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
    fun getLevel(): Level

    companion object : BoundSingletonHolder<GameState>(::GameStateImpl)
}
