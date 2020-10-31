package com.jwbutler.rpglib.graphics

import com.jwbutler.rpglib.geometry.Pixel
import com.jwbutler.rpglib.graphics.images.Image

/**
 * Represents an image and all data directly necessary to render it.
 */
data class Renderable
(
    val image: Image,
    val pixel: Pixel,
    val layer: RenderLayer
)