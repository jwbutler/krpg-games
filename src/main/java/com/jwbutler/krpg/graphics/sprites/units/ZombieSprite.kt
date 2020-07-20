package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.UnitFrame
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
    override fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
    {
        return when (activity)
        {
            Activity.STANDING -> listOf(UnitFrame(activity, direction, "1"))
            Activity.WALKING -> arrayOf(2, 2, 1).map { UnitFrame(activity, direction, it.toString()) }
            Activity.ATTACKING ->
            {
                val frames = mutableListOf<UnitFrame>()
                frames.addAll(arrayOf(1, 2, 2, 1).map { UnitFrame(Activity.ATTACKING, direction, it.toString()) })
                frames.addAll(arrayOf(1, 1).map { UnitFrame(Activity.STANDING, direction, it.toString()) })
                return frames
            }
            Activity.FALLING -> arrayOf(1, 1, 2, 2, 3, 3, 3, 3).map {
                val fallingDirection = _getFallingDirection(direction)
                UnitFrame(
                    activity,
                    fallingDirection,
                    it.toString()
                )
            }
            Activity.DEAD -> (1..8).map { UnitFrame(Activity.FALLING, _getFallingDirection(direction), "3") }
            else -> error("Invalid activity ${activity}")
        }
    }

    override fun _formatFilename(frame: UnitFrame): String
    {
        if (frame.activity == Activity.FALLING)
        {
            if (frame.direction == Direction.NE)
            {
                return String.format(
                    "units/%s/%s_%s_%s", // zombie_falling_{1,2,3}.png
                    spriteName,
                    spriteName,
                    frame.activity,
                    frame.key
                )
            }
            else
            {
                return String.format(
                    "units/%s/%s_%sB_%s", // zombie_fallingB_{1,2,3}.png
                    spriteName,
                    spriteName,
                    frame.activity,
                    frame.key
                )
            }
        }
        else
        {
            // standard formatter
            return String.format(
                "units/%s/%s_%s_%s_%s",
                spriteName,
                spriteName,
                frame.activity,
                frame.direction,
                frame.key
            )
        }
    }

    private fun _getFallingDirection(direction: Direction): Direction
    {
        return when (direction)
        {
            Direction.N, Direction.NE, Direction.E, Direction.SE -> Direction.NE
            else -> Direction.S
        }
    }

    companion object
    {
        val OFFSETS = PlayerSprite.OFFSETS
    }
}