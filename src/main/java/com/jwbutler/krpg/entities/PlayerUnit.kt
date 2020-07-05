package com.jwbutler.krpg.entities

import com.jwbutler.krpg.behavior.*
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.PlayerSprite

class PlayerUnit(coordinates: Coordinates, hp: Int) : AbstractUnit(PlayerSprite(PaletteSwaps()), coordinates, hp)
{
    // protected functions

    override fun getMoveCommand(target: Coordinates): Command
    {
        return MoveCommand(target)
    }

    override fun getAttackCommand(target: Unit): Command
    {
        return AttackCommand(target)
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