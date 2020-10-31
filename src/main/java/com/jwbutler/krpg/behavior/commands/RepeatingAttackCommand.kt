package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.entities.units.Unit

class RepeatingAttackCommand(source: Unit, target: Unit) : AbstractAttackCommand(source, target)
{
    override val activity = RPGActivity.ATTACKING
    override val type = CommandType.ATTACK
    override val isRepeating = true
}