package com.jwbutler.krpg.entities.equipment

import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.ShieldSprite

class Shield : AbstractEquipment(ShieldSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = RPGEquipmentSlot.OFF_HAND
}