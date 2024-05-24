import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class StudentRegistration extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/students_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;
    private static PreparedStatement preparedStatement;

    private JTextField numberField, nameField , titleField, searchField;
    private JComboBox<String> genderComboBox, classComboBox;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel imageLabel;
    private byte[] photo;

    public StudentRegistration() {
        // Create login dialog
        if (!showLoginDialog()) {
            // Close the application if login fails
            System.exit(0);
        }

        titleField = new JTextField("Gestion Etudiant en department TI");
        numberField = new JTextField(10);
        nameField = new JTextField(10);
        searchField = new JTextField(10);

        String[] genders = {"Masculin", "Feminin"};
        genderComboBox = new JComboBox<>(genders);
        String[] classes = {"DSI", "MWD", "RSI"};
        classComboBox = new JComboBox<>(classes);

        JButton registerButton = new JButton("ENREGISTRER");
        JButton deleteButton = new JButton("SUPPRIMER");
        JButton selectImageButton = new JButton("Sélectionner Image");
        JButton searchButton = new JButton("Rechercher par ID");

        tableModel = new DefaultTableModel(new String[]{"Code", "Nom", "Sexe", "Classe", "Photo"}, 0);
        studentTable = new JTable(tableModel);
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));

        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("NUMERO ELEVE"), gbc);
        gbc.gridx = 1;
        formPanel.add(numberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("NOM ET PRENOM"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("SEXE"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("CLASSE"), gbc);
        gbc.gridx = 1;
        formPanel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(selectImageButton, gbc);
        gbc.gridx = 1;
        formPanel.add(imageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(registerButton, gbc);
        gbc.gridx = 1;
        formPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Rechercher par ID: "), gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(searchField, gbc);
        gbc.gridx = 3;
        formPanel.add(searchButton, gbc);

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudentByID();
            }
        });

        initializeJdbc();
        loadStudents();

        setTitle("Enregistrement Des Clients");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setVisible(true);
    }

    private boolean showLoginDialog() {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            // Check if username and password match
            if (username.equals("admin@admin") && password.equals("admin")) {
                return true; // Successful login
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                return false; // Failed login
            }
        } else {
            return false; // User clicked cancel or closed the dialog
        }
    }

    private void initializeJdbc() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
    }

    private void registerStudent() {
        String number = numberField.getText();
        String name = nameField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String studentClass = (String) classComboBox.getSelectedItem();

        try {
            String query = "INSERT INTO students (code_eleve, nom, sexe, classe, photo) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, studentClass);
            preparedStatement.setBytes(5, photo);
            preparedStatement.executeUpdate();

            loadStudents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int studentId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                String query = "DELETE FROM students WHERE code_eleve = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, studentId);
                preparedStatement.executeUpdate();

                loadStudents();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadStudents() {
        try {
            String query = "SELECT * FROM students";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            tableModel.setRowCount(0);
            while (resultSet.next()) {
                int id = resultSet.getInt("code_eleve");
                String name = resultSet.getString("nom");
                String gender = resultSet.getString("sexe");
                String studentClass = resultSet.getString("classe");
                byte[] photoBytes = resultSet.getBytes("photo");

                ImageIcon photoIcon = null;
                if (photoBytes != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(photoBytes);
                    BufferedImage bufferedImage = ImageIO.read(bais);
                    photoIcon = new ImageIcon(bufferedImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                }

                tableModel.addRow(new Object[]{id, name, gender, studentClass, photoIcon});
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void searchStudentByID() {
        String searchID = searchField.getText().trim();
        if (!searchID.isEmpty()) {
            try {
                int studentID = Integer.parseInt(searchID);
                String query = "SELECT * FROM students WHERE code_eleve = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, studentID);
                ResultSet resultSet = preparedStatement.executeQuery();

                tableModel.setRowCount(0);
                while (resultSet.next()) {
                    int id = resultSet.getInt("code_eleve");
                    String name = resultSet.getString("nom");
                    String gender = resultSet.getString("sexe");
                    String studentClass = resultSet.getString("classe");
                    byte[] photoBytes = resultSet.getBytes("photo");

                    ImageIcon photoIcon = null;
                    if (photoBytes != null) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(photoBytes);
                        BufferedImage bufferedImage = ImageIO.read(bais);
                        photoIcon = new ImageIcon(bufferedImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    }

                    tableModel.addRow(new Object[]{id, name, gender, studentClass, photoIcon});
                }
            } catch (NumberFormatException | SQLException | IOException e) {
                e.printStackTrace();
            }
        } else {
            loadStudents(); // If search field is empty, reload all students
        }
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileInputStream fis = new FileInputStream(selectedFile);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                photo = baos.toByteArray();
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(photo).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new StudentRegistration();
    }
}

