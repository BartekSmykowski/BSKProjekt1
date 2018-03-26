package sample.ciphering.jobs;

import sample.ciphering.cipherers.AES.AESCipherer;

import java.util.concurrent.Callable;

public abstract class Job implements Callable<byte[]> {

    protected AESCipherer cipherer;
    protected byte[] data;

    public Job(AESCipherer cipherer, byte[] data){
        this.cipherer = cipherer;
        this.data = data;
    }

    @Override
    public byte[] call() throws Exception {
        return perform();
    }

    protected abstract byte[] perform();
}
