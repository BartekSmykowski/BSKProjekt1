package sample.ciphering.jobs;

import sample.ciphering.cipherers.Cipherer;

public class DecodeJob extends Job {

    public DecodeJob(Cipherer cipherer, byte[] data){
        super(cipherer, data);
    }

    @Override
    protected byte[] perform() {
        return cipherer.decode(data);
    }
}
