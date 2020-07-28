package com.jwbutler.krpg.geometry

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.utils.hypotenuse
import java.util.PriorityQueue

class Pathfinder
{
    companion object
    {
        fun findPath(first: Coordinates, second: Coordinates): List<Coordinates>?
        {
            return Impl.DIJKSTRA.findPath(first, second)
        }

        fun findPath(first: Entity, second: Entity): List<Coordinates>?
        {
            return findPath(first.getCoordinates(), second.getCoordinates())
        }

        fun findNextCoordinates(path: List<Coordinates>?, current: Coordinates): Coordinates?
        {
            if (path != null)
            {
                val index = path.indexOf(current)
                check(index < path.lastIndex)
                val next = path[index + 1]
                if (!next.isBlocked())
                {
                    return next
                }
            }
            return null
        }
    }
}

private enum class Impl
{
    DIJKSTRA
    {
        private val INFINITY = Integer.MAX_VALUE

        override fun findPath(source: Coordinates, target: Coordinates): List<Coordinates>?
        {
            val bestKnownDistances = mutableMapOf<Coordinates, Double>()
            val queue = PriorityQueue(Comparator.comparing<Coordinates, Double> { bestKnownDistances[it] })
            val previous = mutableMapOf<Coordinates, Coordinates>()

            val state : GameState = GameState.getInstance()
            val allCoordinates = state.getAllCoordinates()
                .filter { !it.isBlocked() || it == source || it == target }
                .toSet()

            for (coordinates in allCoordinates.minus(source))
            {
                bestKnownDistances[coordinates] = INFINITY.toDouble()
                queue.offer(coordinates)
            }

            bestKnownDistances[source] = 0.0
            queue.offer(source)

            while (queue.isNotEmpty())
            {
                val current = queue.poll()

                for (neighbor in _getNeighbors(current, allCoordinates.intersect(queue)))
                {
                    val distance = bestKnownDistances[current]!! + hypotenuse(current, neighbor)
                    if (distance < bestKnownDistances[neighbor]!!)
                    {
                        bestKnownDistances[neighbor] = distance
                        previous[neighbor] = current
                        queue.remove(neighbor)
                        queue.offer(neighbor)
                    }
                }
            }

            val path = traverseParents(target, previous)
            if (path.first() == source)
            {
                return path
            }
            return null
        }

        private fun traverseParents(end: Coordinates, previous: MutableMap<Coordinates, Coordinates>): List<Coordinates>
        {
            val nodes = mutableListOf<Coordinates>()
            var current: Coordinates? = end

            do
            {
                nodes.add(0, current!!)
                current = previous[current]
            }
            while (current != null)

            return nodes.toList()
        }
    };

    abstract fun findPath(source: Coordinates, target: Coordinates): List<Coordinates>?

    companion object
    {
        fun _getNeighbors(coordinates: Coordinates, all: Set<Coordinates>): MutableSet<Coordinates>
        {
            val neighbors = mutableSetOf<Coordinates>()
            for (dy in (-1..1))
            {
                for (dx in (-1..1))
                {
                    val neighbor = Coordinates(coordinates.x + dx, coordinates.y + dy)
                    if (all.contains(neighbor))
                    {
                        neighbors.add(neighbor)
                    }
                }
            }
            return neighbors
        }
    }
}