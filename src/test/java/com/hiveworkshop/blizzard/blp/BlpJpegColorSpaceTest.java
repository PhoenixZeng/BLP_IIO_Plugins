package com.hiveworkshop.blizzard.blp;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.net.URL;

import static org.junit.Assert.*;

public class BlpJpegColorSpaceTest
{
	@Test
	public void jpegBlpShouldDecodeAsSrgbWithoutGammaWhitening() throws Exception
	{
		URL url = BlpJpegColorSpaceTest.class.getClassLoader().getResource("test/放弃暗.blp");
		assertNotNull("missing test resource: test/放弃暗.blp", url);

		BufferedImage img = ImageIO.read(url);
		assertNotNull("ImageIO.read returned null", img);
		assertNotNull(img.getColorModel());
		assertNotNull(img.getColorModel().getColorSpace());

		ColorSpace cs = img.getColorModel().getColorSpace();
		assertTrue("expected sRGB colorspace, got type=" + cs.getType() + " isCS_sRGB=" + cs.isCS_sRGB(), cs.isCS_sRGB());

		// Ensure getRGB is consistent with raw raster samples (no extra gamma conversion).
		Raster raster = img.getRaster();
		assertTrue("expected at least 3 bands, got " + raster.getNumBands(), raster.getNumBands() >= 3);
		int x = 10;
		int y = 10;
		int r = raster.getSample(x, y, 0) & 0xff;
		int g = raster.getSample(x, y, 1) & 0xff;
		int b = raster.getSample(x, y, 2) & 0xff;
		int a = (raster.getNumBands() >= 4) ? (raster.getSample(x, y, 3) & 0xff) : 255;
		int packed = (a << 24) | (r << 16) | (g << 8) | b;
		assertEquals("pixel mismatch indicates gamma conversion/whitening", packed, img.getRGB(x, y));
	}
}

