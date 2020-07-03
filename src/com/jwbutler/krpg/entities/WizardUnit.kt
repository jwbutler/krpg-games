package com.jwbutler.krpg.entities

import com.jwbutler.krpg.behavior.Command
import com.jwbutler.krpg.behavior.StayCommand
import com.jwbutler.krpg.behavior.TeleportingMoveCommand
import com.jwbutler.krpg.behavior.ThreeDirectionDieCommand
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.WizardSprite
import java.lang.UnsupportedOperationException

class WizardUnit(coordinates: Coordinates, hp: Int) : AbstractUnit(WizardSprite(PaletteSwaps()), coordinates, hp)
{
    override fun getMoveCommand(target: Coordinates): Command
    {
        return TeleportingMoveCommand(target)
    }

    override fun getAttackCommand(target: Unit): Command
    {
        throw UnsupportedOperationException()
    }

    override fun getStayCommand(): Command
    {
        return StayCommand()
    }

    override fun getDieCommand(): Command
    {
        return ThreeDirectionDieCommand()
    }
}