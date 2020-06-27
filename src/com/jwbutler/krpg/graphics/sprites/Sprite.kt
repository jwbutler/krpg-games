package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.Unit
import com.jwbutler.krpg.graphics.Frame
import com.jwbutler.krpg.graphics.ImageUtils
import com.jwbutler.krpg.graphics.PaletteSwaps

/**
 * Sprites are:
 * - immutable
 * - reusable for multiple entities
 * - instantiated with particular palette swaps
 */
interface Sprite
{
    fun getFrame(entity: Entity): Frame

    class PLAYER(private val paletteSwaps: PaletteSwaps? = null) : Sprite
    {
        override fun getFrame(entity: Entity): Frame
        {
            val unit = entity as Unit
            val filename = formatUnitFilename(unit.getActivity(), unit.getDirection(), unit.getFrameNumber())
            val image = ImageUtils.loadImage(filename, paletteSwaps)
            return Frame(image)
        }
    }
}

private fun formatUnitFilename(activity: Activity, direction: Direction, frameNumber: Int): String
{
    return String.format(
        "player_%s_%s_%d",
        activity.toString(),
        direction.toString(),
        frameNumber
    )
}