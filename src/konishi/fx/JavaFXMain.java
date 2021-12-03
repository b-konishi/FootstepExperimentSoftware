package konishi.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXMain extends Application {

    public static void main(String[] args) {
        // JavaFX の実行
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
    	stage.setTitle("RecognitionTest by Human");
    	stage.setResizable(false);
    	
        // hello.fxml の読み込み
        Parent root = FXMLLoader.load(getClass().getResource("humanRecognitionTester.fxml"));

        // Scene の作成・登録
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // 表示
        stage.show();
    }
    @Override
	public void stop() {
		System.exit(0);
	}
}