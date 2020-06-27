package com.jwbutler.krpg.entities

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.sprites.Sprite

interface Entity
{
    fun getCoordinates(): Coordinates
    fun getSprite(): Sprite
}