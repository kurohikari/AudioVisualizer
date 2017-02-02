package FileExplorer;

import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Arthur Geneau on 2017-02-01.
 */
public class ExplorerStage {

    private Stage fileStage;
    private String homePath;
    File homeFile;

    public ExplorerStage() {
        this.fileStage = new Stage();
        this.homeFile = new File(System.getProperty("user.home"));
    }

    public Stage getFileStage() {
        return this.fileStage;
    }

}
