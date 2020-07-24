package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.entities.equipment.EquipmentSlot
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.units.UnitSprite
import com.jwbutler.krpg.players.Player

interface Unit : Entity
{
    override val sprite: UnitSprite
    fun getPlayer(): Player
    fun getCommand(): Command
    fun getActivity(): Activity
    fun getDirection(): Direction
    /**
     * This is a numerical frame number corresponding to the position in the list,
     * not including stuff like "2b"
     */
    fun getFrameNumber(): Int
    fun getEquipment(): Map<EquipmentSlot, Equipment>

    fun getCurrentHP(): Int
    fun getMaxHP(): Int

    fun getCooldown(activity: Activity): Int // maybe move this to AbstractUnit?
    fun getRemainingCooldown(activity: Activity): Int
    fun triggerCooldown(activity: Activity)

    fun moveTo(coordinates: Coordinates)
    fun getDamage(activity: Activity): Int
    fun takeDamage(amount: Int)
    fun setCommand(command: Command)
    fun setActivity(activity: Activity, direction: Direction)
    fun removeEquipment(equipment: Equipment)
    fun addEquipment(equipment: Equipment)
    fun die()
}