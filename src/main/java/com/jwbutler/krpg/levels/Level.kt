package com.jwbutler.krpg.levels

import com.jwbutler.krpg.entities.tiles.Tile
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.entities.objects.GameObject
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.players.Player

/**
 * Levels are mutable - they save their state when you exit them.
 */
data class Level
(
    val tiles: Map<Coordinates, Tile>,
    val units: Collection<UnitData>,
    val objects: Map<Coordinates, Collection<GameObject>>,
    val startPosition: Coordinates,
    private val victoryCondition: VictoryCondition
)
{
    var forceComplete = false // hack for skip level cheat

    data class UnitData
    (
        val unit: Unit,
        val coordinates: Coordinates,
        val player: Player,
        val equipment: Map<EquipmentSlot, Equipment>
    )

    fun isComplete() = forceComplete || victoryCondition.predicate()
    fun onComplete() = victoryCondition.onComplete()
}