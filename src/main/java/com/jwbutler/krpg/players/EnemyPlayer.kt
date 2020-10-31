package com.jwbutler.krpg.players

import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.players.ai.UnitAI
import java.lang.IllegalArgumentException

class EnemyPlayer : AbstractPlayer()
{
    override val isHuman = false

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        val unitAI = when (unit)
        {
            is PlayerUnit -> UnitAI.SIMPLE_ATTACK
            is ZombieUnit -> UnitAI.NO_OP // TODO
            is WizardUnit -> UnitAI.WIZARD
            else -> throw IllegalArgumentException("Unsupported unit type ${unit::class.java.getSimpleName()}")
        }

        return unitAI.chooseCommand(unit).chooseActivity()
    }
}