package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.geometry.IntPair
import com.jwbutler.rpglib.graphics.FrameKey
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.rpglib.graphics.sprites.UnitSprite
import java.lang.IllegalArgumentException

private val OFFSETS = PlayerSprite.OFFSETS + IntPair.of(0, 4)

class WizardSprite(paletteSwaps: PaletteSwaps) : UnitSprite
(
    "robed_wizard",
    paletteSwaps.withTransparentColor(Colors.WHITE)
)
{
    override val offsets = OFFSETS

    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return when (activity)
        {
            RPGActivity.STANDING -> (1..4)
                .flatMap { listOf(it, it) }
                .map { FrameKey(direction, it) }

            RPGActivity.WALKING -> listOf(1, 1, 1, 1)
                .map { FrameKey(activity, direction, it) }

            RPGActivity.FALLING -> (1..4)
                .flatMap { listOf(it, it) }
                .map {
                FrameKey("vanishing", Direction.SE, it) }

            RPGActivity.DEAD -> (1..4)
                .flatMap { listOf(it, it) }
                .map { FrameKey(RPGActivity.DEAD) }

            RPGActivity.RESURRECTING -> (1..8)
                .flatMap { listOf(it, it, it, it) }
                .map { FrameKey("casting", "SE", it) }

            else -> throw IllegalArgumentException("Unknown activity ${activity}")
        }
    }
}