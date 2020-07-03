package com.jwbutler.krpg.behavior

import com.jwbutler.krpg.entities.Unit

class StayCommand : Command
{
    override fun getType() = CommandType.STAY
    override fun chooseActivity(unit: Unit) = Pair(Activity.STANDING, unit.getDirection())
}