/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsg.view;
import bsg.koneksi.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Toolkit;
/**
 *
 * @author kevadamargalih
 */
public class FrameMHS extends javax.swing.JFrame {
    Connection connection;
    DefaultTableModel model;
    /**
     * Creates new form FrameMHS
     */
    public FrameMHS() {
        initComponents();
        connection = koneksi.getConnection();
        getDataTable();
        getCmbProdi();
        getCmbKonsen();
        tNIM.requestFocus();
        bUbah.setEnabled(false);
        bHapus.setEnabled(false);
        
        // mengambil ukuran layar
        Dimension layar = Toolkit.getDefaultToolkit().getScreenSize();
        
        // membuat titik x dan y
        int x = layar.width / 2 - this.getSize().width / 2;
        int y = layar.height / 2 - this.getSize().height / 2; 
        
        this.setLocation(x, y);
        refresh();
    }
    
    private void getDataTable() {
        model = (DefaultTableModel) table.getModel();
        try {
            Statement stat = connection.createStatement();
            String sql = "SELECT * FROM mahasiswa,prodi,konsentrasi "
                    + "WHERE mahasiswa.id_prodi=prodi.id_prodi AND "
                    + "mahasiswa.id_konsentrasi=konsentrasi.id_konsentrasi";
            ResultSet res = stat.executeQuery(sql);
            while(res.next ()) {
                Object[ ] obj = new Object[6];
                obj[0] = res.getString("nim");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("alamat");
                obj[3] = res.getString("no_telp");
                obj[4] = res.getString("nama_prodi");
                obj[5] = res.getString("nama_konsen");
                model.addRow(obj);
            }
        } catch(SQLException err) {
            err.printStackTrace();
        }
    }
    
    private void getCmbProdi() {
        cmbProdi.removeAllItems();
        try {
            Statement stat = connection.createStatement();
            String sql = "SELECT * FROM prodi";
            ResultSet res = stat.executeQuery(sql);
            while (res.next ()) {
                cmbProdi.addItem(res.getString("nama_prodi")); 
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    private void getCmbKonsen() {
        cmbKonsen.removeAllItems();
        try {
            Statement stat = connection.createStatement();
            String sql = "SELECT * FROM konsentrasi";
            ResultSet res = stat.executeQuery(sql);
            while (res.next ()) {
                cmbKonsen.addItem(res.getString("nama_konsen")); 
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }
    
    private void refresh() {
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        getDataTable();
        tNIM.setEditable(true);
    }
    
    private void reset() {
        tNIM.setText("");
        tNama.setText("");
        tAlamat.setText("");
        tNotelp.setText("");
        tNIM.setEditable(true);
        bSimpan.setEnabled(true);
        bUbah.setEnabled(false);
        bHapus.setEnabled(false);
    }
    
    private void insert() {
        PreparedStatement statement = null;
        String sql = "INSERT INTO mahasiswa (nim,nama,alamat,no_telp,id_prodi,id_konsentrasi) VALUES(?,?,?,?,?,?);";
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tNIM.getText());
            statement.setString(2, tNama.getText());
            statement.setString(3, tAlamat.getText());
            statement.setString(4, tNotelp.getText());
            statement.setInt(5, getIDProdi(cmbProdi.getSelectedItem().toString()));
            statement.setInt(6, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void update() {
        PreparedStatement statement = null;
        String sql = "UPDATE mahasiswa SET nama=?,alamat=?,no_telp=?,id_prodi=?,id_konsentrasi=? WHERE nim=?";
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tNama.getText());
            statement.setString(2, tAlamat.getText());
            statement.setString(3, tNotelp.getText());
            statement.setInt(4, getIDProdi(cmbProdi.getSelectedItem().toString()));
            statement.setInt(5, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
            statement.setString(6, tNIM.getText());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void delete() {
        PreparedStatement statement = null;
        String sql = "DELETE FROM mahasiswa WHERE nim=?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, tNIM.getText());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void search() {
        model = (DefaultTableModel) table.getModel();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM mahasiswa,prodi,konsentrasi WHERE "
                    + "mahasiswa.id_prodi=prodi.id_prodi AND "
                    + "mahasiswa.id_konsentrasi=konsentrasi.id_konsentrasi AND nama LIKE ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + tCari.getText() + "%");
            ResultSet res = statement.executeQuery();
            while (res.next ()) {
                Object[ ] obj = new Object[6];
                obj[0] = res.getString("nim");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("alamat");
                obj[3] = res.getString("no_telp");
                obj[4] = res.getString("nama_prodi");
                obj[5] = res.getString("nama_konsen");
                model.addRow(obj);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private int getIDProdi(String nama_prodi) {
        int id = 0;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT id_prodi FROM prodi WHERE nama_prodi=?",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, nama_prodi);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id_prodi");
            } 
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(FrameMHS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    private int getIDKonsen(String namakonsen) {
        int id = 0;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT id_konsentrasi FROM konsentrasi WHERE nama_konsen=?",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, namakonsen);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id_konsentrasi");
            } 
            return id;
        } catch (SQLException ex) {
            Logger.getLogger(FrameMHS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        tNIM = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tNama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tAlamat = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        tNotelp = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbProdi = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbKonsen = new javax.swing.JComboBox<>();
        bSimpan = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bReset = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        tCari = new javax.swing.JTextField();
        bCari = new javax.swing.JButton();
        bRefresh = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Mahasiswa");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 255));
        jLabel1.setText("Data Mahasiswa");

        jLabel2.setText("NIM");

        jLabel3.setText("Nama");

        jLabel4.setText("Alamat");

        tAlamat.setColumns(20);
        tAlamat.setRows(5);
        jScrollPane1.setViewportView(tAlamat);

        jLabel5.setText("No. Telephone");

        jLabel6.setText("Program Studi");

        cmbProdi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Konsentrasi");

        cmbKonsen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        bSimpan.setText("Simpan");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

        bHapus.setText("Hapus");
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });

        bUbah.setText("Ubah");
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });

        bReset.setText("Reset");
        bReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bResetActionPerformed(evt);
            }
        });

        jLabel8.setText("Cari Data");

        bCari.setText("Cari");
        bCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCariActionPerformed(evt);
            }
        });

        bRefresh.setText("Refresh");
        bRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRefreshActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "NIM", "Nama", "Alamat", "No. Telepon", "Prodi", "Konsentrasi"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table);

        jButton1.setText("Menu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(39, 232, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tNotelp)
                                            .addComponent(cmbProdi, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbKonsen, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(bSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(bHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(bUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(bReset, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tNama)
                                    .addComponent(tNIM))
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bCari, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bRefresh))
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(174, 174, 174)
                        .addComponent(jLabel1)))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tNIM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCari)
                    .addComponent(bRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(104, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNotelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbProdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbKonsen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSimpan)
                            .addComponent(bUbah))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bHapus)
                            .addComponent(bReset))
                        .addGap(32, 32, 32))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        if(!tNIM.getText().trim().isEmpty() && !tNama.getText().trim().isEmpty()
                && !tAlamat.getText().trim().isEmpty() && !tNotelp.getText().trim().isEmpty()) {
            insert();
            refresh();
            reset();
            JOptionPane.showMessageDialog(this, "Mahasiswa berhasil ditambahkan", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lengkapi form terlebih dahulu", "Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        int alert = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus mahasiswa ini?", "Notifikasi", JOptionPane.YES_NO_OPTION);
        if (alert == JOptionPane.YES_OPTION) {
            delete();
            refresh();
            reset();
            JOptionPane.showMessageDialog(this, "mahasiswa berhasil dihapus", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        update();
        refresh();
        reset();
        JOptionPane.showMessageDialog(this, "Mahasiswa berhasil diubah", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_bUbahActionPerformed

    private void bResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bResetActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_bResetActionPerformed

    private void bCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariActionPerformed
        // TODO add your handling code here:
        if(!tCari.getText().trim().isEmpty()) {
            model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            search();
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan nama prodi yang ingin dicari", "Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bCariActionPerformed

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_bRefreshActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
        tNIM.setText(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
        tNama.setText(table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
        tAlamat.setText(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
        tNotelp.setText(table.getModel().getValueAt(table.getSelectedRow(), 3).toString());
        cmbProdi.setSelectedItem(table.getModel().getValueAt(table.getSelectedRow(), 4).toString());
        cmbKonsen.setSelectedItem(table.getModel().getValueAt(table.getSelectedRow(), 5).toString());
        tNIM.setEditable(false);
        bSimpan.setEnabled(false);
        bUbah.setEnabled(true);
        bHapus.setEnabled(true);
    }//GEN-LAST:event_tableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new FrameMENU().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMHS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameMHS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCari;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bRefresh;
    private javax.swing.JButton bReset;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cmbKonsen;
    private javax.swing.JComboBox<String> cmbProdi;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea tAlamat;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tNIM;
    private javax.swing.JTextField tNama;
    private javax.swing.JTextField tNotelp;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
