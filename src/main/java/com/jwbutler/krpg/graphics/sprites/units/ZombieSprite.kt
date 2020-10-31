package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.graphics.FrameKey
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.krpg.utils.SpriteUtils
import com.jwbutler.rpglib.graphics.sprites.UnitSprite

private val OFFSETS = PlayerSprite.OFFSETS

class ZombieSprite(paletteSwaps: PaletteSwaps) : UnitSprite
(
    "zombie",
    paletteSwaps.withTransparentColor(Colors.WHITE)
)
{
    override val offsets = OFFSETS
    /**
     * Tons of copy-paste from [SpriteUtils.getPlayerFrames], but the falling animation is different
     * and it seems cleaner to just rewrite the whole function
     */
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return when (activity)
        {
            RPGActivity.STANDING -> listOf(
                FrameKey(activity, direction, "1")
            )
            RPGActivity.WALKING -> arrayOf(2, 2, 1)
                .map { FrameKey(activity, direction, it.toString()) }
            RPGActivity.ATTACKING ->
            {
                val frames = mutableListOf<FrameKey>()

                frames.addAll(arrayOf(1, 2, 2, 1)
                    .map { FrameKey(RPGActivity.ATTACKING, direction, it.toString()) })

                frames.addAll(arrayOf(1, 1)
                    .map { FrameKey( RPGActivity.STANDING, direction, it.toString()) })
                return frames
            }
            RPGActivity.FALLING -> arrayOf(1, 1, 2, 2, 3, 3, 3, 3)
                .map { FrameKey(
                    _getFallingActivity(direction),
                    it
                ) }
            RPGActivity.DEAD -> (1..8)
                .map { FrameKey(_getFallingActivity(direction), 3) }
            else -> error("Invalid activity ${activity}")
        }
    }

    private fun _getFallingActivity(direction: Direction): String
    {
        return when (direction)
        {
            Direction.N, Direction.NE, Direction.E, Direction.SE -> "falling"
            else -> "fallingB"
        }
    }
}