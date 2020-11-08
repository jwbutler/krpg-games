package com.jwbutler.krpg.utils

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.graphics.FrameKey

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
            RPGActivity.STANDING -> listOf(
                FrameKey(activity, direction, 1)
            )

            RPGActivity.WALKING -> arrayOf(2, 2, 1, 1)
                .map { FrameKey(activity, direction, it) }

            RPGActivity.ATTACKING ->
            {
                val frames = mutableListOf<FrameKey>()
                frames.addAll(
                    arrayOf(1, 2, 2, 1)
                        .map { FrameKey(RPGActivity.ATTACKING, direction, it) }
                )
                frames.addAll(
                    arrayOf(1, 1)
                        .map { FrameKey(RPGActivity.STANDING, direction, it) }
                )
                return frames
            }

            RPGActivity.BASHING ->
            {
                val frames = mutableListOf<FrameKey>()
                frames.addAll(
                    arrayOf(1, 2, 2, 1)
                        .map { FrameKey(RPGActivity.ATTACKING, direction, it) }
                )
                return frames
            }

            RPGActivity.FALLING -> arrayOf(1, 1, 2, 2, 3, 3, 4, 4)
                .map { FrameKey(
                    activity,
                    _getPlayerFallingDirection(direction),
                    it
                ) }

            RPGActivity.DEAD -> (1..8)
                .map { FrameKey(RPGActivity.FALLING, _getPlayerFallingDirection(direction), 4) }

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