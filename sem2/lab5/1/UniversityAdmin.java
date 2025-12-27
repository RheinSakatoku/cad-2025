import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UniversityAdmin {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Вход в систему");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(400, 250);
            loginFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);

            usernameField.setText("moderator");
            passwordField.setText("123");

            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Логин:"), gbc);
            gbc.gridx = 1;
            panel.add(usernameField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Пароль:"), gbc);
            gbc.gridx = 1;
            panel.add(passwordField, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            JButton loginButton = new JButton("Войти");
            panel.add(loginButton, gbc);

            loginFrame.add(panel);
            loginFrame.setVisible(true);

            loginButton.addActionListener(e -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (testConnection(username, password)) {
                    loginFrame.dispose();
                    showMainWindow(username, password);
                } else {
                    JOptionPane.showMessageDialog(loginFrame,
                            "Неверный логин/пароль или сервер недоступен",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        });
    }

    private static boolean testConnection(String username, String password) {
        try {
            String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            URL url = new URL("http://localhost:8080/api/moderator/all-requests");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", authHeader);
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            conn.disconnect();
            return code == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static void showMainWindow(String username, String password) {
        JFrame mainFrame = new JFrame("Панель модератора - Университет");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1300, 750);
        mainFrame.setLocationRelativeTo(null);

        ModeratorPanel moderatorPanel = new ModeratorPanel(username, password);
        mainFrame.add(moderatorPanel);
        mainFrame.setVisible(true);
    }
}

class ModeratorPanel extends JPanel {
    private String authHeader;
    private JTable requestsTable, scheduleTable;
    private DefaultTableModel requestsModel, scheduleModel;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;

    public ModeratorPanel(String username, String password) {
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        setupUI();
        loadAllData();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Панель статуса
        statusLabel = new JLabel("Готово");
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);
        
        // Панель с вкладками
        tabbedPane = new JTabbedPane();
        
        // ВКЛАДКА 1: Запросы на изменения
        JPanel requestsPanel = new JPanel(new BorderLayout());
        
        // Заголовки для таблицы запросов
        String[] requestColumns = {"ID", "Преподаватель", "Группа", "Предмет", 
                                   "Текущая дата", "Запрашиваемая дата", "Причина", "Статус"};
        requestsModel = new DefaultTableModel(requestColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        requestsTable = new JTable(requestsModel);
        requestsTable.setRowHeight(25);
        requestsTable.setAutoCreateRowSorter(true);
        
        // Настройка цвета строк по статусу
        requestsTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) table.getValueAt(row, 7);
                if ("PENDING".equals(status)) {
                    c.setBackground(new Color(255, 243, 205)); // светло-желтый
                } else if ("APPROVED".equals(status)) {
                    c.setBackground(new Color(212, 237, 218)); // светло-зеленый
                } else if ("REJECTED".equals(status)) {
                    c.setBackground(new Color(248, 215, 218)); // светло-красный
                } else {
                    c.setBackground(Color.WHITE);
                }
                if (isSelected) {
                    c.setBackground(new Color(200, 200, 255)); // цвет выделения
                }
                return c;
            }
        });
        
        requestsPanel.add(new JScrollPane(requestsTable), BorderLayout.CENTER);
        
        // Панель кнопок для запросов
        JPanel requestButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton refreshRequestsBtn = new JButton("Обновить запросы");
        JButton approveBtn = new JButton("Одобрить");
        JButton rejectBtn = new JButton("Отклонить");
        JButton viewCommentBtn = new JButton("Просмотр комментария");
        
        approveBtn.setEnabled(false);
        rejectBtn.setEnabled(false);
        viewCommentBtn.setEnabled(false);
        
        requestButtonsPanel.add(refreshRequestsBtn);
        requestButtonsPanel.add(Box.createHorizontalStrut(20));
        requestButtonsPanel.add(approveBtn);
        requestButtonsPanel.add(rejectBtn);
        requestButtonsPanel.add(Box.createHorizontalStrut(20));
        requestButtonsPanel.add(viewCommentBtn);
        
        requestsPanel.add(requestButtonsPanel, BorderLayout.SOUTH);
        
        // ВКЛАДКА 2: Расписание
        JPanel schedulePanel = new JPanel(new BorderLayout());
        
        // Заголовки для таблицы расписания
        String[] scheduleColumns = {"ID", "Дата", "Время", "Предмет", 
                                    "Преподаватель", "Группа", "Аудитория"};
        scheduleModel = new DefaultTableModel(scheduleColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        scheduleTable = new JTable(scheduleModel);
        scheduleTable.setRowHeight(25);
        scheduleTable.setAutoCreateRowSorter(true);
        
        schedulePanel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        
        // Панель кнопок для расписания
        JPanel scheduleButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton refreshScheduleBtn = new JButton("Обновить расписание");
        scheduleButtonsPanel.add(refreshScheduleBtn);
        schedulePanel.add(scheduleButtonsPanel, BorderLayout.SOUTH);
        
        // Добавляем вкладки
        tabbedPane.addTab("Запросы на изменения", requestsPanel);
        tabbedPane.addTab("Расписание", schedulePanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Обработчики событий
        refreshRequestsBtn.addActionListener(e -> loadRequests());
        refreshScheduleBtn.addActionListener(e -> loadSchedule());
        
        approveBtn.addActionListener(e -> processRequest("approve"));
        rejectBtn.addActionListener(e -> processRequest("reject"));
        
        viewCommentBtn.addActionListener(e -> showComment());
        
        // Выбор строки в таблице запросов
        requestsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = requestsTable.getSelectedRow();
                boolean hasSelection = selectedRow >= 0;
                approveBtn.setEnabled(hasSelection);
                rejectBtn.setEnabled(hasSelection);
                viewCommentBtn.setEnabled(hasSelection);
            }
        });
    }
    
    private void loadAllData() {
        loadRequests();
        loadSchedule();
    }
    
    private void loadRequests() {
        statusLabel.setText("Загрузка запросов...");
        new Thread(() -> {
            try {
                String json = fetchData("http://localhost:8080/api/moderator/all-requests");
                SwingUtilities.invokeLater(() -> {
                    updateRequestsTable(json);
                    statusLabel.setText("Запросы загружены");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Ошибка загрузки запросов");
                    JOptionPane.showMessageDialog(this,
                            "Ошибка загрузки запросов: " + e.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
    
    private void loadSchedule() {
        statusLabel.setText("Загрузка расписания...");
        new Thread(() -> {
            try {
                String json = fetchData("http://localhost:8080/api/moderator/all-schedules");
                SwingUtilities.invokeLater(() -> {
                    updateScheduleTable(json);
                    statusLabel.setText("Расписание загружено");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Ошибка загрузки расписания");
                    JOptionPane.showMessageDialog(this,
                            "Ошибка загрузки расписания: " + e.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
    
    private String fetchData(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", authHeader);
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new Exception("HTTP ошибка: " + code);
        }

        BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
    
    private void updateRequestsTable(String json) {
        requestsModel.setRowCount(0);
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return;
        }

        try {
            json = json.trim();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1);
            }
            
            String[] objects = json.contains("},{") ? json.split("\\},\\{") : new String[]{json};
            
            for (int i = 0; i < objects.length; i++) {
                String obj = objects[i];
                if (i == 0 && obj.startsWith("{")) obj = obj.substring(1);
                if (i == objects.length - 1 && obj.endsWith("}")) obj = obj.substring(0, obj.length() - 1);
                
                String id = getJsonValue(obj, "id");
                String teacher = getJsonValue(obj, "teacherName");
                String group = getJsonValue(obj, "groupName");
                String subject = getJsonValue(obj, "subject");
                String currentDate = getJsonValue(obj, "currentDateTime");
                String newDate = getJsonValue(obj, "requestedDateTime");
                String reason = getJsonValue(obj, "reason");
                String status = getJsonValue(obj, "status");
                
                // Форматирование дат
                if (currentDate.length() > 10) currentDate = currentDate.substring(0, 10);
                if (newDate.length() > 10) newDate = newDate.substring(0, 10);
                
                requestsModel.addRow(new Object[]{id, teacher, group, subject, 
                                                  currentDate, newDate, reason, status});
            }
            
            tabbedPane.setTitleAt(0, "Запросы на изменения (" + objects.length + ")");
        } catch (Exception e) {
            statusLabel.setText("Ошибка парсинга запросов: " + e.getMessage());
        }
    }
    
    private void updateScheduleTable(String json) {
        scheduleModel.setRowCount(0);
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return;
        }

        try {
            json = json.trim().substring(1, json.length() - 1);
            
            String[] objects = json.split("\\},\\{");
            
            for (int i = 0; i < objects.length; i++) {
                String obj = objects[i];
                if (i == 0) obj = obj.substring(1);
                if (i == objects.length - 1) obj = obj.substring(0, obj.length() - 1);
                
                String id = getJsonValue(obj, "id");
                String date = getJsonValue(obj, "date");
                String startTime = getJsonValue(obj, "startTime");
                String endTime = getJsonValue(obj, "endTime");
                String subject = getJsonValue(obj, "subject");
                String teacher = getJsonValue(obj, "teacherName");
                String group = getJsonValue(obj, "groupName");
                String room = getJsonValue(obj, "room");
                
                // Форматирование времени
                String time = startTime;
                if (endTime != null && !endTime.isEmpty()) {
                    time = startTime + " - " + endTime;
                }
                
                scheduleModel.addRow(new Object[]{id, date, time, subject, teacher, group, room});
            }
            
            tabbedPane.setTitleAt(1, "Расписание (" + objects.length + ")");
        } catch (Exception e) {
            statusLabel.setText("Ошибка парсинга расписания: " + e.getMessage());
        }
    }
    
    private String getJsonValue(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int start = json.indexOf(search);
            if (start == -1) return "";
            
            start += search.length();
            
            while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
                start++;
            }
            
            if (start >= json.length()) return "";
            
            char firstChar = json.charAt(start);
            if (firstChar == '"') {
                start++;
                int end = json.indexOf('"', start);
                return end == -1 ? "" : json.substring(start, end);
            } else {
                int end = start;
                while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') {
                    end++;
                }
                return json.substring(start, end).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }
    
    private void processRequest(String action) {
        int row = requestsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Выберите запрос из таблицы");
            return;
        }
        
        int modelRow = requestsTable.convertRowIndexToModel(row);
        String id = (String) requestsModel.getValueAt(modelRow, 0);
        String currentStatus = (String) requestsModel.getValueAt(modelRow, 7);
        
        // Проверка, можно ли обработать запрос
        if (!"PENDING".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                    "Этот запрос уже обработан (" + currentStatus + ")",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Создаем final переменную для комментария
        String commentInput = JOptionPane.showInputDialog(this,
                "Введите комментарий для " + (action.equals("approve") ? "одобрения" : "отклонения") + ":",
                action.equals("approve") ? "Одобрение запроса" : "Отклонение запроса",
                JOptionPane.QUESTION_MESSAGE);
        
        if (commentInput == null) return;
        
        // Копируем в final переменную для использования в лямбде
        final String comment = commentInput.trim().isEmpty() ? 
                (action.equals("approve") ? "Одобрено" : "Отклонено") : 
                commentInput;
        
        statusLabel.setText("Обработка запроса...");
        
        // Создаем final переменные для ID и действия для использования в лямбде
        final String requestId = id;
        final String requestAction = action;
        
        new Thread(() -> {
            try {
                String response = updateRequestStatus(requestId, requestAction, comment);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(ModeratorPanel.this,
                            "Запрос успешно обработан",
                            "Успех",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadRequests(); // Обновляем список запросов
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(ModeratorPanel.this,
                            "Ошибка: " + e.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    statusLabel.setText("Ошибка обработки");
                });
            }
        }).start();
    }
    
    private String updateRequestStatus(String id, String action, String comment) throws Exception {
        String urlStr = "http://localhost:8080/api/moderator/requests/" + id + "/" + action;
        
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization", authHeader);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        
        // Отправка запроса
        try (OutputStream os = conn.getOutputStream()) {
            String postData = "comment=" + URLEncoder.encode(comment, "UTF-8");
            os.write(postData.getBytes(StandardCharsets.UTF_8));
        }
        
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new Exception("HTTP ошибка: " + code);
        }
        
        // Чтение ответа
        BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        
        return response.toString();
    }
    
    private void showComment() {
        int row = requestsTable.getSelectedRow();
        if (row < 0) return;
        
        int modelRow = requestsTable.convertRowIndexToModel(row);
        String id = (String) requestsModel.getValueAt(modelRow, 0);
        String status = (String) requestsModel.getValueAt(modelRow, 7);
        
        // В реальном приложении здесь бы загружался полный объект с комментарием
        // Для упрощения покажем диалог
        String comment = JOptionPane.showInputDialog(this,
                "Комментарий модератора для запроса #" + id + " (статус: " + status + "):",
                "Комментарий",
                JOptionPane.PLAIN_MESSAGE);
        
        if (comment != null && !comment.trim().isEmpty()) {
            // Здесь можно сохранить комментарий, если нужно
        }
    }
}