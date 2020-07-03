package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.Frame
import com.jwbutler.krpg.graphics.PaletteSwaps

class PlayerSprite(paletteSwaps: PaletteSwaps) : UnitSprite("spriteName", paletteSwaps)
{
    override fun getFrame(activity: Activity, direction: Direction, frameNumber: Int): Frame
    {
        when (activity)
        {
            // TODO  these are all made up
            Activity.STANDING -> (1..4).map { Frame(activity, direction, it.toString()) }
            Activity.WALKING -> arrayOf(1, 1, 2, 2).map { Frame(activity, direction, it.toString()) }
            Activity.ATTACKING -> arrayOf(1, 1, 2, 2).map { Frame(activity, direction, it.toString()) }
            Activity.FALLING -> arrayOf(1, 1, 2, 2).map { Frame(activity, direction, it.toString()) }
        }
        error("Invalid activity ${activity}")
    }
}