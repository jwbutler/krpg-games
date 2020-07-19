package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.PaletteSwaps

class WizardSprite(paletteSwaps: PaletteSwaps) : UnitSprite("spriteName", paletteSwaps)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
    {
        return when (activity)
        {
            // TODO  these are all made up
            Activity.STANDING -> (1..4).map { UnitFrame(activity, direction, it.toString()) }
            Activity.WALKING  -> arrayOf(1, 1, 2, 2).map { UnitFrame(activity, direction, it.toString()) }
            Activity.FALLING  -> arrayOf(1, 1, 2, 2).map { UnitFrame(activity, direction, it.toString()) }
            else              -> error("Invalid activity ${activity}")
        }
    }
}