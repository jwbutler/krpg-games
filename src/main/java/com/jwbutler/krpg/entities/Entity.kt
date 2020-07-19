package com.jwbutler.krpg.entities

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.sprites.Sprite

interface Entity
{
    fun getCoordinates(): Coordinates
    fun getSprite(): Sprite

    fun update()
    fun render(): Pair<Image, Pixel>
    fun afterRender()
}