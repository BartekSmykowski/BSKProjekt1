package sample.scenesManage;

public class NoSuchSceneException extends RuntimeException {
    public NoSuchSceneException(ScenesNames name) {
        super(name.toString());
    }
}
