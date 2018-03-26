package sample.ciphering.jobs;

import sample.ciphering.cipherers.AES.AESCipherer;

public class DecodeJob extends CiphererJob {

    public DecodeJob(AESCipherer cipherer, String sourceFile, String destinationFile) {
        super(cipherer, sourceFile, destinationFile);
        appendToOutput = false;
    }

    @Override
    protected byte[] perform(byte[] dataBlock) {
        return cipherer.decode(dataBlock);
    }
}
