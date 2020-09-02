package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.players.ai.UnitAI

class EnemyPlayer : AbstractPlayer()
{
    override val isHuman = false

    override fun chooseCommand(unit: Unit): Command
    {
        val unitAI = when (unit)
        {
            is PlayerUnit -> UnitAI.SIMPLE_ATTACK
            is ZombieUnit -> UnitAI.NO_OP // TODO
            is WizardUnit -> UnitAI.WIZARD
            else -> error("Unsupported unit type ${unit::class}")
        }

        return unitAI.chooseCommand(unit)
    }
}