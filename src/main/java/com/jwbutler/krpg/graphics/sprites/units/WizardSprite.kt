package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.IntPair
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.PaletteSwaps
import java.awt.Color

private val offsets = PlayerSprite.OFFSETS + IntPair.of(0, 4)

class WizardSprite(paletteSwaps: PaletteSwaps) : UnitSprite("robed_wizard", paletteSwaps.withTransparentColor(Color.WHITE), offsets)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
    {
        return when (activity)
        {
            Activity.STANDING -> (1..4).flatMap { listOf(it, it) }.map { UnitFrame(activity, direction, it.toString()) }
            Activity.WALKING  -> listOf(1, 1, 1, 1).map { UnitFrame(activity, direction, it.toString()) }
            Activity.FALLING  -> (1..4).flatMap { listOf(it, it) }.map { UnitFrame("vanishing", Direction.SE, it.toString()) }
            Activity.DEAD  -> (1..4).flatMap { listOf(it, it) }.map { UnitFrame(Activity.DEAD, Direction.SE, "1") }
            else              -> error("Invalid activity ${activity}")
        }
    }

    override fun _formatFilename(frame: UnitFrame): String
    {
        return when (frame.activity)
        {
            Activity.STANDING -> String.format(
                "units/%s/%s_%s_%s",
                spriteName,
                spriteName,
                frame.direction,
                frame.key
            )
            Activity.DEAD -> String.format(
                "units/%s/%s_%s",
                spriteName,
                spriteName,
                frame.activity
            )
            else -> String.format(
                "units/%s/%s_%s_%s_%s",
                spriteName,
                spriteName,
                frame.activity,
                frame.direction,
                frame.key
            )
        }
    }
}