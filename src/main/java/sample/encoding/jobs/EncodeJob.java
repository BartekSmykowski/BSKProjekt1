package sample.encoding.jobs;

import sample.encoding.encoders.Encoder;

public class EncodeJob extends Job{

    public EncodeJob(Encoder encoder, byte[] data) {
        super(encoder, data);
    }

    @Override
    protected byte[] perform() {
        return encoder.encode(data);
    }
}
