package org.example.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginView extends JFrame {

    private final JTextField txtName;
    private final JTextField txtStudentNumber;
    private final JButton btnSubmit;

    private final JTable historyTable;
    private final DefaultTableModel sharedTableModel;
    private final MainView mainFrameContext;

    public LoginView(MainView mainFrame, DefaultTableModel sharedModel) {
        this.mainFrameContext = mainFrame;
        this.sharedTableModel = sharedModel;

        setTitle("Attendance Management System - Portal");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 242, 242));

        // --- HEADER BAR ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(192, 57, 43));
        headerPanel.setPreferredSize(new Dimension(900, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblHeaderTitle = new JLabel("STUDENT ATTENDANCE PORTAL");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeaderTitle.setForeground(Color.WHITE);
        headerPanel.add(lblHeaderTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- WORKSPACE CONTAINER ---
        JPanel workspacePanel = new JPanel(new BorderLayout(25, 25));
        workspacePanel.setBackground(new Color(250, 242, 242));
        workspacePanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // =========================================================================
        // --- 2. KALIWANG PANEL: INPUT FORM CONTAINER (ANG LIHIM SA PAG-AYOS) ---
        // Gagamit tayo ng BoxLayout (Y_AXIS) para hindi ma-stretch ang taas ng fields
        // =========================================================================
        JPanel leftPanelWrapper = new JPanel();
        leftPanelWrapper.setLayout(new BoxLayout(leftPanelWrapper, BoxLayout.Y_AXIS));
        leftPanelWrapper.setBackground(new Color(250, 242, 242));
        leftPanelWrapper.setPreferredSize(new Dimension(340, 400));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color textRedColor = new Color(150, 40, 27);

        // Full Name Field Setup
        JLabel lblName = new JLabel("Full Name:");
        lblName.setFont(labelFont);
        lblName.setForeground(textRedColor);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBorder(BorderFactory.createLineBorder(new Color(211, 84, 0), 1));
        txtName.setMaximumSize(new Dimension(340, 35)); // Nilimitahan ang taas sa 35 pixels para maging square/rectangular strip
        txtName.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Student Number Field Setup
        JLabel lblStudNum = new JLabel("Student Number:");
        lblStudNum.setFont(labelFont);
        lblStudNum.setForeground(textRedColor);
        lblStudNum.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtStudentNumber = new JTextField();
        txtStudentNumber.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtStudentNumber.setBorder(BorderFactory.createLineBorder(new Color(211, 84, 0), 1));
        txtStudentNumber.setMaximumSize(new Dimension(340, 35)); // 35 pixels limit
        txtStudentNumber.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Submit Button Setup
        btnSubmit = new JButton("Submit Attendance");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBackground(new Color(214, 48, 49));
        btnSubmit.setFocusable(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setMaximumSize(new Dimension(340, 40)); // 40 pixels max height para malinis tingnan
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btnSubmit.setBackground(new Color(192, 57, 43)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btnSubmit.setBackground(new Color(214, 48, 49)); }
        });

        // Pagsasamahin ang mga fields sa Left Wrapper na may Rigid Space gaps
        leftPanelWrapper.add(Box.createRigidArea(new Dimension(0, 10))); // Top padding
        leftPanelWrapper.add(lblName);
        leftPanelWrapper.add(Box.createRigidArea(new Dimension(0, 8)));  // Gap sa pagitan ng label at textfield
        leftPanelWrapper.add(txtName);
        leftPanelWrapper.add(Box.createRigidArea(new Dimension(0, 20))); // Gap sa pagitan ng magkaibang fields
        leftPanelWrapper.add(lblStudNum);
        leftPanelWrapper.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanelWrapper.add(txtStudentNumber);
        leftPanelWrapper.add(Box.createRigidArea(new Dimension(0, 25)));
        leftPanelWrapper.add(btnSubmit);

        // --- 3. KANANG PANEL: HISTORY LOG ---
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(214, 48, 49), 1),
                "Live Attendance History Log (Today)",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(192, 57, 43)
        ));

        historyTable = new JTable(sharedTableModel);
        historyTable.getTableHeader().setReorderingAllowed(false);
        historyTable.getTableHeader().setBackground(new Color(214, 48, 49));
        historyTable.getTableHeader().setForeground(Color.WHITE);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        historyTable.setGridColor(new Color(242, 215, 213));

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        // Pagsamahin sa Main Layout panel
        workspacePanel.add(leftPanelWrapper, BorderLayout.WEST);
        workspacePanel.add(historyPanel, BorderLayout.CENTER);

        add(workspacePanel, BorderLayout.CENTER);
        btnSubmit.addActionListener(this::handleAttendanceSubmit);
    }

    private void handleAttendanceSubmit(ActionEvent e) {
        String name = txtName.getText().trim();
        String studentNum = txtStudentNumber.getText().trim();

        if (name.isEmpty() || studentNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        String currentTime = now.format(formatter);

        int nextId = sharedTableModel.getRowCount() + 1;
        Object[] rowData = {nextId, studentNum, name, "PRESENT", currentTime};

        sharedTableModel.addRow(rowData);

        JOptionPane.showMessageDialog(this, "Attendance recorded successfully for:\n" + name, "Success", JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
        mainFrameContext.updateDashboardCounters();
        mainFrameContext.setVisible(true);
    }
}