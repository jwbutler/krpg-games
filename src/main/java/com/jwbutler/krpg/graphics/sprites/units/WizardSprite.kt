package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.IntPair
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.PaletteSwaps
import java.awt.Color

private val offsets = PlayerSprite.OFFSETS + IntPair.of(0, 4)

class WizardSprite(paletteSwaps: PaletteSwaps) : UnitSprite("robed_wizard", paletteSwaps.withTransparentColor(Color.WHITE), offsets)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return when (activity)
        {
            Activity.STANDING -> (1..4).flatMap { listOf(it, it) }.map { FrameKey(direction, it) }
            Activity.WALKING  -> listOf(1, 1, 1, 1).map { FrameKey(activity, direction, it) }
            Activity.FALLING  -> (1..4).flatMap { listOf(it, it) }.map { FrameKey("vanishing", Direction.SE, it) }
            Activity.DEAD  -> (1..4).flatMap { listOf(it, it) }.map { FrameKey(Activity.DEAD) }
            else              -> error("Invalid activity ${activity}")
        }
    }
}