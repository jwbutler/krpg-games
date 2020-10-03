package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.graphics.sprites.Sprite

abstract class AbstractEquipment(override val sprite: Sprite) : Equipment
{
    override var direction: Direction? = null

    override fun getUnit() = GameState.getInstance().getUnit(this)
    override fun getCoordinates() = getUnit()?.getCoordinates() ?: GameState.getInstance().getCoordinates(this)
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun render() = sprite.render(this)

    override fun update() {}

    override fun isBlocking() = false
}