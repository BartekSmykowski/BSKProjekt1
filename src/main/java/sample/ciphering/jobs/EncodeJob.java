package sample.ciphering.jobs;

import sample.ciphering.cipherers.AES.AESCipherer;

public class EncodeJob extends CiphererJob {

    public EncodeJob(AESCipherer cipherer, String sourceFile, String destinationFile) {
        super(cipherer, sourceFile, destinationFile);
        appendToOutput = true;
    }

    @Override
    protected byte[] perform(byte[] dataBlock) {
        return cipherer.encode(dataBlock);
    }
}
