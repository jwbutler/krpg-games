package com.jwbutler.krpg.entities.objects

import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.objects.GameObject

abstract class AbstractObject : GameObject
{
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun render() = sprite.render(this)

    override fun update() {}
}