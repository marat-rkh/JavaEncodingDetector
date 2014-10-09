package encoding;

import java.nio.charset.Charset;

/**
 * Created by mrx on 08.10.14.
 */
public class DetectionResult {
    private final Charset charset;
    private final double confidence;

    public DetectionResult(Charset charset, double confidence) {
        this.charset = charset;
        this.confidence = confidence;
    }

    public Charset getCharset() {
        return charset;
    }

    public double getConfidence() {
        return confidence;
    }
}
