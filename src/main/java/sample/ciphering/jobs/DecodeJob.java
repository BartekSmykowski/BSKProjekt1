package sample.ciphering.jobs;

import sample.ciphering.cipherers.AES.AESCipherer;

public class DecodeJob extends Job {

    public DecodeJob(AESCipherer cipherer, byte[] data){
        super(cipherer, data);
    }

    @Override
    protected byte[] perform() {
        return cipherer.decode(data);
    }
}
