package com.jwbutler.rpglib.levels

import com.jwbutler.rpglib.entities.tiles.Tile
import com.jwbutler.rpglib.entities.equipment.Equipment
import com.jwbutler.rpglib.entities.equipment.EquipmentSlot
import com.jwbutler.rpglib.entities.objects.GameObject
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.players.Player

/**
 * Levels *WILL BE* mutable - they'll save their state when you exit them.
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
    var forceVictory = false // hack for skip level cheat

    data class UnitData
    (
        val unit: Unit,
        val coordinates: Coordinates,
        val player: Player,
        val equipment: Map<EquipmentSlot, Equipment>
    )

    fun checkVictory() = forceVictory || victoryCondition.predicate()
    fun doVictory() = victoryCondition.onComplete()
}