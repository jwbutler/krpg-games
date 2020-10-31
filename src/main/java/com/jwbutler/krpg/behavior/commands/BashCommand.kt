package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.entities.units.Unit

class BashCommand(source: Unit, target: Unit) : AbstractAttackCommand(source, target)
{
    override val activity = RPGActivity.BASHING
    override val type = CommandType.BASH
    override val isRepeating = false
}