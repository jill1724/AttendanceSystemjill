package org.example.main;

import javax.swing.*;
import org.example.View.MainView;
import org.example.View.LoginView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            MainView dashboard = new MainView();


            LoginView loginView = new LoginView(dashboard, dashboard.getAttendanceTableModel());


            loginView.setVisible(true);
        });
    }
}