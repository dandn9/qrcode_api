package qrcodeapi;

public class QRException {
    private String error;

    QRException(String errorMessage) {
        this.error = errorMessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}