package com.jwbutler.krpg.entities.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.units.UnitSprite
import kotlin.math.max

abstract class AbstractUnit(hp: Int, activities: Set<Activity>) : Unit
{
    abstract override val sprite: UnitSprite

    private var activity: Activity = Activity.STANDING
    private var direction: Direction = Direction.SE
    private var frameNumber: Int = 0

    private var currentHP: Int = hp
    private var maxHP: Int = hp

    private val remainingCooldowns = mutableMapOf<Activity, Int>()
    private val activities = activities.toMutableSet()

    override fun isBlocking() = true
    override fun getPlayer() = GameState.getInstance().getPlayer(this)
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun exists() = GameState.getInstance().containsEntity(this)
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

    final override fun startActivity(activity: Activity, direction: Direction)
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
        if (!GameEngine.getInstance().isPaused())
        {
            frameNumber++
            remainingCooldowns.replaceAll { activity, current -> max(current - 1, 0) }
        }

        // If HP reaches zero, immediately cancel the current activity and start falling
        if (currentHP <= 0 && activity != Activity.FALLING)
        {
            startActivity(Activity.FALLING, direction)
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

                val (activity, direction) = getPlayer().chooseActivity(this)
                startActivity(activity, direction)
            }
        }
    }

    final override fun render() = sprite.render(this)

    final override fun addEquipment(equipment: Equipment)
    {
        GameState.getInstance().addEquipment(equipment, this)
    }

    final override fun removeEquipment(equipment: Equipment)
    {
        GameState.getInstance().removeEquipment(equipment, this)
    }

    final override fun getEquipment() = GameState.getInstance().getEquipment(this)

    override fun toString(): String = this::class.java.getSimpleName()

    private fun _onActivityComplete(activity: Activity)
    {
        activity.onComplete(this)
        triggerCooldown(activity)
    }
}