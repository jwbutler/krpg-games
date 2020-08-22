package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pathfinder
import kotlin.math.abs

/**
 * Base class for various melee abilities (attack, bash, etc.)
 */
abstract class AbstractAttackCommand(final override val source: Unit, val target: Unit) : Command
{
    protected abstract val activity: Activity
    private var hasAttacked = false
    private var path = Pathfinder.findPath(source, target)

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        return _tryAttack()
            ?: _tryWalk()
            ?: _stand()
    }

    private fun _tryAttack(): Pair<Activity, Direction>?
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

    private fun _tryWalk(): Pair<Activity, Direction>?
    {
        if (target.exists())
        {
            if (source.isActivityReady(Activity.WALKING))
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
                    return Pair(Activity.WALKING, direction)
                }
            }
        }
        return null
    }

    private fun _stand(): Pair<Activity, Direction>
    {
        return Pair(Activity.STANDING, source.getDirection())
    }

    override fun isPreemptible() = true
    override fun isComplete() = hasAttacked

    private fun _isInRange(first: Coordinates, second: Coordinates): Boolean
    {
        val dx = second.x - first.x
        val dy = second.y - first.y
        return (abs(dx) <= 1 && abs(dy) <= 1)
    }
}