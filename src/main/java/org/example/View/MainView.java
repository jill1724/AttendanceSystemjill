package org.example.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainView extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private DefaultTableModel attendanceTableModel;
    private DefaultTableModel studentDirectoryModel;
    private JTable studentTable;

    private JLabel lblTotalValue;
    private JLabel lblPresentValue;
    private JLabel lblAbsentValue;

    public MainView() {
        setTitle("Attendance Management System - Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // --- HEADER BAR ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(192, 57, 43));
        headerPanel.setPreferredSize(new Dimension(1000, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("ATTENDANCE SYSTEM DASHBOARD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblWelcome = new JLabel("Welcome, Admin!");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWelcome.setForeground(Color.WHITE);
        headerPanel.add(lblWelcome, BorderLayout.EAST);

        // --- SIDEBAR MENU ---
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(108, 22, 22));
        sidebarPanel.setPreferredSize(new Dimension(240, 650));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnDashboard = createSidebarButton("📊 Dashboard Overview");
        JButton btnManageStudents = createSidebarButton("👥 Manage Students");
        JButton btnAttendanceLogs = createSidebarButton("📋 Attendance Logs");
        JButton btnLogout = createSidebarButton("🚪 Logout");

        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnManageStudents);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(btnAttendanceLogs);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(btnLogout);

        // --- MAIN CONTENT AREA ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setBackground(new Color(250, 242, 242));

        String[] columns = {"ID", "Student Number", "Student Name", "Status", "Date / Time"};
        attendanceTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JPanel dashboardScreen = createDashboardScreen();
        JPanel manageStudentsScreen = createManageStudentsScreen();
        JPanel attendanceLogsScreen = createAttendanceLogsScreen();

        contentPanel.add(dashboardScreen, "Dashboard");
        contentPanel.add(manageStudentsScreen, "ManageStudents");
        contentPanel.add(attendanceLogsScreen, "AttendanceLogs");

        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        btnDashboard.addActionListener(e -> cardLayout.show(contentPanel, "Dashboard"));
        btnManageStudents.addActionListener(e -> cardLayout.show(contentPanel, "ManageStudents"));
        btnAttendanceLogs.addActionListener(e -> cardLayout.show(contentPanel, "AttendanceLogs"));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                LoginView loginView = new LoginView(this, attendanceTableModel);
                loginView.setVisible(true);
            }
        });

        updateDashboardCounters();
    }

    public DefaultTableModel getAttendanceTableModel() {
        return this.attendanceTableModel;
    }

    public void updateDashboardCounters() {
        int totalRegistered = studentDirectoryModel.getRowCount();
        int totalPresent = attendanceTableModel.getRowCount();
        int totalAbsent = totalRegistered - totalPresent;
        if (totalAbsent < 0) totalAbsent = 0;

        if (lblTotalValue != null) lblTotalValue.setText("  " + totalRegistered);
        if (lblPresentValue != null) lblPresentValue.setText("  " + totalPresent);
        if (lblAbsentValue != null) lblAbsentValue.setText("  " + totalAbsent);
    }

    private JPanel createDashboardScreen() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(250, 242, 242));

        JLabel title = new JLabel("System Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(108, 22, 22));
        panel.add(title, BorderLayout.NORTH);

        JPanel cardsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsGrid.setBackground(new Color(250, 242, 242));

        lblTotalValue = new JLabel("  0");
        lblPresentValue = new JLabel("  0");
        lblAbsentValue = new JLabel("  0");

        cardsGrid.add(createInfoCard("Total Registered Students", lblTotalValue, new Color(192, 57, 43)));
        cardsGrid.add(createInfoCard("Present Today", lblPresentValue, new Color(214, 48, 49)));
        cardsGrid.add(createInfoCard("Absent Today", lblAbsentValue, new Color(150, 40, 27)));

        panel.add(cardsGrid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createManageStudentsScreen() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(new Color(250, 242, 242));

        JLabel title = new JLabel("Manage Student Directory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(108, 22, 22));
        panel.add(title, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionPanel.setBackground(new Color(250, 242, 242));

        JButton btnAdd = createStyledButton("+ Add New Student", new Color(39, 174, 96));
        JButton btnRemove = createStyledButton("🗑️ Remove Selected", new Color(214, 48, 49));
        actionPanel.add(btnAdd);
        actionPanel.add(btnRemove);

        String[] studentColumns = {"Student Number", "Full Name", "Course & Section", "Status"};
        studentDirectoryModel = new DefaultTableModel(studentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(studentDirectoryModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);

        studentTable.getTableHeader().setBackground(new Color(192, 57, 43));
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        studentTable.setGridColor(new Color(242, 215, 213));

        studentDirectoryModel.addRow(new Object[]{"2023-00123-SR-0", "Ezrah simone Dela Cruz", "BSIT 3-2", "Active"});
        studentDirectoryModel.addRow(new Object[]{"2023-00456-SR-0", "Jeymi Bato", "BSIT 3-2", "Active"});
        studentDirectoryModel.addRow(new Object[]{"2023-00789-SR-0", "Shaine Ashly Simon", "BSIT 3-2", "Active"});

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(actionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            JTextField txtStudNum = new JTextField();
            JTextField txtFullName = new JTextField();
            JTextField txtCourse = new JTextField();
            Object[] message = {"Student Number:", txtStudNum, "Full Name:", txtFullName, "Course & Section:", txtCourse};

            int option = JOptionPane.showConfirmDialog(this, message, "Register Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                if (!txtStudNum.getText().isEmpty() && !txtFullName.getText().isEmpty()) {
                    studentDirectoryModel.addRow(new Object[]{txtStudNum.getText().trim(), txtFullName.getText().trim(), txtCourse.getText().trim(), "Active"});
                    updateDashboardCounters();
                }
            }
        });

        btnRemove.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                studentDirectoryModel.removeRow(row);
                updateDashboardCounters();
            }
        });

        return panel;
    }

    // --- SCREEN 3: ATTENDANCE LOGS WITH DEDICATED "+ ADD ATTENDANCE" ---
    private JPanel createAttendanceLogsScreen() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(new Color(250, 242, 242));

        JLabel title = new JLabel("Master Attendance Logs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(108, 22, 22));
        panel.add(title, BorderLayout.NORTH);

        // DEDICATED ACTION BUTTON PANELS FOR THE MASTER LOG
        JPanel logActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        logActionPanel.setBackground(new Color(250, 242, 242));

        JButton btnAddAttendance = createStyledButton("+ Add Attendance", new Color(39, 174, 96));
        logActionPanel.add(btnAddAttendance);

        JTable table = new JTable(attendanceTableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(new Color(192, 57, 43));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setGridColor(new Color(242, 215, 213));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(logActionPanel, BorderLayout.NORTH); // Isinama ang button sa unahan ng table
        panel.add(scroll, BorderLayout.CENTER);

        // ACTION LISTENER FOR MANUAL MASTER LOG INSERTION
        btnAddAttendance.addActionListener(e -> {
            JTextField txtStudNum = new JTextField();
            JTextField txtFullName = new JTextField();
            Object[] message = {
                    "Student Number:", txtStudNum,
                    "Student Full Name:", txtFullName
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Manual Attendance Entry", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String sNum = txtStudNum.getText().trim();
                String sName = txtFullName.getText().trim();

                if (sNum.isEmpty() || sName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Automatic real-time tracking string generator
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");

                int nextId = attendanceTableModel.getRowCount() + 1;
                attendanceTableModel.addRow(new Object[]{nextId, sNum, sName, "PRESENT", now.format(formatter)});
                updateDashboardCounters(); // I-refresh ang real-time card parameters
            }
        });

        return panel;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(220, 45));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(139, 0, 0));
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.setBackground(new Color(192, 57, 43)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.setBackground(new Color(139, 0, 0)); }
        });
        return button;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createInfoCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 7, 0, 0, color));

        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(200, 100));
        return card;
    }
}