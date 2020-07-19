package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.MailArmorSprite

class Shield : AbstractEquipment(MailArmorSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = EquipmentSlot.OFF_HAND
}