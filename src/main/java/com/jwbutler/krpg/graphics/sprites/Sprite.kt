package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.Image

/**
 * Sprites are:
 * - immutable
 * - reusable for multiple entities
 * - instantiated with particular palette swaps
 */
interface Sprite
{
    fun render(entity: Entity): Pair<Image, Pixel>
}