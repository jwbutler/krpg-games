package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.Entity
import com.jwbutler.krpg.entities.equipment.Equipment
import com.jwbutler.krpg.geometry.Offsets
import com.jwbutler.krpg.graphics.FrameKey
import com.jwbutler.krpg.graphics.images.Image
import com.jwbutler.krpg.graphics.images.ImageLoader
import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite

private const val BEHIND_PREFIX = "_B"

/**
 * Abstract class for rendering anything that goes on top of a unit
 * (equipment, overlays, etc.)
 *
 * TODO: This is mostly copy-pasted from [PlayerSprite].  However, I can't think of a sane inheritance hierarchy.
 */
abstract class EquipmentSprite(private val spriteName: String, private val paletteSwaps: PaletteSwaps, override val offsets: Offsets) : Sprite
{
    override fun render(entity: Entity): Renderable
    {
        val equipment = entity as Equipment
        val frame = _getFrame(equipment)
        val filename = _formatFilename(frame)
        val (image, renderLayer) = _tryLoadBehindFirst(filename, paletteSwaps)
        val pixel = entity.getCoordinates().toPixel() + offsets
        return Renderable(image, pixel, renderLayer)
    }

    private fun _formatFilename(frameKey: FrameKey): String
    {
        return "equipment/${spriteName}/${spriteName}_${frameKey}"
    }

    private fun _getFrame(equipment: Equipment): FrameKey
    {
        val unit = equipment.getUnit()
        if (unit != null)
        {
            return _getFrame(unit.getActivity(), unit.getDirection(), unit.getFrameNumber())
        }
        return _getFrame(Activity.DEAD, equipment.direction!!, 1)
    }

    private fun _getFrame(activity: Activity, direction: Direction, frameNumber: Int): FrameKey
    {
        val frames = _getFrames(activity, direction)
        return frames[frameNumber]
    }

    private fun _tryLoadBehindFirst(filename: String, paletteSwaps: PaletteSwaps): Pair<Image, RenderLayer>
    {
        val imageLoader = ImageLoader.getInstance()
        var image = imageLoader.loadOptional(filename + BEHIND_PREFIX, paletteSwaps)
        if (image != null)
        {
            return Pair(image, RenderLayer.EQUIPMENT_BEHIND)
        }
        else
        {
            image = imageLoader.loadOptional(filename, paletteSwaps)
            if (image != null)
            {
                return Pair(image, RenderLayer.EQUIPMENT_ABOVE)
            }
        }
        error("Could not find image filename ${filename} or ${filename + BEHIND_PREFIX}")
    }

    protected abstract fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
}