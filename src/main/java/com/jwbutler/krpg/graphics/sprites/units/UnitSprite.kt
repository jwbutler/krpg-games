package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.Sprite

abstract class UnitSprite
(
    protected val spriteName: String,
    private val paletteSwaps: PaletteSwaps,
    val offsets: Offsets
) : Sprite
{
    override fun render(entity: Entity): Renderable
    {
        val unit = entity as Unit
        val coordinates = unit.getCoordinates()
        return render(unit.getActivity(), unit.getDirection(), unit.getFrameNumber(), coordinates)
    }

    fun render(activity: Activity, direction: Direction, frameNumber: Int, coordinates: Coordinates): Renderable
    {
        val frame = _getFrame(activity, direction, frameNumber)
        val filename = _formatFilename(frame)
        val image = ImageLoader.getInstance().loadImage(filename, paletteSwaps)
        val pixel = coordinates.toPixel() + offsets
        return Renderable(image, pixel, RenderLayer.UNIT)
    }

    open fun _formatFilename(frame: UnitFrame): String
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