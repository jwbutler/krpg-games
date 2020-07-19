package com.jwbutler.krpg.entities

import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.sprites.Sprite

interface Entity
{
    /**
     * Implementations of this will differ:
     * Units, tiles, etc. will delegate this to [GameState],
     * while dependent entities such as equipment will delegate to their owner
     */
    fun getCoordinates(): Coordinates
    fun getSprite(): Sprite

    fun update()
    fun render(): Pair<Image, Pixel>
    fun afterRender()
}