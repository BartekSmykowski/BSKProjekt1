package sample.encoding;

import sample.encoding.encoders.Encoder;
import sample.model.CipherModes;

import java.util.concurrent.Callable;

public class EncodingJob implements Callable<byte[]> {

    private Encoder encoder;
    private byte[] data;
    private CipherModes mode;

    public EncodingJob(Encoder encoder, byte[] data, CipherModes mode){
        this.encoder = encoder;
        this.data = data;
        this.mode = mode;
    }

    @Override
    public byte[] call() throws Exception {
        if(mode.equals(CipherModes.ENCRYPT))
            return encoder.encode(data);
        else
            return encoder.decode(data);
    }
}
