/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hospital;

import classes.DbHelper;
import classes.DoctorBolumleri;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author DUMAN
 */
public class DoctorGUI extends javax.swing.JFrame {

    /**
     * Creates new form DoctorGUI
     */
    String tc_kimlik;
    DefaultTableModel model;
    LoginGUI login = new LoginGUI();
    Date defaultDate = new Date();
    
    public DoctorGUI() {
        initComponents();
        TableColumn idColumn = tblKayitlar.getColumnModel().getColumn(0);
        idColumn.setPreferredWidth(10);
        TableColumn cinsiyetColumn = tblKayitlar.getColumnModel().getColumn(8);
        cinsiyetColumn.setPreferredWidth(40);
        model = (DefaultTableModel) tblKayitlar.getModel();
        loadBolum();
        loadDoktor();
        resimDuzelt(lblResim, "Images/doctorIcon.png");
        resimDuzelt(lblCarpi, "Images/carpiIcon.png");
        ButtonGroup group = new ButtonGroup();
        group.add(rdbErkek);
        group.add(rdbKadin);
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
    
    private void loadDoktor() {
        model.setRowCount(0);
        try {
            ArrayList<DoctorBolumleri> doctors = getDoctors();
            for (DoctorBolumleri doctor : doctors) {
                Object[] row = {doctor.getId(), doctor.getTc(), doctor.getAd(), doctor.getSoyad(), doctor.getBolum_adi(), doctor.getTelefon(), doctor.getEmail(), login.getirFormattedDate(doctor.getDogumTarihi()), doctor.getCinsiyet()};
                model.addRow(row);
            }
        } catch (SQLException ex) {
        }
    }
    
    public boolean kontrol() {
        boolean control = false;
        if (!ftxtTC.getText().isEmpty()
                && !ftxtTelefon.getText().isEmpty()
                && !ftxtDogumTarihi.getText().isEmpty()
                && !txtAd.getText().isEmpty()
                && !txtSoyad.getText().isEmpty()
                && !txtEmail.getText().isEmpty()
                && (rdbErkek.isSelected() || rdbKadin.isSelected())) {
            control = true;
        }
        return control;
    }
    
    public ArrayList<DoctorBolumleri> getDoctors() throws SQLException {
        Connection connection = null;
        DbHelper helper = new DbHelper();
        Statement statement = null;
        ResultSet resultSet;
        ArrayList<DoctorBolumleri> doctors = null;
        try {
            connection = helper.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from doktor_bolumleri");
            doctors = new ArrayList<DoctorBolumleri>();
            while (resultSet.next()) {
                doctors.add(new DoctorBolumleri(
                        resultSet.getInt("doktor_id"),
                        resultSet.getString("tc_kimlik"),
                        resultSet.getString("ad"),
                        resultSet.getString("soyad"),
                        resultSet.getString("dogum_tarihi"),
                        resultSet.getString("cinsiyet"),
                        resultSet.getString("telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("adres"),
                        resultSet.getString("bolum_adi")));
            }
        } catch (SQLException e) {
            helper.showErrorMessage(e);
        } finally {
            statement.close();
            connection.close();
        }
        return doctors;
    }
    
    public String bolum(String tc_kimlik) {
        String bolumAdi = "";
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        ResultSet resultSet;
        try {
            connection = dbHelper.getConnection();
            String sql = "select * from doktor_bolumleri where tc_kimlik = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, tc_kimlik);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                bolumAdi = resultSet.getString("bolum_adi");
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
        return bolumAdi;
    }
    
    public int bolumId(String bolum){
        int id = 0;
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        ResultSet resultSet;
        try {
            connection = dbHelper.getConnection();
            String sql = "select * from bolumler where bolum_adi = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, bolum);
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
    
    public void resimDuzelt(JLabel label, String url) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + url));
        Image resim = icon.getImage();
        Image yeniResim = resim.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon yeniIcon = new ImageIcon(yeniResim);
        label.setIcon(yeniIcon);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKayitlar = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblHastaBilgileri = new javax.swing.JLabel();
        lblCizgi = new javax.swing.JLabel();
        lblTC = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtAd = new javax.swing.JTextField();
        lblAd = new javax.swing.JLabel();
        txtSoyad = new javax.swing.JTextField();
        lblSoyad = new javax.swing.JLabel();
        lblCinsiyet = new javax.swing.JLabel();
        lblDogumTarihi = new javax.swing.JLabel();
        btnGetir = new javax.swing.JButton();
        lblTelefon = new javax.swing.JLabel();
        rdbErkek = new javax.swing.JRadioButton();
        rdbKadin = new javax.swing.JRadioButton();
        btnKaydet = new javax.swing.JButton();
        btnSil = new javax.swing.JButton();
        btnTemizle = new javax.swing.JButton();
        btnGuncelle = new javax.swing.JButton();
        lblResim = new javax.swing.JLabel();
        lblCarpi = new javax.swing.JLabel();
        cmbBolum = new javax.swing.JComboBox<>();
        lblBolum = new javax.swing.JLabel();
        ftxtTC = new javax.swing.JFormattedTextField();
        ftxtDogumTarihi = new javax.swing.JFormattedTextField();
        ftxtTelefon = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblKayitlar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "TC Numarası", "Ad", "Soyad", "Bölüm", "Telefon", "Email", "Doğum Tarihi", "Cinsiyet"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKayitlar.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblKayitlar);
        if (tblKayitlar.getColumnModel().getColumnCount() > 0) {
            tblKayitlar.getColumnModel().getColumn(0).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(1).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(2).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(3).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(4).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(5).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(6).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(7).setResizable(false);
            tblKayitlar.getColumnModel().getColumn(8).setResizable(false);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 230));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, 730, 230));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHastaBilgileri.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblHastaBilgileri.setText("Doktor Bilgileri");
        jPanel1.add(lblHastaBilgileri, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        lblCizgi.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCizgi.setText("__________________");
        jPanel1.add(lblCizgi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        lblTC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTC.setText("TC:");
        jPanel1.add(lblTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 180, 30));

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEmail.setText("Email:");
        jPanel1.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, -1, -1));

        txtAd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtAd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdKeyTyped(evt);
            }
        });
        jPanel1.add(txtAd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 180, 30));

        lblAd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAd.setText("Ad:");
        jPanel1.add(lblAd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        txtSoyad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoyad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSoyadKeyTyped(evt);
            }
        });
        jPanel1.add(txtSoyad, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 180, 30));

        lblSoyad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSoyad.setText("Soyad:");
        jPanel1.add(lblSoyad, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, -1));

        lblCinsiyet.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCinsiyet.setText("Cinsiyet:");
        jPanel1.add(lblCinsiyet, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, -1, -1));

        lblDogumTarihi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDogumTarihi.setText("Doğum Tarihi:");
        jPanel1.add(lblDogumTarihi, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, -1, -1));

        btnGetir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGetir.setText("Getir");
        btnGetir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetirActionPerformed(evt);
            }
        });
        jPanel1.add(btnGetir, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 60, 25));

        lblTelefon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTelefon.setText("Telefon:");
        jPanel1.add(lblTelefon, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        rdbErkek.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdbErkek.setText("Erkek");
        jPanel1.add(rdbErkek, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 260, 60, -1));

        rdbKadin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rdbKadin.setText("Kadın");
        jPanel1.add(rdbKadin, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 260, 60, -1));

        btnKaydet.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnKaydet.setText("KAYDET");
        btnKaydet.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnKaydet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKaydetActionPerformed(evt);
            }
        });
        jPanel1.add(btnKaydet, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 80, 30));

        btnSil.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSil.setText("SİL");
        btnSil.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilActionPerformed(evt);
            }
        });
        jPanel1.add(btnSil, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 310, 80, 30));

        btnTemizle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTemizle.setText("TEMİZLE");
        btnTemizle.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnTemizle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemizleActionPerformed(evt);
            }
        });
        jPanel1.add(btnTemizle, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 310, 80, 30));

        btnGuncelle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuncelle.setText("GÜNCELLE");
        btnGuncelle.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        btnGuncelle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuncelleActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuncelle, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 80, 30));
        jPanel1.add(lblResim, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 140, 170));

        lblCarpi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblCarpi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCarpiMouseClicked(evt);
            }
        });
        jPanel1.add(lblCarpi, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 30, 30));

        jPanel1.add(cmbBolum, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, 180, 30));

        lblBolum.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBolum.setText("Bölüm:");
        jPanel1.add(lblBolum, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 180, -1, -1));

        try {
            ftxtTC.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel1.add(ftxtTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 180, 30));

        try {
            ftxtDogumTarihi.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel1.add(ftxtDogumTarihi, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, 180, 30));

        try {
            ftxtTelefon.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("+90(###)-###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel1.add(ftxtTelefon, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 180, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 360));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTemizleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTemizleActionPerformed
        // TODO add your handling code here:
        txtAd.setText("");
        txtSoyad.setText("");
        txtEmail.setText("");
        ftxtTC.setText("");
        ftxtTelefon.setText("");
        ftxtDogumTarihi.setText("");
    }//GEN-LAST:event_btnTemizleActionPerformed

    private void lblCarpiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCarpiMouseClicked
        // TODO add your handling code here:
        MenuGUI menu = new MenuGUI();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_lblCarpiMouseClicked

    private void txtAdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdKeyTyped
        // TODO add your handling code here:
        char testChar = evt.getKeyChar();
        if (!(Character.isAlphabetic(testChar)))
            evt.consume();
    }//GEN-LAST:event_txtAdKeyTyped

    private void txtSoyadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoyadKeyTyped
        // TODO add your handling code here:
        char testChar = evt.getKeyChar();
        if (!(Character.isAlphabetic(testChar)))
            evt.consume();
    }//GEN-LAST:event_txtSoyadKeyTyped

    private void btnGetirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetirActionPerformed
        // TODO add your handling code here:
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        ResultSet result;
        try {
            connection = dbHelper.getConnection();
            String sql = "select * from doktor_bolumleri where tc_kimlik = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ftxtTC.getText());
            result = statement.executeQuery();
            if (result.next()) {
                tc_kimlik = ftxtTC.getText();
                txtAd.setText(result.getString("ad"));
                txtSoyad.setText(result.getString("soyad"));
                ftxtTelefon.setText(result.getString("telefon"));
                txtEmail.setText(result.getString("email"));
                ftxtDogumTarihi.setText(login.getirFormattedDate(result.getString("dogum_tarihi")));
                cmbBolum.setSelectedItem(bolum(ftxtTC.getText()));
                if (result.getString("cinsiyet").equals("E")) {
                    rdbErkek.setSelected(true);
                } else {
                    rdbKadin.setSelected(true);
                }
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

    private void btnSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilActionPerformed
        // TODO add your handling code here:
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        if (tblKayitlar.getSelectedRow() != -1) {
            try {
                connection = dbHelper.getConnection();
                String sql = "delete from doktorlar where tc_kimlik = " + tblKayitlar.getValueAt(tblKayitlar.getSelectedRow(), 1);
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(rootPane, "Silme İşlemi Başarılı!");
                loadDoktor();
            } catch (SQLException e) {
                dbHelper.showErrorMessage(e);
            } finally {
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Silmek İstediğiniz Kullanıcıyı Seçiniz!");
        }
    }//GEN-LAST:event_btnSilActionPerformed

    private void btnGuncelleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuncelleActionPerformed
        // TODO add your handling code here:
        String cinsiyet;
        if (rdbErkek.isSelected()) {
            cinsiyet = "E";
        } else {
            cinsiyet = "K";
        }
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        Statement selectStatement = null;
        ResultSet resultSet;
        int id;
        boolean control = kontrol();
        if (control) {
            try {
                connection = dbHelper.getConnection();
                selectStatement = connection.createStatement();
                resultSet = selectStatement.executeQuery("select * from doktorlar where tc_kimlik = " + tc_kimlik);
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String sql = "update doktorlar set tc_kimlik = ?, ad = ?, soyad = ?, dogum_tarihi = ?, cinsiyet = ?, telefon = ?, email = ?, bolum_id = ?, guncelle_tarihi = ?, guncelle_kullanici_id = ? where id = " + id;
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, ftxtTC.getText());
                    statement.setString(2, txtAd.getText());
                    statement.setString(3, txtSoyad.getText());
                    statement.setString(4, login.formattedDate(ftxtDogumTarihi.getText()));
                    statement.setString(5, cinsiyet);
                    statement.setString(6, ftxtTelefon.getText());
                    statement.setString(7, txtEmail.getText());
                    statement.setInt(8,bolumId((String)cmbBolum.getSelectedItem()));
                    statement.setString(9,login.formattedDate(defaultDate));
                    statement.setInt(10,login.userId);
                    statement.executeUpdate();
                    loadDoktor();
                    btnTemizleActionPerformed(evt);
                    JOptionPane.showMessageDialog(rootPane, "Güncelleme İşlemi Başarılı!");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Kullanıcı Bulunamadı!");
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
        } else {
            JOptionPane.showMessageDialog(rootPane, "Tüm Alanları Doldurunuz!");
        }
    }//GEN-LAST:event_btnGuncelleActionPerformed

    private void btnKaydetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKaydetActionPerformed
        // TODO add your handling code here:
        String cinsiyet;
        if (rdbErkek.isSelected()) {
            cinsiyet = "E";
        } else {
            cinsiyet = "K";
        }
        Connection connection = null;
        DbHelper dbHelper = new DbHelper();
        PreparedStatement statement = null;
        boolean control = kontrol();
        if (control) {
            try {
                connection = dbHelper.getConnection();
                String sql = "insert into doktorlar(tc_kimlik, ad, soyad, dogum_tarihi, cinsiyet, telefon, email, adres, bolum_id,ekle_tarih,ekle_kullanici_id) values (?,?,?,?,?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, ftxtTC.getText());
                statement.setString(2, txtAd.getText());
                statement.setString(3, txtSoyad.getText());
                statement.setString(4, login.formattedDate(ftxtDogumTarihi.getText()));
                statement.setString(5, cinsiyet);
                statement.setString(6, ftxtTelefon.getText());
                statement.setString(7, txtEmail.getText());
                statement.setString(8,"İstanbul");
                statement.setInt(9,bolumId((String)cmbBolum.getSelectedItem()));
                statement.setString(10, login.formattedDate(defaultDate));
                statement.setInt(11,login.userId);
                statement.executeUpdate();
                btnTemizleActionPerformed(evt);
                JOptionPane.showMessageDialog(rootPane, "Kayıt İşlemi Başarılı!");
                loadDoktor();
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
            java.util.logging.Logger.getLogger(DoctorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DoctorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DoctorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DoctorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DoctorGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGetir;
    private javax.swing.JButton btnGuncelle;
    private javax.swing.JButton btnKaydet;
    private javax.swing.JButton btnSil;
    private javax.swing.JButton btnTemizle;
    private javax.swing.JComboBox<String> cmbBolum;
    private javax.swing.JFormattedTextField ftxtDogumTarihi;
    private javax.swing.JFormattedTextField ftxtTC;
    private javax.swing.JFormattedTextField ftxtTelefon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAd;
    private javax.swing.JLabel lblBolum;
    private javax.swing.JLabel lblCarpi;
    private javax.swing.JLabel lblCinsiyet;
    private javax.swing.JLabel lblCizgi;
    private javax.swing.JLabel lblDogumTarihi;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblHastaBilgileri;
    private javax.swing.JLabel lblResim;
    private javax.swing.JLabel lblSoyad;
    private javax.swing.JLabel lblTC;
    private javax.swing.JLabel lblTelefon;
    private javax.swing.JRadioButton rdbErkek;
    private javax.swing.JRadioButton rdbKadin;
    private javax.swing.JTable tblKayitlar;
    private javax.swing.JTextField txtAd;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtSoyad;
    // End of variables declaration//GEN-END:variables
}
