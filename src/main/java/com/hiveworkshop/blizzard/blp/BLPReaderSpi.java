package com.hiveworkshop.blizzard.blp;

import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Locale;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import com.hiveworkshop.lang.MagicInt;

import static com.hiveworkshop.blizzard.blp.ImageSpiCommon.*;

/**
 * Service provider for BLP image file ImageReader.
 * 
 * @author Imperial Good
 */
public class BLPReaderSpi extends ImageReaderSpi {
	static final String READER_CLASS = "com.hiveworkshop.blizzard.blp.BLPReader";
	static final Class<?>[] INPUT_TYPES = { ImageInputStream.class, File.class,
			Path.class };
	static final String[] WRITER_SPI_CLASSES = { "com.hiveworkshop.blizzard.blp.BLPWriterSpi" };

	public BLPReaderSpi() {
		super(VENDOR, VERSION, FORMAT_NAMES, FORMAT_SUFFIXES, FORMAT_MIMES,
				READER_CLASS, INPUT_TYPES, WRITER_SPI_CLASSES,
				STANDARD_STREAM_METADATA_SUPPORT, NATIVE_STREAM_METADATA_NAME,
				NATIVE_STREAM_METADATA_CLASS, EXTRA_STREAM_METADATA_NAME,
				EXTRA_STREAM_METADATA_CLASS, STANDARD_IMAGE_METADATA_SUPPORT,
				NATIVE_IMAGE_METADATA_NAME, NATIVE_IMAGE_METADATA_CLASS,
				EXTRA_IMAGE_METADATA_NAME, EXTRA_IMAGE_METADATA_CLASS);
	}

	@Override
	public boolean canDecodeInput(Object source) throws IOException {
		if (source instanceof ImageInputStream) {
			// Record stream state.
			final ImageInputStream src = (ImageInputStream) source;
			src.mark();
			final ByteOrder order = src.getByteOrder();
			
			try {
				// Check magic number.
				src.setByteOrder(ByteOrder.LITTLE_ENDIAN);
				final MagicInt magic = new MagicInt(src.readInt(),
						ByteOrder.LITTLE_ENDIAN);
				if (BLPCommon.resolveVersion(magic) != -1)
					return true;
			} finally {
				// Restore stream.
				src.setByteOrder(order);
				src.reset();
			}

		}

		return false;
	}

	@Override
	public ImageReader createReaderInstance(Object extension) throws IOException {
		return new BLPReader(this);
	}

	@Override
	public String getDescription(Locale locale) {
		return "BLP file image reader.";
	}

}
