package com.example.trellomock;

import com.example.trellomock.member.Member;
import com.example.trellomock.member.MemberRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginController implements DialogController {

    private ScreensConfiguration screens;
    private FXMLDialog dialog;

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    public LoginController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Text incorrectLogin;

    @Autowired
    MemberRepository memberRepository;

    @FXML
    public void clickLoginButton(ActionEvent actionEvent) throws Exception {
        String email = String.valueOf(emailField.getText());
        String password = String.valueOf(passwordField.getText());
        Member member = memberRepository.findByEmail(email);

        if (member != null && member.getPassword().equals(password)) {
            emailField.setText("");
            passwordField.setText("");
            incorrectLogin.setText("");
            member.setLogged(true);
            memberRepository.save(member);
            System.out.println(member.getLogged());

            if (member.getAdminPrivileges()) {
                dialog.close();
                screens.adminDialog().show();
            }
            else {
                dialog.close();
                screens.boardDialog().show();
            }
        }
        else {
            incorrectLogin.setText("Incorrect email or password.");
            incorrectLogin.setFill(Color.RED);
        }
    }

    @FXML
    public void clickRegisterButton(ActionEvent actionEvent) throws Exception {
        dialog.close();
        screens.registerDialog().show();
    }

    @FXML
    public void onEnter(ActionEvent actionEvent) throws Exception {
        clickLoginButton(actionEvent);
    }
}