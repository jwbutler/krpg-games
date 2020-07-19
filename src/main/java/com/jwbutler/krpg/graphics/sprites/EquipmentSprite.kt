package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.geometry.IntPair
import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.Image
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps
import kotlin.test.fail

private const val BEHIND_PREFIX = "_B"

/**
 * Abstract class for rendering anything that goes on top of a unit
 * (equipment, overlays, etc.)
 *
 * TODO: This is mostly copy-pasted from [PlayerSprite].  However, I can't think of a sane inheritance hierarchy.
 */
abstract class EquipmentSprite(private val spriteName: String, private val paletteSwaps: PaletteSwaps, private val offsets: IntPair) : Sprite
{
    override fun render(entity: Entity): Pair<Image, Pixel>
    {
        val equipment = entity as Equipment
        val unit = equipment.getUnit()
        val frame = _getFrame(unit.getActivity(), unit.getDirection(), unit.getFrameNumber())
        val filename = formatFilename(frame)
        val image = _tryLoadBehindFirst(filename, paletteSwaps)
        val pixel = unit.getCoordinates().toPixel() + offsets
        return Pair(image, pixel)
    }

    open fun formatFilename(frame: UnitFrame): String
    {
        return String.format(
            "equipment/%s/%s_%s_%s_%s",
            spriteName,
            spriteName,
            frame.activity,
            frame.direction,
            frame.key
        )
    }

    private fun _getFrame(activity: Activity, direction: Direction, frameNumber: Int): UnitFrame
    {
        return _getFrames(activity, direction)[frameNumber]
    }

    protected abstract fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>

    private fun _tryLoadBehindFirst(filename: String, paletteSwaps: PaletteSwaps): Image
    {
        val imageLoader = ImageLoader.getInstance()
        return imageLoader.loadOptional(filename + BEHIND_PREFIX, paletteSwaps)
            ?: imageLoader.loadOptional(filename, paletteSwaps)
            ?: fail("Could not find image filename ${filename} or ${filename + BEHIND_PREFIX}")
    }
}