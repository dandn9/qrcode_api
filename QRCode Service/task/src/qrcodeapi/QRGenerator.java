package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class QRGenerator {
    private int size;
    private BufferedImage image;
    private MediaType type;


    public QRGenerator(Integer size, String mediaType, String contents, Character correction) throws IllegalArgumentException {
        // Validation
        if (contents == null || contents.isBlank()) {
            throw new IllegalArgumentException("Contents cannot be null or blank");
        }
        if (size < 150 || size > 350) {
            throw new IllegalArgumentException("Image size must be between 150 and 350 pixels");
        }

        ErrorCorrectionLevel errorCorrectionLevel = null;

        switch (correction) {
            case 'L' -> errorCorrectionLevel = ErrorCorrectionLevel.L;
            case 'M' -> errorCorrectionLevel = ErrorCorrectionLevel.M;
            case 'Q' -> errorCorrectionLevel = ErrorCorrectionLevel.Q;
            case 'H' -> errorCorrectionLevel = ErrorCorrectionLevel.H;
            default -> throw new IllegalArgumentException("Permitted error correction levels are L, M, Q, H");
        }

        switch (mediaType) {
            case "png" -> this.type = MediaType.IMAGE_PNG;
            case "jpeg" -> this.type = MediaType.IMAGE_JPEG;
            case "gif" -> this.type = MediaType.IMAGE_GIF;
            default -> throw new IllegalArgumentException("Only png, jpeg and gif image types are supported");

        }


        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        try {
            BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            this.image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            System.err.println("Failed to encode - " + e);
        }


    }


    public int getSize() {
        return size;
    }

    public BufferedImage getImage() {
        return image;
    }

    public MediaType getType() {
        return type;
    }
}
