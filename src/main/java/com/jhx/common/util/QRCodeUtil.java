package com.jhx.common.util;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码
 */
public class QRCodeUtil {

	/**
	 * 生成二维码，默认大小:300*300, logo:60*60
	 * 
	 * @param stream 
	 * @param text
	 * @param logoUrl
	 * @throws IOException
	 * @throws WriterException
	 */
	public static void qrCodeWithLogo(OutputStream stream, String text, String logoUrl){
		qrCodeWithLogo(stream, text, logoUrl, 300, 60);
	}

	/**
	 * 生成二维码
	 * 
	 * @param stream 
	 * @param text
	 * @param logoUrl
	 * @param size
	 * @param logoSize
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void qrCodeWithLogo(OutputStream stream, String text, String logoUrl, int size, int logoSize){
		try {
			BitMatrix matrix = cteateBitMatrix(text, size);
			BufferedImage qrcode = toBufferedImage(matrix);

			Image logo = ImageIO.read(new URL(logoUrl));
			logo = logo.getScaledInstance(logoSize, logoSize, BufferedImage.SCALE_SMOOTH);

			int x = (size - logoSize) / 2;
			int y = (size - logoSize) / 2;
			Graphics2D graph = qrcode.createGraphics();
			graph.drawImage(logo, x, y, logoSize, logoSize, null);
			Shape shape = new RoundRectangle2D.Float(x, y, logoSize, logoSize, 6, 6);
			graph.setStroke(new BasicStroke(3f));
			graph.draw(shape);
			graph.dispose();

			ImageIO.write(qrcode, "png", stream);
			stream.close();
		} catch (WriterException | IOException e) {
			LogUtil.err(QRCodeUtil.class, "生成二维码失败");
		}
	}

	/**
	 * 生成二维码, 大小:300*300
	 * 
	 * @param stream 
	 * @param text
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void qrCode(OutputStream stream, String text){
		qrCode(stream, text, 300);
	}

	/**
	 * 生成二维码
	 * 
	 * @param stream 
	 * @param text
	 * @param size
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void qrCode(OutputStream stream, String text, int size){
		try {
			BitMatrix matrix = cteateBitMatrix(text, size);
			MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
			MatrixToImageWriter.writeToStream(matrix, "png", stream, config);
		} catch (WriterException | IOException e) {
			LogUtil.err(QRCodeUtil.class, "生成二维码失败");
		}
	}

	private static BitMatrix cteateBitMatrix(String text, int size) throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用编码
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 指定纠错等级
		hints.put(EncodeHintType.MARGIN, 0); // 白边大小，取值范围0~4
		BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
		return matrix;
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		return image;
	}
}
