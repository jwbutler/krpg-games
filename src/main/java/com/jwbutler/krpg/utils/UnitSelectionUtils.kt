package com.jwbutler.krpg.utils

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.players.Player

fun getPlayerUnits(): Collection<Unit>
{
    return GameState.getInstance()
        .getPlayers()
        .find(Player::isHuman)
        ?.getUnits()
        ?: error("Could not find player units")
}