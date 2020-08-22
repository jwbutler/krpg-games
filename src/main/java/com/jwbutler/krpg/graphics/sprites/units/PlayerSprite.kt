package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.geometry.TILE_HEIGHT
import com.jwbutler.krpg.geometry.TILE_WIDTH
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import java.awt.Color

class PlayerSprite(paletteSwaps: PaletteSwaps) : UnitSprite(
    "player",
    paletteSwaps.withTransparentColor(Color.WHITE)
)
{
    override val offsets = OFFSETS

    override fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
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
        fun getFrames(activity: Activity, direction: Direction): List<FrameKey>
        {
            return when (activity)
            {
                Activity.STANDING -> listOf(FrameKey(activity, direction, 1))
                Activity.WALKING -> arrayOf(2, 1, 1).map { FrameKey(activity, direction, it) }
                Activity.ATTACKING ->
                {
                    val frames = mutableListOf<FrameKey>()
                    frames.addAll(arrayOf(1, 2, 2, 1).map { FrameKey(Activity.ATTACKING, direction, it) })
                    return frames
                }
                // TODO 2b
                Activity.BASHING ->
                {
                    val frames = mutableListOf<FrameKey>()
                    frames.addAll(arrayOf(1, 2, 2, 1).map { FrameKey(Activity.ATTACKING, direction, it) })
                    return frames
                }
                Activity.FALLING -> arrayOf(1, 1, 2, 2, 3, 3, 4, 4).map {
                    val fallingDirection = _getFallingDirection(direction)
                    FrameKey(activity, fallingDirection, it)
                }
                Activity.DEAD -> (1..8).map { FrameKey(Activity.FALLING, _getFallingDirection(direction), 4) }
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