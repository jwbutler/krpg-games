package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.DieCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.UnitSprite
import com.jwbutler.krpg.players.Player
import kotlin.math.max

abstract class AbstractUnit(private var player: Player, private val sprite: UnitSprite, coordinates: Coordinates, hp: Int) : Unit
{
    private val state = GameState.getInstance()

    private var command: Command
    private var activity: Activity
    private var direction: Direction
    /**
     * This isn't "1", "2", "2b"... this is 1, 2, 3
     */
    private var frameNumber: Int

    private var currentHP: Int
    private var maxHP: Int

    init
    {
        // Lots of `this` escaping.  I don't see a problem, though.
        // The idea is that this constructor handles all the necessary state updates
        // so the caller doesn't have to handle them individually.
        GameState.getInstance().addUnit(this, coordinates)
        player.addUnit(this)
        command = StayCommand(this)
        activity = Activity.STANDING
        direction = Direction.SE
        frameNumber = 1

        currentHP = hp
        maxHP = hp
    }

    override fun getPlayer() = player
    override fun getSprite() = sprite
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun getCommand() = command
    override fun getActivity() = activity
    override fun getDirection() = direction
    override fun getFrameNumber() = frameNumber

    override fun getCurrentHP() = currentHP
    override fun getMaxHP() = maxHP

    final override fun setCommand(command: Command)
    {
        this.command = command
        val (activity, direction) = command.getActivity()
        this.activity = activity
        this.direction = direction
        this.frameNumber = 1
        println("${this.activity} ${this.direction}")
    }

    final override fun moveTo(coordinates: Coordinates)
    {
        require(state.getUnit(coordinates) == null)
        state.removeUnit(this)
        state.addUnit(this, coordinates)
    }

    final override fun takeDamage(amount: Int)
    {
        currentHP = max(currentHP - amount, 0)
        if (currentHP <= 0)
        {
            setCommand(DieCommand(this))
        }
    }

    final override fun die()
    {
        state.removeUnit(this)
        player.removeUnit(this)
    }

    final override fun update()
    {
        // Take player input
        // (AI input too)
        // TODO this is assuming the unit is the player unit
        if (sprite.isAnimationComplete(this))
        {
            activity.onComplete(this)
            val command = getPlayer().chooseCommand(this)
            setCommand(command)
        }
    }

    final override fun render() = sprite.render(this)

    final override fun afterRender()
    {
        frameNumber++
    }
}