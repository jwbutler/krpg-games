package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.core.GameState

abstract class AbstractObject : GameObject
{
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun render() = sprite.render(this)

    override fun update() {}
    override fun afterRender() {}
}