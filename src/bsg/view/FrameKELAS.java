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
public class FrameKELAS extends javax.swing.JFrame {
    Connection connection;
    DefaultTableModel model;
    /**
     * Creates new form FrameKELAS
     */
    public FrameKELAS() {
        initComponents();
        connection = koneksi.getConnection();
        getDataTable();
        getCmbProdi();
        getCmbKonsen();
        tIDKelas.requestFocus();
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
            String sql = "SELECT * FROM kelas,prodi,konsentrasi "
                    + "WHERE kelas.id_prodi=prodi.id_prodi AND "
                    + "kelas.id_konsentrasi=konsentrasi.id_konsentrasi";
            ResultSet res = stat.executeQuery(sql);
            while(res.next ()) {
                Object[ ] obj = new Object[4];
                obj[0] = res.getString("id_kelas");
                obj[1] = res.getString("nama_kelas");
                obj[2] = res.getString("nama_prodi");
                obj[3] = res.getString("nama_konsen");
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
        tIDKelas.setEditable(true);
    }
    
    private void reset() {
        tIDKelas.setText("");
        tNama.setText("");
        tIDKelas.setEditable(true);
        bSimpan.setEnabled(true);
        bUbah.setEnabled(false);
        bHapus.setEnabled(false);
    }
    
    private void insert() {
        PreparedStatement statement = null;
        String sql = "INSERT INTO kelas (id_kelas,nama_kelas,id_prodi,id_konsentrasi) VALUES(?,?,?,?);";
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tIDKelas.getText());
            statement.setString(2, tNama.getText());
            statement.setInt(3, getIDProdi(cmbProdi.getSelectedItem().toString()));
            statement.setInt(4, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
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
        String sql = "UPDATE kelas SET nama_kelas=?,id_prodi=?,id_konsentrasi=? WHERE id_kelas=?";
        try {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tNama.getText());
            statement.setInt(2, getIDProdi(cmbProdi.getSelectedItem().toString()));
            statement.setInt(3, getIDKonsen(cmbKonsen.getSelectedItem().toString()));
            statement.setString(4, tIDKelas.getText());
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
        String sql = "DELETE FROM kelas WHERE id_kelas=?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, tIDKelas.getText());
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
            String sql = "SELECT * FROM kelas,prodi,konsentrasi WHERE "
                    + "kelas.id_prodi=prodi.id_prodi AND kelas.id_konsentrasi=konsentrasi.id_konsentrasi "
                    + "AND nama_kelas LIKE ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + tCari.getText() + "%");
            ResultSet res = statement.executeQuery();
            while (res.next ()) {
                Object[ ] obj = new Object[6];
                obj[0] = res.getString("id_kelas");
                obj[1] = res.getString("nama_kelas");
                obj[2] = res.getString("nama_prodi");
                obj[3] = res.getString("nama_konsen");
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
        tIDKelas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tNama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tCari = new javax.swing.JTextField();
        cmbProdi = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbKonsen = new javax.swing.JComboBox<>();
        bSimpan = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bReset = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        bCari = new javax.swing.JButton();
        bRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        bSimpan1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Kelas");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 255));
        jLabel1.setText("Data Kelas");

        jLabel2.setText("ID Kelas");

        jLabel3.setText("Nama Kelas");

        jLabel4.setText("Program Studi");

        cmbProdi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Konsentrasi");

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

        jLabel6.setText("Cari Data");

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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID Kelas", "Nama Kelas", "Prodi", "Konsentrasi"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        bSimpan1.setText("Menu");
        bSimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpan1ActionPerformed(evt);
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
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cmbKonsen, 0, 200, Short.MAX_VALUE)
                                        .addComponent(cmbProdi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tIDKelas, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tNama, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(bHapus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bSimpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(bReset, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jLabel5))
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tCari)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bCari, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bRefresh))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bSimpan1)
                                .addGap(184, 184, 184)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(bSimpan1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tIDKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bCari)
                    .addComponent(bRefresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbProdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbKonsen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSimpan)
                            .addComponent(bUbah))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bHapus)
                            .addComponent(bReset)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        // TODO add your handling code here:
        if(!tIDKelas.getText().trim().isEmpty() && !tNama.getText().trim().isEmpty()) {
            insert();
            refresh();
            reset();
            JOptionPane.showMessageDialog(this, "Kelas berhasil ditambahkan", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Lengkapi form terlebih dahulu", "Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        // TODO add your handling code here:
        int alert = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus kelas ini?", "Notifikasi", JOptionPane.YES_NO_OPTION);
        if (alert == JOptionPane.YES_OPTION) {
            delete();
            refresh();
            reset();
            JOptionPane.showMessageDialog(this, "kelas berhasil dihapus", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_bHapusActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        // TODO add your handling code here:
        update();
        refresh();
        reset();
        JOptionPane.showMessageDialog(this, "Kelas berhasil diubah", "Notifikasi", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Masukkan nama kelas yang ingin dicari", "Notifikasi", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bCariActionPerformed

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
        // TODO add your handling code here:
        refresh();
    }//GEN-LAST:event_bRefreshActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        // TODO add your handling code here:
        tIDKelas.setText(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
        tNama.setText(table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
        cmbProdi.setSelectedItem(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
        cmbKonsen.setSelectedItem(table.getModel().getValueAt(table.getSelectedRow(), 3).toString());
        tIDKelas.setEditable(false);
        bSimpan.setEnabled(false);
        bUbah.setEnabled(true);
        bHapus.setEnabled(true);
    }//GEN-LAST:event_tableMouseClicked

    private void bSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpan1ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        new FrameMENU().setVisible(true);
    }//GEN-LAST:event_bSimpan1ActionPerformed

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
            java.util.logging.Logger.getLogger(FrameKELAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameKELAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameKELAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameKELAS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameKELAS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCari;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bRefresh;
    private javax.swing.JButton bReset;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bSimpan1;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cmbKonsen;
    private javax.swing.JComboBox<String> cmbProdi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tIDKelas;
    private javax.swing.JTextField tNama;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
