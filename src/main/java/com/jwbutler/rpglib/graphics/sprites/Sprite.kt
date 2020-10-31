package com.jwbutler.rpglib.graphics.sprites

import com.jwbutler.rpglib.entities.Entity
import com.jwbutler.rpglib.geometry.Offsets
import com.jwbutler.rpglib.graphics.Renderable

/**
 * Sprites are:
 * - immutable
 * - reusable for multiple entities
 * - instantiated with particular palette swaps
 */
interface Sprite
{
    /**
     * This is not guaranteed to be accurate for all frames, but provides a baseline position for derived objects
     */
    val offsets: Offsets
    fun render(entity: Entity): Renderable
}