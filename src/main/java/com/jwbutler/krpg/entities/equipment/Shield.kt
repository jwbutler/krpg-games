package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.ShieldSprite

class Shield : AbstractEquipment(ShieldSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = EquipmentSlot.OFF_HAND
}