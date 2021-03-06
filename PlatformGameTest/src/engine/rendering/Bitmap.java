package engine.rendering;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bitmap {
	private final IRenderDevice device;
	private final int width;
	private final int height;
	private int id;

	public Bitmap(IRenderDevice device, int width, int height, int[] pixels) {
		this.device = device;
		this.width = width;
		this.height = height;
		this.id = device.createTexture(width, height, pixels,
				IRenderDevice.FILTER_NEAREST);
	}

	public Bitmap(IRenderDevice device, String fileName) throws IOException {
		this.device = device;
		BufferedImage image;
		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			throw new IOException(fileName + " couldn't be loaded");
		}

		this.width = image.getWidth();
		this.height = image.getHeight();

		int imgPixels[] = new int[width * height];
		image.getRGB(0, 0, width, height, imgPixels, 0, width);
		this.id = device.createTexture(width, height, imgPixels,
				IRenderDevice.FILTER_NEAREST);
	}

	public void dispose() {
		id = device.releaseTexture(id);
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	public int getDeviceID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getPixels(int[] dest) {
		return getPixels(dest, 0, 0, width, height);
	}

	public int[] getPixels(int[] dest, int x, int y, int width, int height) {
		return device.getTexture(id, dest, x, y, width, height);
	}

	public void save(String filetype, File file) throws IOException {
		BufferedImage output = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int[] displayComponents = ((DataBufferInt) output.getRaster()
				.getDataBuffer()).getData();
		device.getTexture(id, displayComponents, 0, 0, width, height);

		ImageIO.write(output, "png", file);
	}
}
