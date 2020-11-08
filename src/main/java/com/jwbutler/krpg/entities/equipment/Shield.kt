package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.graphics.sprites.ShieldSprite
import com.jwbutler.rpglib.graphics.images.PaletteSwaps

class Shield : AbstractEquipment(ShieldSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = RPGEquipmentSlot.OFF_HAND
}