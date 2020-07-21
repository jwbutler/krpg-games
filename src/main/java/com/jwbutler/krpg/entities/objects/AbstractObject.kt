package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Coordinates

abstract class AbstractObject(coordinates: Coordinates) : GameObject
{
    init
    {
        GameState.getInstance().addObject(this, coordinates)
    }

    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun render() = sprite.render(this)

    override fun update() {}
    override fun afterRender() {}
}