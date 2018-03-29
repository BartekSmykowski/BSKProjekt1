package sample.ciphering.cipherManagers;

import sample.ciphering.jobs.CipherJobExecutor;

public interface CipherManager {
    void performJob();
    CipherJobExecutor getJobExecutor();
    String getSourcePath();
    String getDestinationPath();
    int getFileSize();
}
