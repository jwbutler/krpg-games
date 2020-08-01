package com.jwbutler.krpg.graphics

import com.jwbutler.krpg.geometry.Pixel
import com.jwbutler.krpg.graphics.images.Image

/**
 * Represents an image and all data directly necessary to render it.
 * TODO better name
 */
data class Renderable(val image: Image, val pixel: Pixel, val layer: RenderLayer)