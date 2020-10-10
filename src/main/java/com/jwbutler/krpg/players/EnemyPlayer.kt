package com.jwbutler.krpg.players

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.players.ai.UnitAI
import java.lang.IllegalArgumentException

class EnemyPlayer : AbstractPlayer()
{
    override val isHuman = false

    private val currentCommands = mutableMapOf<Unit, Command>()

    override fun update()
    {
        for (unit in getUnits())
        {
            val unitAI = when (unit)
            {
                is PlayerUnit -> UnitAI.SIMPLE_ATTACK
                is ZombieUnit -> UnitAI.NO_OP // TODO
                is WizardUnit -> UnitAI.WIZARD
                else -> throw IllegalArgumentException("Unsupported unit type ${unit::class.java.getSimpleName()}")
            }

            currentCommands[unit] = unitAI.chooseCommand(unit)
        }
    }

    override fun getNextActivity(unit: Unit): Pair<Activity, Direction>
    {
        return currentCommands[unit]!!.chooseActivity()
    }
}