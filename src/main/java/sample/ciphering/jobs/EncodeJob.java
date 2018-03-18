package sample.ciphering.jobs;

import sample.ciphering.cipherers.Cipherer;

public class EncodeJob extends Job{

    public EncodeJob(Cipherer cipherer, byte[] data) {
        super(cipherer, data);
    }

    @Override
    protected byte[] perform() {
        return cipherer.encode(data);
    }
}
