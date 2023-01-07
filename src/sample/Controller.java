package sample;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private Button btn_submit;

    @FXML
    private Label label_banner;

    @FXML
    private Label label_msg;

    @FXML
    private Label label_password;

    @FXML
    private Label label_username;

    @FXML
    public Label label_zoneID;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_username;

    public void determinLang() {
        String userLanguage = System.getProperty("user.language");
        System.out.println(userLanguage);

        if (userLanguage == "fr") {
            //translate to french
            label_zoneID.setText("ID de zone : France");
            label_password.setText("Mot de passe");
            label_username.setText("Nom d'utilisateur");
            btn_submit.setText("Identifiant");
            label_banner.setText("Identifiant");
        }
        else {
            label_zoneID.setText("Zone ID : America");
        }
    }

    public void login() {
        String username = tf_username.getText();
        String password = tf_password.getText();

        System.out.println(username + "  " + password);
    }

    @FXML public void initialize() {
        System.out.println("INIT");
        determinLang();
    }

}
