package com.jwbutler.krpg.entities

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.Command
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.Sprite
import kotlin.math.max

abstract class AbstractUnit(private val sprite: Sprite, coordinates: Coordinates, hp: Int): Unit
{
    private val state = GameState.getInstance()

    private var command: Command
    private var activity: Activity
    private var direction: Direction
    private var frameNumber: Int

    private var currentHP: Int
    private var maxHP: Int

    init
    {
        GameState.getInstance().addUnit(this, coordinates) // I think this is fine
        command = getStayCommand() // I think this is fine
        activity = Activity.STANDING
        direction = Direction.SE
        frameNumber = 1

        currentHP = hp
        maxHP = hp
    }

    override fun getSprite() = sprite
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun getCommand() = command
    override fun getActivity() = activity
    override fun getDirection() = direction
    override fun getFrameNumber() = frameNumber

    override fun getCurrentHP() = currentHP
    override fun getMaxHP() = maxHP

    override fun setCommand(command: Command)
    {
        this.command = command
        chooseActivity(command)
    }

    override fun moveTo(coordinates: Coordinates)
    {
        require(state.getUnit(coordinates) == null)
        state.removeUnit(this)
        state.addUnit(this, coordinates)
    }

    override fun takeDamage(amount: Int)
    {
        currentHP = max(currentHP - amount, 0)
        if (currentHP <= 0)
        {
            setCommand(getDieCommand())
        }
    }

    override fun die()
    {
        state.removeUnit(this)
    }

    final override fun update()
    {

    }

    final override fun render() = sprite.render(this)

    private fun chooseActivity(command: Command)
    {
        val (activity, direction) = command.chooseActivity(this)
        this.activity = activity
        this.direction = direction
    }

    protected abstract fun getMoveCommand(target: Coordinates): Command
    protected abstract fun getAttackCommand(target: Unit): Command
    protected abstract fun getStayCommand(): Command
    protected abstract fun getDieCommand(): Command
}