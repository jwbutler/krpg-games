package com.jwbutler.krpg.utils

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.FrameKey

object SpriteUtils
{
    /**
     * Since these patterns are used extensively on other objects, we will reuse this logic
     * across those objects.
     */
    fun getPlayerFrames(activity: Activity, direction: Direction): List<FrameKey>
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
            Activity.BASHING ->
            {
                val frames = mutableListOf<FrameKey>()
                frames.addAll(arrayOf(1, 2, 2, 1).map { FrameKey(Activity.ATTACKING, direction, it) })
                return frames
            }
            Activity.FALLING -> arrayOf(1, 1, 2, 2, 3, 3, 4, 4).map {
                val fallingDirection = _getPlayerFallingDirection(direction)
                FrameKey(activity, fallingDirection, it)
            }
            Activity.DEAD -> (1..8).map { FrameKey(Activity.FALLING, _getPlayerFallingDirection(direction), 4) }
            else -> error("Invalid activity ${activity}")
        }
    }

    private fun _getPlayerFallingDirection(direction: Direction): Direction
    {
        return when (direction)
        {
            Direction.N, Direction.NE, Direction.E, Direction.SE -> Direction.NE
            else -> Direction.S
        }
    }
}