package com.mrpowergamerbr.loritta.commands.nashorn.wrappers

import com.mrpowergamerbr.loritta.commands.nashorn.LorittaNashornException
import com.mrpowergamerbr.loritta.utils.ImageUtils

import java.awt.*
import java.awt.image.BufferedImage

/**
 * Wrapper para o BufferedImage, usado para imagens de comandos Nashorn
 */
class NashornImage {
	private var bufferedImage: BufferedImage
	private var graphics: Graphics

	constructor(x: Int, y: Int) {
		if (x > 1024 || y > 1024) {
			throw LorittaNashornException("Imagem grande demais!")
		}
		bufferedImage = BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB)
		graphics = bufferedImage.graphics
	}

	constructor(image: BufferedImage) {
		this.bufferedImage = image
		graphics = bufferedImage.graphics
	}

	fun write(texto: String, x: Int, y: Int): NashornImage {
		return write(texto, Color(0, 0, 0), x, y)
	}

	fun write(texto: String, cor: Color, x: Int, y: Int): NashornImage {
		graphics!!.color = cor
		ImageUtils.drawTextWrap(texto, x, y, 9999, 9999, graphics.fontMetrics, graphics)
		return this
	}

	fun resize(x: Int, y: Int): NashornImage {
		if (x > 1024 || y > 1024) {
			throw LorittaNashornException("Imagem grande demais!")
		}
		return NashornImage(ImageUtils.toBufferedImage(bufferedImage.getScaledInstance(x, y, BufferedImage.SCALE_SMOOTH)))
	}

	fun paste(imagem: NashornImage, x: Int, y: Int): NashornImage {
		graphics!!.drawImage(imagem.bufferedImage, x, y, null)
		return this
	}

	fun paste(x: Int, y: Int, h: Int, w: Int): NashornImage {
		bufferedImage = bufferedImage.getSubimage(x, y, h, w)
		this.graphics = bufferedImage.graphics
		return this
	}

	fun fillRectangle(color: Color, x: Int, y: Int, h: Int, w: Int): NashornImage {
		graphics.color = color
		graphics.fillRect(x, y, h, w)
		return this
	}
}