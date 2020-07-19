package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.players.Player

interface Unit : Entity
{
    fun getPlayer(): Player
    fun getCommand(): Command
    fun getActivity(): Activity
    fun getDirection(): Direction
    /**
     * This is a numerical frame number corresponding to the position in the list,
     * not including stuff like "2b"
     */
    fun getFrameNumber(): Int

    fun getCurrentHP(): Int
    fun getMaxHP(): Int

    fun moveTo(coordinates: Coordinates)
    fun takeDamage(amount: Int)
    fun setCommand(command: Command)
    fun die()
}