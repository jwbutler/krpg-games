package com.jwbutler.krpg.entities

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.Command
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.Coordinates

interface Unit : Entity
{
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