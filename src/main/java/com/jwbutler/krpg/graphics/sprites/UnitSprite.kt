package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.Unit
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.Frame
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps

abstract class UnitSprite(private val spriteName: String, private val paletteSwaps: PaletteSwaps) : Sprite
{
    override fun render(entity: Entity): Pair<Image, Pixel>
    {
        val unit = entity as Unit
        val frame = getFrame(unit.getActivity(), unit.getDirection(), unit.getFrameNumber())
        val filename = formatFilename(frame)
        val image = ImageLoader.getInstance().loadImage(filename, paletteSwaps)
        val pixel = Pixel(unit.getCoordinates().x * 20, unit.getCoordinates().y * 20) // TODO
        return Pair(image, pixel)
    }

    open fun formatFilename(frame: Frame): String
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

    protected abstract fun getFrame(activity: Activity, direction: Direction, frameNumber: Int): Frame
}