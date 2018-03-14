package sample.encoding;

import sample.model.EncodingData;

public class ECBEncoderJob extends EncoderJob {

    public ECBEncoderJob(EncodingData encodingData) {
        super(encodingData);
    }

    @Override
    public void doStep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
