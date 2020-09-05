package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.entities.units.Unit

class RepeatingAttackCommand(source: Unit, target: Unit) : AbstractAttackCommand(source, target)
{
    override val activity = Activity.ATTACKING
    override val type = CommandType.ATTACK
    override val isRepeating = true
}