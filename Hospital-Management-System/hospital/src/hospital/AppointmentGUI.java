/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hospital;

import classes.DbHelper;
import classes.Randevu;
import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DUMAN
 */
public class AppointmentGUI extends javax.swing.JFrame {

    /**
     * Creates new form AppointmentGUI
     */
    DefaultTableModel model;
    LoginGUI login = new LoginGUI();
    DoctorGUI doctor = new DoctorGUI();
    int bolumId;

    public AppointmentGUI() {
        initComponents();
        model = (DefaultTableModel) tblRandevular.getModel();
        loadBolum();
        resimDuzelt(lblResim, "Images/appointmentImage.png");
        resimDuzelt(lblCarpi, "Images/carpiIcon.png");
    }

    public void resimDuzelt(JLabel label, String url) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + url));
        Image resim = icon.getImage();
        Image yeniResim = resim.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon yeniIcon = new ImageIcon(yeniResim);
        label.setIcon(yeniIcon);
    }

    public boolean kontrol() {
        boolean control = false;
        if (!ftxtHastaTC.getText().isEmpty()
                && !txtHastaAdi.getText().isEmpty()
                && !txtHastaSoyadi.getText().isEmpty()
                && !ftxtHastaDogumTarihi.getText().isEmpty()
                && !ftxtTarih.getText().isEmpty()) {
            control = true;
        }
        return control;
    }

    public int hastaId(String tc) {
        int id = 0;
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        ResultSet resultSet;
        try {
            connection = dbHelper.getConnection();
            String sql = "select * from hastalar where tc_kimlik = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, tc);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            dbHelper.showErrorMessage(e);
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException ex) {
            }
        }
        return id;
    }

    public int doktorId(String name, String soyad) {
        int id = 0;
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        ResultSet resultSet;
        try {
            connection = dbHelper.getConnection();
            String sql = "select * from doktorlar where ad = ? and soyad = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, soyad);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            dbHelper.showErrorMessage(e);
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException ex) {
            }
        }
        return id;
    }

    public void loadBolum() {
        Connection connection = null;
        DbHelper helper = new DbHelper();
        Statement statement = null;
        ResultSet resultSet;
        ArrayList<String> bolumler = null;
        try {
            connection = helper.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from bolumler");
            bolumler = new ArrayList<String>();
            while (resultSet.next()) {
                bolumler.add((resultSet.getString("bolum_adi")));
            }
            for (String bolum : bolumler) {
                cmbBolum.addItem(bolum);
            }
        } catch (SQLException exception) {
            helper.showErrorMessage(exception);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

    public void loadData() {
        model.setRowCount(0);
        ArrayList<Randevu> randevular = getRandevular();
        for (Randevu randevu : randevular) {
            Object[] row = {randevu.getSira(), randevu.getBolum(), randevu.getDoktor(), randevu.getHasta(), randevu.getRandevu_saati(), randevu.getRandevu_durumu()};
            model.addRow(row);
        }
    }

    public void loadDoktor() {
        cmbDoktor.removeAllItems();
        bolumId = doctor.bolumId((String) cmbBolum.getSelectedItem());
        ArrayList<String> doctorlar;
        try {
            doctorlar = getDoctors();
            for (String str : doctorlar) {
                cmbDoktor.addItem(str);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
        }
    }

    public ArrayList<String> getDoctors() throws SQLException {
        Connection connection = null;
        DbHelper helper = new DbHelper();
        Statement statement = null;
        ResultSet resultSet;
        ArrayList<String> doctors = null;
        try {
            connection = helper.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from doktorlar where bolum_id = " + bolumId);
            doctors = new ArrayList<String>();
            while (resultSet.next()) {
                String isim = resultSet.getString("ad") + " " + resultSet.getString("soyad");
                doctors.add(isim);
            }
        } catch (SQLException e) {
            helper.showErrorMessage(e);
        } finally {
            statement.close();
            connection.close();
        }
        return doctors;
    }

    public ArrayList<Randevu> getRandevular() {
        Connection connection = null;
        DbHelper helper = new DbHelper();
        Statement saatStatement = null;
        PreparedStatement randevuStatement = null;
        ResultSet saatResultSet;
        ResultSet randevuResultSet;
        ArrayList<Randevu> randevular = new ArrayList<>();
        try {
            connection = helper.getConnection();
            saatStatement = connection.createStatement();
            saatResultSet = saatStatement.executeQuery("SELECT * FROM randevu_saatleri");
            while (saatResultSet.next()) {
                String sql = "SELECT * FROM v_randevular WHERE saat_id = ? AND tarih = ? AND doktor_adsoyad = ?";
                randevuStatement = connection.prepareStatement(sql);
                randevuStatement.setInt(1, saatResultSet.getInt("id"));
                randevuStatement.setString(2, login.formattedDate(ftxtTarih.getText()));
                randevuStatement.setString(3, (String) cmbDoktor.getSelectedItem());
                randevuResultSet = randevuStatement.executeQuery();
                String saat = saatResultSet.getString("saat");
                if (!randevuResultSet.isBeforeFirst()) {
                    randevular.add(new Randevu(
                            saatResultSet.getString("sira"),
                            "-- BOŞ --",
                            "-- BOŞ --",
                            "-- BOŞ --",
                            saat,
                            "-- BOŞ --"
                    ));
                } else {
                    while (randevuResultSet.next()) {
                        randevular.add(new Randevu(
                                saatResultSet.getString("sira"),
                                randevuResultSet.getString("bolum_adi"),
                                randevuResultSet.getString("doktor_adsoyad"),
                                randevuResultSet.getString("hasta_adsoyad"),
                                saat,
                                "Aktif"
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            helper.showErrorMessage(e);
        } finally {
            try {
                saatStatement.close();
                randevuStatement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
        return randevular;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHastaBilgileri = new javax.swing.JLabel();
        lblCizgi = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRandevular = new javax.swing.JTable();
        lblResim = new javax.swing.JLabel();
        lblHastaTC = new javax.swing.JLabel();
        txtHastaAdi = new javax.swing.JTextField();
        lblHastaAdi = new javax.swing.JLabel();
        txtHastaSoyadi = new javax.swing.JTextField();
        lblHastaSoyadi = new javax.swing.JLabel();
        lblDoktor = new javax.swing.JLabel();
        btnGetir = new javax.swing.JButton();
        lblHastaDogumTarihi = new javax.swing.JLabel();
        lblBolum = new javax.swing.JLabel();
        cmbDoktor = new javax.swing.JComboBox<>();
        cmbBolum = new javax.swing.JComboBox<>();
        btnKaydet = new javax.swing.JButton();
        btnListele = new javax.swing.JButton();
        btnIptal = new javax.swing.JButton();
        ftxtTarih = new javax.swing.JFormattedTextField();
        ftxtHastaTC = new javax.swing.JFormattedTextField();
        lblCarpi = new javax.swing.JLabel();
        lblTarih = new javax.swing.JLabel();
        ftxtHastaDogumTarihi = new javax.swing.JFormattedTextField();
        btnTemizle = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Randevu Bilgileri");
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHastaBilgileri.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblHastaBilgileri.setText("Randevu Bilgileri");
        getContentPane().add(lblHastaBilgileri, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        lblCizgi.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCizgi.setText("____________________");
        getContentPane().add(lblCizgi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        tblRandevular.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sıra", "Bölüm", "Doktor", "Hasta", "Randevu Saati", "Randevu Durumu"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRandevular.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblRandevular);
        if (tblRandevular.getColumnModel().getColumnCount() > 0) {
            tblRandevular.getColumnModel().getColumn(0).setResizable(false);
            tblRandevular.getColumnModel().getColumn(1).setResizable(false);
            tblRandevular.getColumnModel().getColumn(2).setResizable(false);
            tblRandevular.getColumnModel().getColumn(3).setResizable(false);
            tblRandevular.getColumnModel().getColumn(4).setResizable(false);
            tblRandevular.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 870, 210));
        jPanel1.add(lblResim, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 250, 260));

        lblHastaTC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHastaTC.setText("Hasta TC:");
        jPanel1.add(lblHastaTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));
        jPanel1.add(txtHastaAdi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 180, 30));

        lblHastaAdi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHastaAdi.setText("Hasta Adı:");
        jPanel1.add(lblHastaAdi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));
        jPanel1.add(txtHastaSoyadi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 180, 30));

        lblHastaSoyadi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHastaSoyadi.setText("Hasta Soyadı:");
        jPanel1.add(lblHastaSoyadi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        lblDoktor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDoktor.setText("Doktor:");
        jPanel1.add(lblDoktor, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, -1, -1));

        btnGetir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGetir.setText("Getir");
        btnGetir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetirActionPerformed(evt);
            }
        });
        jPanel1.add(btnGetir, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 60, 25));

        lblHastaDogumTarihi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblHastaDogumTarihi.setText("Hasta Doğum Tarihi:");
        jPanel1.add(lblHastaDogumTarihi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        lblBolum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBolum.setText("Bölüm:");
        jPanel1.add(lblBolum, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, -1, -1));

        jPanel1.add(cmbDoktor, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 180, 30));

        cmbBolum.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBolumİtemStateChanged(evt);
            }
        });
        jPanel1.add(cmbBolum, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 180, 30));

        btnKaydet.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKaydet.setText("KAYDET");
        btnKaydet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKaydetActionPerformed(evt);
            }
        });
        jPanel1.add(btnKaydet, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 100, 30));

        btnListele.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnListele.setText("Listele");
        btnListele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListeleActionPerformed(evt);
            }
        });
        jPanel1.add(btnListele, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 320, 100, 30));

        btnIptal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnIptal.setText("İPTAL");
        btnIptal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIptalActionPerformed(evt);
            }
        });
        jPanel1.add(btnIptal, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 100, 30));

        try {
            ftxtTarih.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel1.add(ftxtTarih, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 180, 30));

        try {
            ftxtHastaTC.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel1.add(ftxtHastaTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 180, 30));

        lblCarpi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCarpi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCarpiMouseClicked(evt);
            }
        });
        jPanel1.add(lblCarpi, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 10, 30, 30));

        lblTarih.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTarih.setText("Tarih:");
        jPanel1.add(lblTarih, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, -1, -1));

        try {
            ftxtHastaDogumTarihi.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel1.add(ftxtHastaDogumTarihi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 180, 30));

        btnTemizle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTemizle.setText("TEMİZLE");
        btnTemizle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemizleActionPerformed(evt);
            }
        });
        jPanel1.add(btnTemizle, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 320, 100, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 570));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGetirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetirActionPerformed
        // TODO add your handling code here:
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        ResultSet result;
        try {
            connection = dbHelper.getConnection();
            String sql = "select * from hastalar where tc_kimlik = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ftxtHastaTC.getText());
            result = statement.executeQuery();
            if (result.next()) {
                txtHastaAdi.setText(result.getString("ad"));
                txtHastaSoyadi.setText(result.getString("soyad"));
                ftxtHastaDogumTarihi.setText(login.getirFormattedDate(result.getString("dogum_tarihi")));
            } else {
                JOptionPane.showMessageDialog(rootPane, "Kayıt Bulunamadı!");
            }
        } catch (SQLException e) {
            dbHelper.showErrorMessage(e);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_btnGetirActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        System.out.println("kap");
    }//GEN-LAST:event_formWindowClosed

    private void lblCarpiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCarpiMouseClicked
        // TODO add your handling code here:
        MenuGUI menu = new MenuGUI();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblCarpiMouseClicked

    private void btnListeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListeleActionPerformed
        // TODO add your handling code here:
        if (!ftxtTarih.getText().equals("  -  -    ")) {
            loadData();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Tarih Giriniz!");
        }
    }//GEN-LAST:event_btnListeleActionPerformed

    private void cmbBolumİtemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBolumİtemStateChanged
        // TODO add your handling code here:
        loadDoktor();
    }//GEN-LAST:event_cmbBolumİtemStateChanged

    private void btnIptalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIptalActionPerformed
        // TODO add your handling code here:
        Connection connection = null;
        DbHelper helper = new DbHelper();
        PreparedStatement statement = null;
        if (tblRandevular.getSelectedRow() != -1) {
            try {
                connection = helper.getConnection();
                String sql = "delete from randevular where tarih = ? and saat_id = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, login.formattedDate(ftxtTarih.getText()));
                statement.setString(2, (String) tblRandevular.getValueAt(tblRandevular.getSelectedRow(), 0));
                statement.executeUpdate();
                JOptionPane.showMessageDialog(rootPane, "Silme İşlemi Başarılı!");
                loadData();
            } catch (SQLException e) {
                helper.showErrorMessage(e);
            } finally {
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "İptal Etmek İstediğiniz Randevuyu Seçiniz!");
        }

    }//GEN-LAST:event_btnIptalActionPerformed

    private void btnTemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemizleActionPerformed
        // TODO add your handling code here:
        ftxtHastaTC.setText("");
        ftxtHastaDogumTarihi.setText("");
        txtHastaAdi.setText("");
        txtHastaSoyadi.setText("");
    }//GEN-LAST:event_btnTemizleActionPerformed

    private void btnKaydetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKaydetActionPerformed
        // TODO add your handling code here:
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        if (kontrol()) {
            if (tblRandevular.getSelectedRow() != -1) {
                try {
                    connection = dbHelper.getConnection();
                    String sql = "INSERT INTO randevular (bolum_id, doktor_id, hasta_id, tarih, saat_id, durum) VALUES (?,?,?,?,?,?)";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, bolumId);
                    String text = (String) cmbDoktor.getSelectedItem();
                    int index = text.indexOf(' ');
                    String beforeSpace = text.substring(0, index);
                    String afterSpace = text.substring(index + 1);
                    statement.setInt(2, doktorId(beforeSpace, afterSpace));
                    statement.setInt(3, hastaId(ftxtHastaTC.getText()));
                    statement.setString(4, login.formattedDate(ftxtTarih.getText()));
                    statement.setString(5, (String) tblRandevular.getValueAt(tblRandevular.getSelectedRow(), 0));
                    statement.setString(6, "A");
                    statement.executeUpdate();
                    btnTemizleActionPerformed(evt);
                    JOptionPane.showMessageDialog(rootPane, "Kayıt İşlemi Başarılı!");
                    loadDoktor();
                    loadData();
                } catch (SQLException e) {
                    dbHelper.showErrorMessage(e);
                    JOptionPane.showMessageDialog(rootPane, "Tüm Alanları Doğru Giriniz!");
                } finally {
                    try {
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane, "Randevu Saati Seçiniz!");
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Tüm Alanları Doldurunuz!");
        }
    }//GEN-LAST:event_btnKaydetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppointmentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppointmentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppointmentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppointmentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppointmentGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGetir;
    private javax.swing.JButton btnIptal;
    private javax.swing.JButton btnKaydet;
    private javax.swing.JButton btnListele;
    private javax.swing.JButton btnTemizle;
    private javax.swing.JComboBox<String> cmbBolum;
    private javax.swing.JComboBox<String> cmbDoktor;
    private javax.swing.JFormattedTextField ftxtHastaDogumTarihi;
    private javax.swing.JFormattedTextField ftxtHastaTC;
    private javax.swing.JFormattedTextField ftxtTarih;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBolum;
    private javax.swing.JLabel lblCarpi;
    private javax.swing.JLabel lblCizgi;
    private javax.swing.JLabel lblDoktor;
    private javax.swing.JLabel lblHastaAdi;
    private javax.swing.JLabel lblHastaBilgileri;
    private javax.swing.JLabel lblHastaDogumTarihi;
    private javax.swing.JLabel lblHastaSoyadi;
    private javax.swing.JLabel lblHastaTC;
    private javax.swing.JLabel lblResim;
    private javax.swing.JLabel lblTarih;
    private javax.swing.JTable tblRandevular;
    private javax.swing.JTextField txtHastaAdi;
    private javax.swing.JTextField txtHastaSoyadi;
    // End of variables declaration//GEN-END:variables
}
