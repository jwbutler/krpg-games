package com.jwbutler.krpg.geometry

import com.jwbutler.krpg.core.GameState

object GeometryUtils
{
    fun getAdjacentCoordinates(coordinates: Coordinates): Set<Coordinates>
    {
        val adjacentCoordinates = mutableSetOf<Coordinates>()
        for (dy in -1..1)
        {
            for (dx in -1..1)
            {
                val candidate = Coordinates(coordinates.x + dx, coordinates.y + dy)
                if (!candidate.isBlocked())
                {
                    adjacentCoordinates.add(candidate)
                }
            }
        }
        val state = GameState.getInstance()
        return adjacentCoordinates
            .filter(state::containsCoordinates)
            .filter { it != coordinates }
            .toSet()
    }

    fun getAdjacentUnblockedCoordinates(coordinates: Coordinates): Set<Coordinates>
    {
        return getAdjacentCoordinates(coordinates).filter { !it.isBlocked() }.toSet()
    }
}