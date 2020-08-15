package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.CommandType
import com.jwbutler.krpg.behavior.commands.DieCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.units.UnitSprite
import kotlin.math.max

abstract class AbstractUnit(hp: Int, activities: Set<Activity>) : Unit
{
    abstract override val sprite: UnitSprite

    private var command: Command
    private var activity: Activity
    private var direction: Direction
    private var frameNumber: Int

    private var currentHP: Int
    private var maxHP: Int

    private val remainingCooldowns = mutableMapOf<Activity, Int>()
    private val activities = activities.toMutableSet()

    init
    {
        command = StayCommand(this)
        activity = Activity.STANDING
        direction = Direction.SE
        frameNumber = 0

        currentHP = hp
        maxHP = hp
    }

    override fun isBlocking() = true
    override fun getPlayer() = GameState.getInstance().getPlayer(this)
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun getCommand() = command
    override fun getActivity() = activity
    override fun getDirection() = direction
    override fun getFrameNumber() = frameNumber

    override fun getCurrentHP() = currentHP
    override fun getMaxHP() = maxHP

    final override fun getRemainingCooldown(activity: Activity): Int
    {
        return remainingCooldowns.getOrDefault(activity, 0)
    }

    final override fun triggerCooldown(activity: Activity)
    {
        require(activities.contains(activity)) { "$this can't use activity $activity" }
        val duration = getCooldown(activity)
        remainingCooldowns[activity] = max(remainingCooldowns[activity] ?: 0, duration)
    }

    final override fun isActivityReady(activity: Activity): Boolean
    {
        if (activities.contains(activity))
        {
            return getRemainingCooldown(activity) <= 0
        }
        return false
    }

    final override fun setCommand(command: Command)
    {
        this.command = command
        val (activity, direction) = command.chooseActivity()
        setActivity(activity, direction)
    }

    final override fun setActivity(activity: Activity, direction: Direction)
    {
        check(getRemainingCooldown(activity) <= 0)
        this.activity = activity
        this.direction = direction
        this.frameNumber = 0
    }

    final override fun moveTo(coordinates: Coordinates)
    {
        val state = GameState.getInstance()
        require(state.getUnit(coordinates) == null)
        state.moveUnit(this, coordinates)
    }

    final override fun getDamage(activity: Activity): Int
    {
        return 10 // TODO
    }

    final override fun takeDamage(amount: Int)
    {
        currentHP = max(currentHP - amount, 0)
        // Dying happens as a state-based effect on upkeep
    }

    final override fun die()
    {
        val state = GameState.getInstance()

        val equipmentList = getEquipment().values.toMutableList()
        val coordinates = getCoordinates()
        while (equipmentList.isNotEmpty())
        {
            val equipment = equipmentList.removeAt(0)
            removeEquipment(equipment)
            state.addEquipment(equipment, coordinates)
            equipment.direction = this.direction
        }

        val corpse = Corpse(this)
        state.addObject(corpse, coordinates)
        state.removeUnit(this)
    }

    final override fun update()
    {
        // TODO: Replace reference to hardcoded command type
        if (currentHP <= 0 && command.type != CommandType.DIE)
        {
            setCommand(DieCommand(this))
        }
        else
        {
            if (sprite.isAnimationComplete(this))
            {
                _onActivityComplete(activity)

                // TODO: Activity#onComplete can result in killing this unit, and making some
                // subsequent checks fail.  Can we solve this problem some other way?
                if (!this.exists())
                {
                    return
                }

                val nextCommand = getPlayer().chooseCommand(this)
                if (command.isComplete())
                {
                    setCommand(nextCommand)
                }
                // TODO: Replace reference to hardcoded command type
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
        remainingCooldowns.replaceAll { activity, current -> max(current - 1, 0) }
    }

    final override fun addEquipment(equipment: Equipment)
    {
        GameState.getInstance().addEquipment(equipment, this)
    }

    final override fun removeEquipment(equipment: Equipment)
    {
        GameState.getInstance().removeEquipment(equipment, this)
    }

    final override fun getEquipment() = GameState.getInstance().getEquipment(this)

    override fun toString() = this::class.java.getSimpleName()

    private fun _onActivityComplete(activity: Activity)
    {
        activity.onComplete(this)
        triggerCooldown(activity)
    }
}