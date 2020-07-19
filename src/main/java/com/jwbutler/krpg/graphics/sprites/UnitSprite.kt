package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps

abstract class UnitSprite(private val spriteName: String, private val paletteSwaps: PaletteSwaps) : Sprite
{
    override fun render(entity: Entity): Pair<Image, Pixel>
    {
        val unit = entity as Unit
        val frame = _getFrame(unit.getActivity(), unit.getDirection(), unit.getFrameNumber())
        val filename = formatFilename(frame)
        val image = ImageLoader.getInstance().loadImage(filename, paletteSwaps)
        val pixel = unit.getCoordinates().toPixel()
        return Pair(image, pixel)
    }

    open fun formatFilename(frame: UnitFrame): String
    {
        return String.format(
            "units/%s/%s_%s_%s_%s",
            spriteName,
            spriteName,
            frame.activity,
            frame.direction,
            frame.key
        )
    }

    fun isAnimationComplete(unit: Unit): Boolean
    {
        val frames = _getFrames(unit.getActivity(), unit.getDirection())
        return unit.getFrameNumber() > frames.lastIndex
    }

    private fun _getFrame(activity: Activity, direction: Direction, frameNumber: Int): UnitFrame
    {
        return _getFrames(activity, direction)[frameNumber]
    }

    protected abstract fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
}