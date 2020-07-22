package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.IntPair
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.PaletteSwaps
import java.awt.Color
import java.lang.RuntimeException

private val offsets = PlayerSprite.OFFSETS + IntPair.of(0, 4)

class WizardSprite(paletteSwaps: PaletteSwaps) : UnitSprite("robed_wizard", paletteSwaps.withTransparentColor(Color.WHITE), offsets)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return when (activity)
        {
            Activity.STANDING     -> (1..4).flatMap { listOf(it, it) }.map { FrameKey(direction, it) }
            Activity.WALKING      -> listOf(1, 1, 1, 1).map { FrameKey(activity, direction, it) }
            Activity.FALLING      -> (1..4).flatMap { listOf(it, it) }.map { FrameKey("vanishing", Direction.SE, it) }
            Activity.DEAD         -> (1..4).flatMap { listOf(it, it) }.map { FrameKey(Activity.DEAD) }
            Activity.RESURRECTING -> (1..8).flatMap { listOf(it, it, it, it) }.map { FrameKey("casting", "SE", it) }
            else                  -> throw RuntimeException("Unknown activity ${activity}")
        }
    }
}