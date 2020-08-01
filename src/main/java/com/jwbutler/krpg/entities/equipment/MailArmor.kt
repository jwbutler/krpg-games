package com.jwbutler.krpg.entities.equipment

import com.jwbutler.krpg.graphics.images.PaletteSwaps
import com.jwbutler.krpg.graphics.sprites.MailArmorSprite

class MailArmor : AbstractEquipment(MailArmorSprite(PaletteSwaps.WHITE_TRANSPARENT))
{
    override val slot = EquipmentSlot.CHEST
}