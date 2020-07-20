package com.jwbutler.krpg.entities.objects

import com.jwbutler.krpg.graphics.sprites.Sprite

class Corpse(override val sprite: Sprite) : AbstractObject()
{
    override fun isBlocking() = false
}