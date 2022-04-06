
import ij.ImagePlus;

import ij.gui.GenericDialog;

import ij.plugin.filter.PlugInFilter;

import ij.process.ImageProcessor;

import net.imglib2.Cursor;

import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.ImagePlusAdapter;

import net.imglib2.img.display.imagej.ImageJFunctions;

import net.imglib2.type.NativeType;

import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class UInt24toUInt12Converter_PlugIn<T extends RealType<T> & NativeType<T>> implements PlugInFilter {
	protected ImagePlus image;

	/**
	 * @see ij.plugin.filter.PlugInFilter#setup(java.lang.String, ij.ImagePlus)
	 */
	@Override
	public int setup(String arg, ImagePlus imp) {
		image = imp;
		return DOES_32 | DOES_RGB;
	}

	/**
	 * @see ij.plugin.filter.PlugInFilter#run(ij.process.ImageProcessor)
	 */
	@Override
	public void run(ImageProcessor ip) {
		run(image);
		//image.updateAndDraw();
	}

	/**
	 * This should have been the method declared in PlugInFilter...
	 */
	public void run(ImagePlus image) {
		Img<UnsignedIntType> img = ImagePlusAdapter.wrapInt(image);
		Img<UnsignedShortType> output = extract_uint12s(img);
		ImageJFunctions.show( output );
	}

	/**
	 * The actual processing is done here.
	 */
	public static Img<UnsignedShortType> extract_uint12s(Img<UnsignedIntType> img) {
		final Img<UnsignedShortType> output;
		final Cursor<UnsignedShortType> outputCursor;
		final Cursor<UnsignedIntType> cursor = img.cursor();
		long[] dims = img.dimensionsAsLongArray();
		
		int current;
		
		dims[0] *= 2;
		output =  ArrayImgs.unsignedShorts(dims);
		outputCursor = output.cursor();

		while (cursor.hasNext()) {
			cursor.fwd();
			current = cursor.get().getInt();
			outputCursor.fwd();
			outputCursor.get().set(current & 0xfff);
			outputCursor.fwd();
			outputCursor.get().set(current >> 12);
		}
		
		return output;
	}
}
