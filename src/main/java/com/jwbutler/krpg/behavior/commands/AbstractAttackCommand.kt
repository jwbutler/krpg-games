package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Pathfinder
import kotlin.math.abs

/**
 * Base class for various melee abilities (attack, bash, etc.)
 */
abstract class AbstractAttackCommand(final override val source: Unit, val target: Unit) : Command
{
    protected abstract val activity: com.jwbutler.rpglib.behavior.Activity
    protected abstract val isRepeating: Boolean
    private var hasAttacked = false
    private var path = Pathfinder.findPath(source, target)

    override fun chooseActivity(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        return _tryAttack()
            ?: _tryWalk()
            ?: _stand()
    }

    private fun _tryAttack(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>?
    {
        if (target.exists())
        {
            if (
                _isInRange(source.getCoordinates(), target.getCoordinates())
                && source.isActivityReady(activity)
            )
            {
                val direction = Direction.between(target.getCoordinates(), source.getCoordinates())
                hasAttacked = true
                return Pair(activity, direction)
            }
        }
        return null
    }

    private fun _tryWalk(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>?
    {
        if (target.exists())
        {
            if (source.isActivityReady(RPGActivity.WALKING))
            {
                var next = Pathfinder.findNextCoordinates(path, source.getCoordinates())
                if (next == null)
                {
                    path = Pathfinder.findPath(source, target)
                    next = Pathfinder.findNextCoordinates(path, source.getCoordinates())
                }
                if (next != null)
                {
                    val direction = Direction.between(next, source.getCoordinates())
                    return Pair(RPGActivity.WALKING, direction)
                }
            }
        }
        return null
    }

    private fun _stand(): Pair<com.jwbutler.rpglib.behavior.Activity, Direction>
    {
        return Pair(RPGActivity.STANDING, source.getDirection())
    }

    override fun isPreemptible() = true
    override fun isComplete() = hasAttacked && !isRepeating

    private fun _isInRange(first: Coordinates, second: Coordinates): Boolean
    {
        val dx = second.x - first.x
        val dy = second.y - first.y
        return (abs(dx) <= 1 && abs(dy) <= 1)
    }

    override fun toString(): String
    {
        return "${javaClass.getSimpleName()}{target=$target}"
    }
}