package com.jwbutler.krpg.graphics.sprites.units

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.units.Unit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.Sprite

abstract class UnitSprite
(
    private val spriteName: String,
    private val paletteSwaps: PaletteSwaps,
    private val offsets: Offsets
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

    fun isAnimationComplete(unit: Unit): Boolean
    {
        val frames = _getFrames(unit.getActivity(), unit.getDirection())
        return unit.getFrameNumber() > frames.lastIndex
    }

    private fun _getFrame(activity: Activity, direction: Direction, frameNumber: Int): FrameKey
    {
        return _getFrames(activity, direction)[frameNumber]
    }

    private fun _formatFilename(frameKey: FrameKey): String
    {
        return "units/${spriteName}/${spriteName}_${frameKey.keys.joinToString("_")}"
    }

    protected abstract fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
}