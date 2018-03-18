package sample.ciphering.jobs;

import sample.ciphering.cipherers.Cipherer;

import java.util.concurrent.Callable;

public abstract class Job implements Callable<byte[]> {

    protected Cipherer cipherer;
    protected byte[] data;

    public Job(Cipherer cipherer, byte[] data){
        this.cipherer = cipherer;
        this.data = data;
    }

    @Override
    public byte[] call() throws Exception {
        return perform();
    }

    protected abstract byte[] perform();
}
