package com.hiveworkshop.blizzard.blp;

import cn.hutool.core.img.Img;
import cn.hutool.core.io.FileUtil;
import net.moonlightflower.wc3libs.misc.UnsupportedFormatException;
import net.moonlightflower.wc3libs.misc.image.BLP;
import org.junit.Test;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ServiceLoader;

/**
 * @author : Phoenix (火凌之)
 * @date : 2024/12/07 18:10
 */
public class test1
{
//	@Before
	public void d()
	{
		ServiceLoader<ImageReaderSpi> loader = ServiceLoader.load(ImageReaderSpi.class);
		for (ImageReaderSpi readerSpi : loader)
		{
			System.out.println(Arrays.toString(readerSpi.getFormatNames()));
		}
	}
	
	
	public static BufferedImage resize3(Image im,int width,int height) {
		Image itemp = im.getScaledInstance(width, height, Image.SCALE_SMOOTH + Image.SCALE_AREA_AVERAGING);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		image = g.getDeviceConfiguration()
		         .createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g.dispose();
		g = image.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//				g.setColor(Color.white);
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,0.0f));
//		g.fillRect(0, 0, width, height);
//		if (width == itemp.getWidth(null))
//			g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
//		else
//			g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);

//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g.drawImage(itemp, 0, 0, width,height, null, null);
		g.dispose();
		return image;
	}
	
	@Test
	public void aa()
	{
		String path1 = "C:\\Users\\Phoenix\\Desktop\\新建文件夹 (6)\\达摩克利斯之剑.blp";
		String path2 = "C:\\Users\\Phoenix\\Desktop\\新建文件夹 (6)\\达摩克利斯之剑2.png";
		
		boolean icon = false;
		boolean isFormat = false;
		boolean isBottom = false;
		boolean modMass = false;
		boolean notLimit = false;
		BufferedImage water = null;
		String ext = path2.substring(path2.length() - 3, path2.length());
		Integer mass = 80;
		File newFile = new File(path2);
		
		File blpFile = new File(path1);
		try
		{
			BufferedImage img = ImageIO.read(blpFile);
			Image image = img;
			
			if (icon && (img.getHeight()  != 64 || img.getWidth() != 64 ))
			{
				image = resize3(img, 64, 64);
			}
			if(isFormat && !icon){
//			image = ImgUtil.scale(image, TextureUtils.getStdSize(img.getWidth()), TextureUtils.getStdSize(img.getHeight()));

//			image = ImgUtil.scale(image, TextureUtils.getStdSize(img.getWidth()),TextureUtils.getStdSize(img.getHeight()));
				image = resize3(img,
						         getStdSize(img.getWidth()),
						         getStdSize(img.getHeight()));
				if (water != null)
				{
					water = resize3(water,
					                getStdSize(img.getWidth()),
					                getStdSize(img.getHeight()));
				}
			}
			
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
			                                                image.getHeight(null),
			                                                BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g = bufferedImage.createGraphics();
			bufferedImage = g.getDeviceConfiguration()
			                 .createCompatibleImage(image.getWidth(null),
			                                        image.getHeight(null),
			                                        Transparency.TRANSLUCENT);
			g.dispose();
			g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			if ( isBottom && water != null)
			{
				g.drawImage(water,0,0,null);
			}
			g.drawImage(image,0,0,null);
			if (! isBottom && water != null)
			{
				g.drawImage(water,0,0,null);
			}

//		RenderedImage renderedImage = ImgUtil.toRenderedImage(image);
			g.dispose();
//		if (ext.equals("blp")){
//			bufferedImage = ImgUtil.toBufferedImage(image);
//		}
			
			if (!newFile.exists())
			{
				FileUtil.touch(newFile);
			}
			
			if (ext.equalsIgnoreCase("blp")){
				if (modMass || notLimit )
				{
					
					ImageIO.setUseCache(false);
					ImageOutputStream outputStream = ImageIO.createImageOutputStream(newFile);
					ImageWriter writer = ImageIO.getImageWritersByFormatName(ext).next();
					BLPWriteParam blpWriteParam = new BLPWriteParam();
					if (modMass)
					{
						blpWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						blpWriteParam.setCompressionQuality(mass.floatValue());
					}
					if (notLimit)
					{
						blpWriteParam.setScaleOptimization(BLPWriteParam.ScaleOptimization.NONE);
					}
					
					writer.setOutput(outputStream);
					writer.write(null, new IIOImage(bufferedImage, null, null), blpWriteParam);
					writer.dispose();
					outputStream.flush();
					outputStream.close();
				}else{
					ImageIO.write(bufferedImage, ext, newFile);
				}
			}
			else{
				Img.from(img).write(newFile);
			}
//			ImageIO.write(img, ext, newFile);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	
	@Test
	public void aa2()
	{
		String path1 = "C:\\Users\\Phoenix\\Desktop\\新建文件夹 (6)\\az_crack12.blp";
		String path2 = "C:\\Users\\Phoenix\\Desktop\\新建文件夹 (6)\\112.png";
		String ext = path2.substring(path2.length() - 3);
		File blpFile = new File(path1);
		File newFile = new File(path2);
		
		try
		{
			BLP blp = new BLP(blpFile);
			BufferedImage img = blp.getBufImg();
//			BufferedImage img = ImageIO.read(blpFile);
			
			if (!newFile.exists())
			{
				FileUtil.touch(newFile);
			}
			BufferedImage bufferedImage = img;
//			BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
//			                                                img.getHeight(null),
//			                                                BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g = bufferedImage.createGraphics();
			bufferedImage = g.getDeviceConfiguration()
			                 .createCompatibleImage(img.getWidth(null),
			                                        img.getHeight(null),
			                                        Transparency.TRANSLUCENT);
			g.dispose();
			g = bufferedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
//			g.fillRect(0, 0, img.getWidth(), img.getHeight());
//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
//			bufferedImage = g.getDeviceConfiguration()
//			                 .createCompatibleImage(img.getWidth(null),
//			                                        img.getHeight(null),
//			                                        Transparency.TRANSLUCENT);
			g.drawImage(img,0,0,null);
			g.dispose();
			img = bufferedImage;
			if (ext.equalsIgnoreCase("blp")){
				ImageIO.write(img, ext, newFile);
			}
			else{
				Img.from(img).write(newFile);
			}
//			ImageIO.write(img, ext, newFile);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static int getStdSize(int i)
	{
		if (i <= 1024)
		{
			if (i <= 512)
			{
				return getNear2Pow(i);
			}else{
				return 512;
			}
		}
		return 512;
	}
	public static int getNear2Pow(int i)
	{
		if ((i & (i - 1)) == 0) return i;
		int n = i - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return n + 1;
	}
}
