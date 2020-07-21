package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.players.ai.UnitAI

class EnemyPlayer : AbstractPlayer()
{
    override fun chooseCommand(unit: Unit): Command
    {
        val unitAI = when
        {
            unit is PlayerUnit -> UnitAI.SIMPLE_ATTACK
            unit is ZombieUnit -> UnitAI.SIMPLE_ATTACK
            unit is WizardUnit -> UnitAI.NO_OP
            else -> error("fux")
        }

        return unitAI.chooseCommand(unit)
    }
}