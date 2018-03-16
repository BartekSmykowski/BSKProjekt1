package sample.encoding.jobs;

import sample.encoding.encoders.Encoder;

public class DecodeJob extends Job {

    public DecodeJob(Encoder encoder, byte[] data){
        super(encoder, data);
    }

    @Override
    protected byte[] perform() {
        return encoder.decode(data);
    }
}
