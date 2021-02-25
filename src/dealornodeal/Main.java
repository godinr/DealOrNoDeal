package dealornodeal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private SplitPane splitpane;
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.root = FXMLLoader.load(getClass().getResource("/dealornodeal/GameView.fxml"));
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dealornodeal/GameView.fxml"));
        splitpane = (SplitPane) loader.load();
        UIController controller = loader.getController();

        controller.setMain(this);
        Scene scene = new Scene(splitpane);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Parent getRoot() {
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
