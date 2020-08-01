package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.graphics.sprites.Sprite

abstract class AbstractEquipment(override val sprite: Sprite) : Equipment
{
    private var unit: Unit? = null
    override var direction: Direction? = null

    override fun getUnit() = unit
    override fun getCoordinates() = unit?.getCoordinates() ?: GameState.getInstance().getCoordinates(this)
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun render() = sprite.render(this)

    override fun update() {}
    override fun afterRender() {}

    /**
     * Usage pattern: this will be set within the unit's [Unit.addEquipment] method
     */
    override fun setUnit(unit: Unit?)
    {
        check((this.unit == null) != (unit == null))
        this.unit = unit
    }

    override fun isBlocking() = false
}