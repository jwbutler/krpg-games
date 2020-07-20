package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.players.ai.UnitAI

class EnemyPlayer : AbstractPlayer()
{
    override fun chooseCommand(unit: Unit): Command
    {
        return UnitAI.SIMPLE_ATTACK.chooseCommand(unit);
    }
}