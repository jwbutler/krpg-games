package com.jwbutler.krpg.behavior
import com.jwbutler.krpg.entities.objects.Corpse
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.sounds.SoundPlayer
import com.jwbutler.krpg.utils.getAdjacentUnblockedCoordinates
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates

enum class RPGActivity : Activity
{
    STANDING,
    WALKING
    {
        override fun onComplete(unit: Unit)
        {
            val x = unit.getCoordinates().x + unit.getDirection().dx
            val y = unit.getCoordinates().y + unit.getDirection().dy
            val coordinates = Coordinates(x, y)
            if (!coordinates.isBlocked())
            {
                unit.moveTo(Coordinates(x, y))
            }
        }
    },
    ATTACKING
    {
        override fun onComplete(unit: Unit)
        {
            val x = unit.getCoordinates().x + unit.getDirection().dx
            val y = unit.getCoordinates().y + unit.getDirection().dy
            val coordinates = Coordinates(x, y)
            val targetUnit = GameState.getInstance().getUnit(coordinates)
            if (targetUnit != null)
            {
                val damage = unit.getDamage(this)
                targetUnit.takeDamage(damage)
                // TODO: Should this be a unit-specific sound?
                SoundPlayer.playSoundAsync("hit1.wav")
            }
        }
    },
    BASHING
    {
        override fun onComplete(unit: Unit)
        {
            val x = unit.getCoordinates().x + unit.getDirection().dx
            val y = unit.getCoordinates().y + unit.getDirection().dy
            val coordinates = Coordinates(x, y)
            val targetUnit = GameState.getInstance().getUnit(coordinates)
            if (targetUnit != null)
            {
                val damage = unit.getDamage(this)
                targetUnit.takeDamage(damage)
                // TODO: Should this be a unit-specific sound?
                SoundPlayer.playSoundAsync("hit1.wav")

                val state = GameState.getInstance()
                val targetCoordinates = coordinates + unit.getDirection()
                if (state.containsCoordinates(targetCoordinates) && !targetCoordinates.isBlocked())
                {
                    targetUnit.moveTo(targetCoordinates)
                }
            }
        }
    },
    FALLING
    {
        override fun onComplete(unit: Unit)
        {
            unit.die()
        }
    },
    DEAD,
    VANISHING,
    APPEARING,
    RESURRECTING
    {
        override fun onComplete(unit: Unit)
        {
            val state = GameState.getInstance()
            val corpse = state.getEntities()
                .filterIsInstance<Corpse>()
                .firstOrNull { it.getCoordinates() == unit.getCoordinates() || !it.getCoordinates().isBlocked() }

            if (corpse != null)
            {
                val candidates = getAdjacentUnblockedCoordinates(unit.getCoordinates())
                if (candidates.isNotEmpty())
                {
                    state.removeObject(corpse)
                    val reanimated = ZombieUnit(20)
                    state.addUnit(reanimated, candidates.random(), unit.getPlayer())
                }
            }
        }
    };

    override fun toString() = name.toLowerCase()
}
