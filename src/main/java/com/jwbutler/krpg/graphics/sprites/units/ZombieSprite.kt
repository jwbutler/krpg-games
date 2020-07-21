package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.PaletteSwaps
import java.awt.Color

class ZombieSprite(paletteSwaps: PaletteSwaps) : UnitSprite
(
    "zombie",
    paletteSwaps.withTransparentColor(Color.WHITE),
    OFFSETS
)
{
    /**
     * Tons of copy-paste from [PlayerSprite.getFrames], but the falling animation is different
     * and it seems cleaner to just rewrite the whole function
     */
    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
    {
        return when (activity)
        {
            Activity.STANDING -> listOf(FrameKey(activity, direction, "1"))
            Activity.WALKING -> arrayOf(2, 2, 1).map { FrameKey(activity, direction, it.toString()) }
            Activity.ATTACKING ->
            {
                val frames = mutableListOf<FrameKey>()
                frames.addAll(arrayOf(1, 2, 2, 1).map { FrameKey(Activity.ATTACKING, direction, it.toString()) })
                frames.addAll(arrayOf(1, 1).map { FrameKey(Activity.STANDING, direction, it.toString()) })
                return frames
            }
            Activity.FALLING -> arrayOf(1, 1, 2, 2, 3, 3, 3, 3).map {
                val fallingActivity = _getFallingActivity(direction)
                FrameKey(fallingActivity, it)
            }
            Activity.DEAD -> (1..8).map { FrameKey(_getFallingActivity(direction), 3) }
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

    companion object
    {
        val OFFSETS = PlayerSprite.OFFSETS
    }
}