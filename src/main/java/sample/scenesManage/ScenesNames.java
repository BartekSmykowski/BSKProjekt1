package sample.scenesManage;

public enum ScenesNames {
    DECODING, ENCODING, MENU, REGISTER, ENCODING_PROGRESS;

    public static String getFileName(ScenesNames name){
        if(name.equals(DECODING)){
            return "decodingScene.fxml";
        } else if(name.equals(ENCODING)){
            return "encodingScene.fxml";
        } else if(name.equals(MENU)){
            return "mainMenu.fxml";
        } else if(name.equals(REGISTER)){
            return "registerScene.fxml";
        } else if(name.equals(ENCODING_PROGRESS)){
            return "encodingProgressScene.fxml";
        }

        throw new NoSuchSceneException(name);
    }


}
