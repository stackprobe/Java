package charlotte.tools;

public class PngData {
	private byte[] _imageData;
	private int _w;
	private int _h;

	public PngData(byte[] imageData) throws Exception {
		this(Bmp.getBmp(imageData));
	}

	public PngData(Bmp bmp) throws Exception {
		_imageData = bmp.getBytes();
		_w = bmp.getWidth();
		_h = bmp.getHeight();
	}

	public byte[] getBytes() {
		return _imageData;
	}

	public int getWidth() {
		return _w;
	}

	public int getHeight() {
		return _h;
	}
}
