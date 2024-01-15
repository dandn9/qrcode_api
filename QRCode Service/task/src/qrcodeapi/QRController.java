package qrcodeapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.awt.desktop.QuitResponse;
import java.awt.image.BufferedImage;

@RestController
public class QRController {
    @GetMapping("/api/health")
    public ResponseEntity health() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity qrcode(@RequestParam(required = false) Integer size,
                                 @RequestParam(required = false) String type,
                                 @RequestParam(required = true) String contents,
                                 @RequestParam(required = false) Character correction
    ) {
        if (size == null) {
            size = 250;
        }
        if (correction == null) {
            correction = 'L';
        }
        if (type == null) {
            type = "png";
        }
        try {
            QRGenerator qrCode = new QRGenerator(size, type, contents, correction);
            return ResponseEntity.ok().contentType(qrCode.getType()).body(qrCode.getImage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new QRException(ex.getMessage()));
        }

    }
}
