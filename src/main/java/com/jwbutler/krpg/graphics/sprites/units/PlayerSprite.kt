package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.geometry.TILE_HEIGHT
import com.jwbutler.krpg.geometry.TILE_WIDTH
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.PaletteSwaps
import java.awt.Color

class PlayerSprite(paletteSwaps: PaletteSwaps) : UnitSprite(
    "player",
    paletteSwaps.withTransparentColor(Color.WHITE),
    OFFSETS
)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
    {
        return getFrames(activity, direction)
    }

    companion object
    {
        private const val SPRITE_WIDTH = 40
        private const val SPRITE_HEIGHT = 40
        /**
         * X = (24 - 40) / 2
         * Y = 12 - 40 - 5
         */
        val OFFSETS = Offsets((TILE_WIDTH - SPRITE_WIDTH) / 2, (TILE_HEIGHT - SPRITE_HEIGHT - 1))

        /**
         * Since these patterns are used extensively on other objects, we will reuse this logic
         * across those objects.
         */
        fun getFrames(activity: Activity, direction: Direction): List<UnitFrame>
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
                Activity.FALLING -> arrayOf(1, 1, 2, 2, 3, 3, 4, 4).map {
                    val fallingDirection = _getFallingDirection(direction)
                    UnitFrame(
                        activity,
                        fallingDirection,
                        it.toString()
                    )
                }
                Activity.DEAD -> (1..8).map { UnitFrame(Activity.FALLING, _getFallingDirection(direction), "4") }
                else -> error("Invalid activity ${activity}")
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
    }
}