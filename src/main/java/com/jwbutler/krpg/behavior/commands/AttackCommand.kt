package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pathfinder
import kotlin.math.abs

class AttackCommand(override val source: Unit, val target: Unit) : Command
{
    override val type = CommandType.ATTACK
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
                && source.isActivityReady(Activity.ATTACKING)
            )
            {
                val direction = Direction.between(target.getCoordinates(), source.getCoordinates())
                hasAttacked = true
                return Pair(Activity.ATTACKING, direction)
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

    override fun toString(): String
    {
        return "AttackCommand{target=$target}"
    }
}