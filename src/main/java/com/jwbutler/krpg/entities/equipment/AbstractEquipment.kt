package com.jwbutler.krpg.entities.equipment

import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.equipment.Equipment
import com.jwbutler.rpglib.graphics.sprites.Sprite

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