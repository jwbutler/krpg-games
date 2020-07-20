package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.CommandType
import com.jwbutler.krpg.behavior.commands.DieCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.units.UnitSprite
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
        frameNumber = 0

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
        val (activity, direction) = command.chooseActivity()
        setActivity(activity, direction)
    }

    final override fun setActivity(activity: Activity, direction: Direction)
    {
        this.activity = activity
        this.direction = direction
        this.frameNumber = 0
    }

    final override fun moveTo(coordinates: Coordinates)
    {
        require(state.getUnit(coordinates) == null)
        state.removeUnit(this)
        state.addUnit(this, coordinates)
    }

    final override fun getDamage(activity: Activity): Int
    {
        return 10; // TODO
    }

    final override fun takeDamage(amount: Int)
    {
        currentHP = max(currentHP - amount, 0)
        // Dying happens as a state-based effect on upkeep
    }

    final override fun die()
    {
        state.removeUnit(this)
        player.removeUnit(this)
    }

    final override fun update()
    {
        if (currentHP <= 0 && command.type != CommandType.DIE)
        {
            setCommand(DieCommand(this))
        }
        else
        {
            // Take player input
            if (sprite.isAnimationComplete(this))
            {
                activity.onComplete(this)
                val nextCommand = getPlayer().chooseCommand(this)
                if (command.isComplete())
                {
                    setCommand(nextCommand)
                }
                else if (command.isPreemptible() && nextCommand.type != CommandType.STAY)
                {
                    setCommand(nextCommand)
                }
                else
                {
                    val (activity, direction) = command.chooseActivity()
                    this.setActivity(activity, direction)
                }
            }
        }
    }

    final override fun render() = sprite.render(this)

    final override fun afterRender()
    {
        frameNumber++
    }

    override fun addEquipment(equipment: Equipment)
    {
        GameState.getInstance().addEquipment(equipment, this)
    }

    override fun removeEquipment(equipment: Equipment)
    {
        GameState.getInstance().removeEquipment(equipment, this)
    }

    override fun getEquipment() = GameState.getInstance().getEquipment(this)
}