package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.graphics.sprites.Sprite
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.entities.Entity
import com.jwbutler.rpglib.entities.equipment.Equipment
import com.jwbutler.rpglib.geometry.Offsets
import com.jwbutler.rpglib.graphics.FrameKey
import com.jwbutler.rpglib.graphics.images.Image
import com.jwbutler.rpglib.graphics.images.ImageLoader
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.units.PlayerSprite

private const val BEHIND_PREFIX = "_B"

/**
 * Abstract class for rendering anything that goes on top of a unit
 * (equipment, overlays, etc.)
 *
 * TODO: This is mostly copy-pasted from [PlayerSprite].  However, I can't think of a sane inheritance hierarchy.
 */
abstract class EquipmentSprite(private val spriteName: String, private val paletteSwaps: PaletteSwaps, override val offsets: Offsets) :
    Sprite
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
        return "equipment/${spriteName}/${spriteName}_${frameKey.keys.joinToString("_")}"
    }

    private fun _getFrame(equipment: Equipment): FrameKey
    {
        val unit = equipment.getUnit()
        if (unit != null)
        {
            return _getFrame(unit.getActivity(), unit.getDirection(), unit.getFrameNumber())
        }
        return _getFrame(RPGActivity.DEAD, equipment.direction!!, 1)
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