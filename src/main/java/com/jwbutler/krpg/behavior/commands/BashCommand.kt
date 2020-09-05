package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.entities.units.Unit

class BashCommand(source: Unit, target: Unit) : AbstractAttackCommand(source, target)
{
    override val activity = Activity.BASHING
    override val type = CommandType.BASH
    override val isRepeating = false
}