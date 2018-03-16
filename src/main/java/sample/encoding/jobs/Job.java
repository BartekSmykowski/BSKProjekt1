package sample.encoding.jobs;

import sample.encoding.encoders.Encoder;

import java.util.concurrent.Callable;

public abstract class Job implements Callable<byte[]> {

    protected Encoder encoder;
    protected byte[] data;

    public Job(Encoder encoder, byte[] data){
        this.encoder = encoder;
        this.data = data;
    }

    @Override
    public byte[] call() throws Exception {
        return perform();
    }

    protected abstract byte[] perform();
}
