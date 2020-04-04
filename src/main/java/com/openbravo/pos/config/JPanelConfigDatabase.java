//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2018 uniCenta
//    https://unicenta.com
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.config;

import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Session;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DriverWrapper;
import com.openbravo.pos.util.AltEncrypter;
import com.openbravo.pos.util.DirectoryEvent;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Jack Gerrard
 * @author adrianromero
 */
public class JPanelConfigDatabase extends javax.swing.JPanel implements PanelConfig {
    
    private final DirtyManager dirty = new DirtyManager();
    
    /** Creates new form JPanelConfigDatabase */
    public JPanelConfigDatabase() {
        
        initComponents();
        
        webLinkLabel1.setLink("https://unicenta.com");
        webLinkLabel2.setText("Make sure you have created your database Schema and Username before completing the details below");
        webLinkLabel2.setLink("https://unicenta.com/pages/configure-unicenta-opos/#database");        
        
        jtxtDbDriverLib.getDocument().addDocumentListener(dirty);
        jtxtDbDriver.getDocument().addDocumentListener(dirty);
        jbtnDbDriverLib.addActionListener(new DirectoryEvent(jtxtDbDriverLib));
        jcboDBDriver.addActionListener(dirty);
        jcboDBDriver.addItem("MySQL");
        jcboDBDriver.setSelectedIndex(0);
        multiDB.addActionListener(dirty);    
        
// primary DB        
        jtxtDbName.getDocument().addDocumentListener(dirty);
        jtxtDbURL.getDocument().addDocumentListener(dirty);
        jtxtDbSchema.getDocument().addDocumentListener(dirty);        
        jtxtDbOptions.getDocument().addDocumentListener(dirty);
        jtxtDbPassword.getDocument().addDocumentListener(dirty);
        jtxtDbUser.getDocument().addDocumentListener(dirty);        
        jCBSchema.addActionListener(dirty);         
        
// secondary DB        
        jtxtDbName1.getDocument().addDocumentListener(dirty);        
        jtxtDbURL1.getDocument().addDocumentListener(dirty);
        jtxtDbSchema1.getDocument().addDocumentListener(dirty);                
        jtxtDbOptions1.getDocument().addDocumentListener(dirty);
        jtxtDbPassword1.getDocument().addDocumentListener(dirty);
        jtxtDbUser1.getDocument().addDocumentListener(dirty);      
        jCBSchema1.addActionListener(dirty);         
        
        jCBSchema.setEnabled(false);         
        jCBSchema1.setEnabled(false);         

    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasChanged() {
        return dirty.isDirty();
    }
    
    /**
     *
     * @return
     */
    @Override
    public Component getConfigComponent() {
        return this;
    }
   
    /**
     *
     * @param config
     */
    @Override
    public void loadProperties(AppConfig config) {
        
        multiDB.setSelected(Boolean.parseBoolean(config.getProperty("db.multi")));                

        jcboDBDriver.setSelectedItem(config.getProperty("db.engine"));
        jtxtDbDriverLib.setText(config.getProperty("db.driverlib"));
        jtxtDbDriver.setText(config.getProperty("db.driver"));

// primary DB              
        jtxtDbName.setText(config.getProperty("db.name"));
        jtxtDbURL.setText("jdbc:mysql://localhost:3306/");
        jtxtDbURL.setText(config.getProperty("db.URL"));    
        jtxtDbSchema.setText(config.getProperty("db.schema"));        
        jtxtDbOptions.setText(config.getProperty("db.options"));
        String sDBUser = config.getProperty("db.user");
        String sDBPassword = config.getProperty("db.password");        

        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }        
        jtxtDbUser.setText(sDBUser);
        jtxtDbPassword.setText(sDBPassword);   

// secondary DB        
        jtxtDbName1.setText(config.getProperty("db1.name"));
        jtxtDbURL1.setText(config.getProperty("db1.URL"));
        jtxtDbSchema1.setText(config.getProperty("db1.schema"));                
        jtxtDbOptions1.setText(config.getProperty("db1.options"));        
        String sDBUser1 = config.getProperty("db1.user");
        String sDBPassword1 = config.getProperty("db1.password");        

        if (sDBUser1 != null && sDBPassword1 != null && sDBPassword1.startsWith("crypt:")) {
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser1);
            sDBPassword1 = cypher.decrypt(sDBPassword1.substring(6));
        }        
        jtxtDbUser1.setText(sDBUser1);
        jtxtDbPassword1.setText(sDBPassword1);          
        
        dirty.setDirty(false);
    }
   
    /**
     *
     * @param config
     */
    @Override
    public void saveProperties(AppConfig config) {

// multi-db        
        config.setProperty("db.multi",Boolean.toString(multiDB.isSelected()));
        
        config.setProperty("db.engine", comboValue(jcboDBDriver.getSelectedItem()));
        config.setProperty("db.driverlib", jtxtDbDriverLib.getText());
        config.setProperty("db.driver", jtxtDbDriver.getText());

// primary DB
        config.setProperty("db.name", jtxtDbName.getText());
        config.setProperty("db.URL", jtxtDbURL.getText());
        config.setProperty("db.schema", jtxtDbSchema.getText());        
        config.setProperty("db.options", jtxtDbOptions.getText());
        config.setProperty("db.user", jtxtDbUser.getText());
        AltEncrypter cypher = new AltEncrypter("cypherkey" + jtxtDbUser.getText());       
        config.setProperty("db.password", "crypt:" + 
                cypher.encrypt(new String(jtxtDbPassword.getPassword())));

// secondary DB        
        config.setProperty("db1.name", jtxtDbName1.getText());        
        config.setProperty("db1.URL", jtxtDbURL1.getText());
        config.setProperty("db1.schema", jtxtDbSchema1.getText());                
        config.setProperty("db1.options", jtxtDbOptions1.getText());
        config.setProperty("db1.user", jtxtDbUser1.getText());
        cypher = new AltEncrypter("cypherkey" + jtxtDbUser1.getText());       
        config.setProperty("db1.password", "crypt:" + 
                cypher.encrypt(new String(jtxtDbPassword1.getPassword())));        

        dirty.setDirty(false);
    }

    private String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }
    
    public void fillSchema() {
        /* Use existing session credentials but declare new session and connection 
         * to keep separated from current session instance as database could
         * be a different server
        */

        if (jCBSchema.getItemCount() >= 1 ) {
            jCBSchema.removeAllItems();
        }        
    
        try {
            String driverlib = jtxtDbDriverLib.getText();
            String driver = jtxtDbDriver.getText();
            String url = jtxtDbURL.getText() + jtxtDbSchema.getText() + jtxtDbOptions.getText();
            String user = jtxtDbUser.getText();
            String password = new String(jtxtDbPassword.getPassword());

            ClassLoader cloader = new URLClassLoader(new URL[]{new File(driverlib).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(driver, true, cloader).newInstance()));

            Session session1 =  new Session(url, user, password);
            Connection connection1 = session1.getConnection();
            ResultSet rs = connection1.getMetaData().getCatalogs();

            while (rs.next()) {
                jCBSchema.addItem(rs.getString("TABLE_CAT"));
            }
            
            jCBSchema.setEnabled(true);
            jCBSchema.setSelectedIndex(0);
            
        } catch (MalformedURLException | ClassNotFoundException | SQLException 
                | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(JPanelConfigDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    public void fillSchema1() {
        /* Use existing session credentials but declare new session and connection 
         * to keep separated from current session instance as database could
         * be a different server
        */

        if (jCBSchema1.getItemCount() >= 1 ) {
            jCBSchema1.removeAllItems();
        }        
        
        try {
            String driverlib = jtxtDbDriverLib.getText();
            String driver = jtxtDbDriver.getText();
            String url = jtxtDbURL.getText() + jtxtDbSchema.getText() + jtxtDbOptions.getText();
            String user = jtxtDbUser.getText();
            String password = new String(jtxtDbPassword.getPassword());

            ClassLoader cloader = new URLClassLoader(new URL[]{new File(driverlib).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(driver, true, cloader).newInstance()));

            Session session =  new Session(url, user, password);
            Connection connection = session.getConnection();
            ResultSet rs = connection.getMetaData().getCatalogs();

            while (rs.next()) {
                jCBSchema1.addItem(rs.getString("TABLE_CAT"));
            }

            jCBSchema1.setEnabled(true);
            jCBSchema1.setSelectedIndex(0);            

        } catch (MalformedURLException | ClassNotFoundException | SQLException 
                | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(JPanelConfigDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        webPopOver1 = new com.alee.extended.window.WebPopOver();
        jLabel6 = new javax.swing.JLabel();
        jcboDBDriver = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jtxtDbDriverLib = new javax.swing.JTextField();
        jbtnDbDriverLib = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jtxtDbDriver = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtxtDbURL = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtDbUser = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtDbPassword = new javax.swing.JPasswordField();
        jButtonTest = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jButtonTest1 = new javax.swing.JButton();
        jtxtDbPassword1 = new javax.swing.JPasswordField();
        jtxtDbUser1 = new javax.swing.JTextField();
        jtxtDbURL1 = new javax.swing.JTextField();
        jLblDbURL1 = new javax.swing.JLabel();
        jLblDbUser1 = new javax.swing.JLabel();
        jLblDbPassword1 = new javax.swing.JLabel();
        jLblDBName = new javax.swing.JLabel();
        jtxtDbName = new javax.swing.JTextField();
        jLblDbName1 = new javax.swing.JLabel();
        jtxtDbName1 = new javax.swing.JTextField();
        LblMultiDB = new com.alee.laf.label.WebLabel();
        multiDB = new com.alee.extended.button.WebSwitch();
        jLabel7 = new javax.swing.JLabel();
        jtxtDbSchema = new javax.swing.JTextField();
        jLblDbSchema1 = new javax.swing.JLabel();
        jtxtDbSchema1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtxtDbOptions = new javax.swing.JTextField();
        jLblDbOptions1 = new javax.swing.JLabel();
        jtxtDbOptions1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jBtnReset = new javax.swing.JButton();
        jBtnReset1 = new javax.swing.JButton();
        jCBSchema = new javax.swing.JComboBox<>();
        jCBSchema1 = new javax.swing.JComboBox<>();
        webLinkLabel2 = new com.alee.extended.label.WebLinkLabel();
        webLinkLabel1 = new com.alee.extended.label.WebLinkLabel();

        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(900, 500));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
        jLabel6.setText(bundle.getString("label.Database")); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 172, -1, -1));

        jcboDBDriver.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jcboDBDriver.setPreferredSize(new java.awt.Dimension(150, 30));
        jcboDBDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboDBDriverActionPerformed(evt);
            }
        });
        add(jcboDBDriver, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 172, -1, -1));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel18.setText(AppLocal.getIntString("label.dbdriverlib")); // NOI18N
        jLabel18.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 133, -1, 25));

        jtxtDbDriverLib.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbDriverLib.setPreferredSize(new java.awt.Dimension(500, 30));
        add(jtxtDbDriverLib, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 133, -1, -1));

        jbtnDbDriverLib.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileopen.png"))); // NOI18N
        jbtnDbDriverLib.setText("  ");
        jbtnDbDriverLib.setToolTipText("");
        jbtnDbDriverLib.setMaximumSize(new java.awt.Dimension(64, 32));
        jbtnDbDriverLib.setMinimumSize(new java.awt.Dimension(64, 32));
        jbtnDbDriverLib.setPreferredSize(new java.awt.Dimension(80, 35));
        add(jbtnDbDriverLib, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 128, -1, 40));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("label.DbDriver")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 30));
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 172, -1, -1));

        jtxtDbDriver.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbDriver.setPreferredSize(new java.awt.Dimension(150, 30));
        jtxtDbDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtDbDriverActionPerformed(evt);
            }
        });
        add(jtxtDbDriver, new org.netbeans.lib.awtextra.AbsoluteConstraints(398, 173, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText(AppLocal.getIntString("label.DbURL")); // NOI18N
        jLabel2.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 274, -1, -1));

        jtxtDbURL.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbURL.setPreferredSize(new java.awt.Dimension(275, 30));
        add(jtxtDbURL, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 274, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText(AppLocal.getIntString("label.DbUser")); // NOI18N
        jLabel3.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 311, -1, -1));

        jtxtDbUser.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbUser.setPreferredSize(new java.awt.Dimension(150, 30));
        add(jtxtDbUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 312, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText(AppLocal.getIntString("label.DbPassword")); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 348, -1, -1));

        jtxtDbPassword.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbPassword.setPreferredSize(new java.awt.Dimension(150, 30));
        add(jtxtDbPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 348, -1, -1));

        jButtonTest.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonTest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/database.png"))); // NOI18N
        jButtonTest.setText(bundle.getString("button.test")); // NOI18N
        jButtonTest.setActionCommand(bundle.getString("Button.Test")); // NOI18N
        jButtonTest.setPreferredSize(new java.awt.Dimension(110, 45));
        jButtonTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTestActionPerformed(evt);
            }
        });
        add(jButtonTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(299, 324, -1, -1));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 115, 880, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/uniCenta_logo_vert_100.png"))); // NOI18N
        jLabel5.setText(bundle.getString("message.DBDefault")); // NOI18N
        jLabel5.setToolTipText("");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel5.setPreferredSize(new java.awt.Dimension(889, 120));
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 880, 80));

        jButtonTest1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButtonTest1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/database.png"))); // NOI18N
        jButtonTest1.setText(bundle.getString("button.test")); // NOI18N
        jButtonTest1.setActionCommand(bundle.getString("Button.Test")); // NOI18N
        jButtonTest1.setEnabled(false);
        jButtonTest1.setPreferredSize(new java.awt.Dimension(110, 45));
        jButtonTest1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTest1ActionPerformed(evt);
            }
        });
        add(jButtonTest1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 320, -1, -1));

        jtxtDbPassword1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbPassword1.setEnabled(false);
        jtxtDbPassword1.setPreferredSize(new java.awt.Dimension(150, 30));
        add(jtxtDbPassword1, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 349, -1, -1));

        jtxtDbUser1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbUser1.setEnabled(false);
        jtxtDbUser1.setPreferredSize(new java.awt.Dimension(150, 30));
        add(jtxtDbUser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 312, -1, -1));

        jtxtDbURL1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbURL1.setEnabled(false);
        jtxtDbURL1.setPreferredSize(new java.awt.Dimension(275, 30));
        add(jtxtDbURL1, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 275, -1, -1));

        jLblDbURL1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLblDbURL1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLblDbURL1.setText(AppLocal.getIntString("label.DbURL")); // NOI18N
        jLblDbURL1.setEnabled(false);
        jLblDbURL1.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLblDbURL1, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 274, -1, -1));

        jLblDbUser1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLblDbUser1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLblDbUser1.setText(AppLocal.getIntString("label.DbUser")); // NOI18N
        jLblDbUser1.setEnabled(false);
        jLblDbUser1.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLblDbUser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 311, -1, -1));

        jLblDbPassword1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLblDbPassword1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLblDbPassword1.setText(AppLocal.getIntString("label.DbPassword")); // NOI18N
        jLblDbPassword1.setEnabled(false);
        jLblDbPassword1.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLblDbPassword1, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 348, -1, -1));

        jLblDBName.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLblDBName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLblDBName.setText(AppLocal.getIntString("label.DbName")); // NOI18N
        jLblDBName.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLblDBName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 237, -1, -1));

        jtxtDbName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbName.setPreferredSize(new java.awt.Dimension(275, 30));
        add(jtxtDbName, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 238, -1, -1));

        jLblDbName1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLblDbName1.setText(AppLocal.getIntString("label.DbName1")); // NOI18N
        jLblDbName1.setEnabled(false);
        jLblDbName1.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLblDbName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 237, -1, -1));

        jtxtDbName1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbName1.setEnabled(false);
        jtxtDbName1.setPreferredSize(new java.awt.Dimension(275, 30));
        add(jtxtDbName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(568, 238, -1, -1));

        LblMultiDB.setText(AppLocal.getIntString("label.multidb")); // NOI18N
        LblMultiDB.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LblMultiDB.setPreferredSize(new java.awt.Dimension(125, 30));
        add(LblMultiDB, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 172, 80, -1));

        multiDB.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        multiDB.setPreferredSize(new java.awt.Dimension(80, 30));
        multiDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiDBActionPerformed(evt);
            }
        });
        add(multiDB, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 172, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText(AppLocal.getIntString("label.DbSchema")); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, -1, -1));

        jtxtDbSchema.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbSchema.setPreferredSize(new java.awt.Dimension(0, 0));
        add(jtxtDbSchema, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 280, 30));

        jLblDbSchema1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLblDbSchema1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLblDbSchema1.setText(AppLocal.getIntString("label.DbSchema")); // NOI18N
        jLblDbSchema1.setEnabled(false);
        jLblDbSchema1.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLblDbSchema1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 390, -1, -1));

        jtxtDbSchema1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbSchema1.setEnabled(false);
        jtxtDbSchema1.setPreferredSize(new java.awt.Dimension(0, 0));
        add(jtxtDbSchema1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 480, 280, 30));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText(AppLocal.getIntString("label.DbOptions")); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, -1, -1));

        jtxtDbOptions.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbOptions.setPreferredSize(new java.awt.Dimension(275, 30));
        add(jtxtDbOptions, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 430, -1, -1));

        jLblDbOptions1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLblDbOptions1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLblDbOptions1.setText(AppLocal.getIntString("label.DbOptions")); // NOI18N
        jLblDbOptions1.setEnabled(false);
        jLblDbOptions1.setPreferredSize(new java.awt.Dimension(125, 30));
        add(jLblDbOptions1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 430, -1, -1));

        jtxtDbOptions1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jtxtDbOptions1.setEnabled(false);
        jtxtDbOptions1.setPreferredSize(new java.awt.Dimension(275, 30));
        add(jtxtDbOptions1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 430, -1, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 204, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Make sure you have created your database Schema and Username before completing the details below");
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, -1, -1));

        jBtnReset.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jBtnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        jBtnReset.setText("Reset");
        jBtnReset.setPreferredSize(new java.awt.Dimension(110, 45));
        jBtnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnResetActionPerformed(evt);
            }
        });
        add(jBtnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 470, -1, -1));

        jBtnReset1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jBtnReset1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        jBtnReset1.setText("Reset");
        jBtnReset1.setEnabled(false);
        jBtnReset1.setPreferredSize(new java.awt.Dimension(110, 45));
        jBtnReset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnReset1ActionPerformed(evt);
            }
        });
        add(jBtnReset1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 470, -1, -1));

        jCBSchema.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jCBSchema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBSchemaActionPerformed(evt);
            }
        });
        add(jCBSchema, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 390, 275, 28));

        jCBSchema1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jCBSchema1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBSchema1ActionPerformed(evt);
            }
        });
        add(jCBSchema1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 390, 275, 28));

        webLinkLabel2.setText(AppLocal.getIntString("label.configURL")); // NOI18N
        webLinkLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        webLinkLabel2.setLink("https://unicenta.com/pages/configure-unicenta-opos/");
        webLinkLabel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webLinkLabel2ActionPerformed(evt);
            }
        });
        add(webLinkLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, -1, -1));

        webLinkLabel1.setText("https://unicenta.com");
        webLinkLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        webLinkLabel1.setLink("https://unicenta.com");
        webLinkLabel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webLinkLabel1ActionPerformed(evt);
            }
        });
        add(webLinkLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtDbDriverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtDbDriverActionPerformed

    }//GEN-LAST:event_jtxtDbDriverActionPerformed

    private void jcboDBDriverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboDBDriverActionPerformed

        String dirname = System.getProperty("dirname.path");
        dirname = dirname == null ? "./" : dirname;
       
        if ("PostgreSQL".equals(jcboDBDriver.getSelectedItem())) {
            jtxtDbDriverLib.setText(new File(new File(dirname), "lib/postgresql-9.4-1208.jdbc4.jar").getAbsolutePath());
            jtxtDbDriver.setText("org.postgresql.Driver");
            jtxtDbURL.setText("jdbc:postgresql://localhost:5432/");            
            jtxtDbSchema.setText("unicentaopos");
            jtxtDbOptions.setText("");
        } else {
            jtxtDbDriverLib.setText(new File(new File(dirname), "lib/mysql-connector-java-5.1.39.jar").getAbsolutePath());
            jtxtDbDriver.setText("com.mysql.jdbc.Driver");            
            jtxtDbURL.setText("jdbc:mysql://localhost:3306/");
            jtxtDbSchema.setText("unicentaopos");                                    
            jtxtDbOptions.setText("?zeroDateTimeBehavior=convertToNull");
        }    
    }//GEN-LAST:event_jcboDBDriverActionPerformed

    private void jButtonTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTestActionPerformed
        try {
            String driverlib = jtxtDbDriverLib.getText();
            String driver = jtxtDbDriver.getText();
            String url = jtxtDbURL.getText() + jtxtDbSchema.getText() + jtxtDbOptions.getText();
            String user = jtxtDbUser.getText();
            String password = new String(jtxtDbPassword.getPassword());

            ClassLoader cloader = new URLClassLoader(new URL[]{new File(driverlib).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(driver, true, cloader).newInstance()));

            Session session =  new Session(url, user, password);
            Connection connection = session.getConnection();

            boolean isValid;
            isValid = (connection == null) ? false : connection.isValid(1000);

            if (isValid) {
                JOptionPane.showMessageDialog(this, 
                        AppLocal.getIntString("message.databasesuccess"), 
                        "Connection Test", JOptionPane.INFORMATION_MESSAGE);
                fillSchema();
                jCBSchema.setEnabled(true);
            } else {
                JMessageDialog.showMessage(this, 
                        new MessageInf(MessageInf.SGN_WARNING, "Connection Error"));
            }
        } catch (InstantiationException | IllegalAccessException | MalformedURLException | ClassNotFoundException e) {
            JMessageDialog.showMessage(this, 
                    new MessageInf(MessageInf.SGN_WARNING, 
                            AppLocal.getIntString("message.databasedrivererror"), e));
        } catch (SQLException e) {
            JMessageDialog.showMessage(this, 
                    new MessageInf(MessageInf.SGN_WARNING, 
                            AppLocal.getIntString("message.databaseconnectionerror"), e));            
        } catch (Exception e) {
            JMessageDialog.showMessage(this, 
                    new MessageInf(MessageInf.SGN_WARNING, "Unknown exception", e));
        }
    }//GEN-LAST:event_jButtonTestActionPerformed

    private void jButtonTest1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTest1ActionPerformed
        try {
            String driverlib = jtxtDbDriverLib.getText();
            String driver = jtxtDbDriver.getText();
            String url = jtxtDbURL1.getText() + jtxtDbSchema1.getText() + jtxtDbOptions1.getText();
            String user = jtxtDbUser1.getText();
            String password = new String(jtxtDbPassword1.getPassword());

            ClassLoader cloader = new URLClassLoader(new URL[]{new File(driverlib).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(driver, true, cloader).newInstance()));

            Session session =  new Session(url, user, password);
            Connection connection = session.getConnection();
            boolean isValid;
            isValid = (connection == null) ? false : connection.isValid(1000);

            if (isValid) {
                JOptionPane.showMessageDialog(this, 
                        AppLocal.getIntString("message.databasesuccess"), 
                        "Connection Test", JOptionPane.INFORMATION_MESSAGE);
                fillSchema1();
                jCBSchema1.setEnabled(true);                
            } else {
                JMessageDialog.showMessage(this, 
                        new MessageInf(MessageInf.SGN_WARNING, "Connection Error"));
            }
        } catch (InstantiationException | IllegalAccessException | MalformedURLException | ClassNotFoundException e) {
            JMessageDialog.showMessage(this, 
                    new MessageInf(MessageInf.SGN_WARNING, 
                            AppLocal.getIntString("message.databasedrivererror"), e));
        } catch (SQLException e) {
            JMessageDialog.showMessage(this, 
                    new MessageInf(MessageInf.SGN_WARNING, 
                            AppLocal.getIntString("message.databaseconnectionerror"), e));
        } catch (Exception e) {
            JMessageDialog.showMessage(this, 
                    new MessageInf(MessageInf.SGN_WARNING, "Unknown exception", e));
        }
    }//GEN-LAST:event_jButtonTest1ActionPerformed

    private void multiDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiDBActionPerformed
        if (multiDB.isSelected()) {
            jLblDbName1.setEnabled(true);
            jtxtDbName1.setEnabled(true);
            jLblDbOptions1.setEnabled(true);
            jtxtDbOptions1.setEnabled(true);  
            jLblDbURL1.setEnabled(true);
            jtxtDbURL1.setEnabled(true);            
            jLblDbSchema1.setEnabled(true);
            jtxtDbSchema1.setEnabled(true);            
            jLblDbUser1.setEnabled(true);
            jtxtDbUser1.setEnabled(true);
            jLblDbPassword1.setEnabled(true);
            jtxtDbPassword1.setEnabled(true);
            jButtonTest1.setEnabled(true);
            jBtnReset1.setEnabled(true);            
        } else {
            jLblDbName1.setEnabled(false);
            jtxtDbName1.setEnabled(false);
            jLblDbOptions1.setEnabled(false);
            jtxtDbOptions1.setEnabled(false);  
            jLblDbURL1.setEnabled(false);
            jtxtDbURL1.setEnabled(false);
            jLblDbSchema1.setEnabled(false);
            jtxtDbSchema1.setEnabled(false);              
            jLblDbUser1.setEnabled(false);
            jtxtDbUser1.setEnabled(false);
            jLblDbPassword1.setEnabled(false);
            jtxtDbPassword1.setEnabled(false);            
            jButtonTest1.setEnabled(false);  
            jBtnReset1.setEnabled(false);
        }
    }//GEN-LAST:event_multiDBActionPerformed

    private void jBtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnResetActionPerformed
        if (jCBSchema.getItemCount() >= 1 ) {
            jCBSchema.removeAllItems();
        } 
//        loadProperties(AppConfig.getInstance());
        String dirname = System.getProperty("dirname.path");
        dirname = dirname == null ? "./" : dirname;
        
        jtxtDbDriverLib.setText(new File(new File(dirname), "lib/mysql-connector-java-5.1.39.jar").getAbsolutePath());
        jtxtDbDriver.setText("com.mysql.jdbc.Driver");            

        jtxtDbName.setText("Main DB");
        jtxtDbURL.setText("jdbc:mysql://localhost:3306/");
        jtxtDbSchema.setText("unicentaopos");
        jtxtDbOptions.setText("?zeroDateTimeBehavior=convertToNull");
        jtxtDbUser.setText(null);        
        jtxtDbPassword.setText(null);        
    }//GEN-LAST:event_jBtnResetActionPerformed

    private void jBtnReset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnReset1ActionPerformed
        jtxtDbName1.setText("Other DB");
        jtxtDbURL1.setText("jdbc:mysql://localhost:3306/");
        jtxtDbSchema1.setText("unicentaopos1");
        jtxtDbOptions1.setText("?zeroDateTimeBehavior=convertToNull");
        jtxtDbUser1.setText(null);        
        jtxtDbPassword1.setText(null);
    }//GEN-LAST:event_jBtnReset1ActionPerformed

    private void jCBSchemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBSchemaActionPerformed

        if (jCBSchema.getItemCount() > 0 ) {
            String selected = jCBSchema.getSelectedItem().toString();            
            if(!selected.equals(null)) {
                jtxtDbSchema.setText(selected);            
            }
        }
    }//GEN-LAST:event_jCBSchemaActionPerformed

    private void jCBSchema1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBSchema1ActionPerformed
        
        if (jCBSchema1.getItemCount() > 0 ) {
            String selected1 = jCBSchema1.getSelectedItem().toString();            
            if(!selected1.equals(null)) {
                jtxtDbSchema1.setText(selected1);            
            }
        }
    }//GEN-LAST:event_jCBSchema1ActionPerformed

    private void webLinkLabel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webLinkLabel2ActionPerformed
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI("https://unicenta.com");
            desktop.browse(oURL);
        } catch (IOException | URISyntaxException e) {
        }
    }//GEN-LAST:event_webLinkLabel2ActionPerformed

    private void webLinkLabel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webLinkLabel1ActionPerformed
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI("https://unicenta.com/pages/configure-unicenta-opos/#database");
            desktop.browse(oURL);
        } catch (IOException | URISyntaxException e) {
        }
    }//GEN-LAST:event_webLinkLabel1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.alee.laf.label.WebLabel LblMultiDB;
    private javax.swing.JButton jBtnReset;
    private javax.swing.JButton jBtnReset1;
    private javax.swing.JButton jButtonTest;
    private javax.swing.JButton jButtonTest1;
    private javax.swing.JComboBox<String> jCBSchema;
    private javax.swing.JComboBox<String> jCBSchema1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLblDBName;
    private javax.swing.JLabel jLblDbName1;
    private javax.swing.JLabel jLblDbOptions1;
    private javax.swing.JLabel jLblDbPassword1;
    private javax.swing.JLabel jLblDbSchema1;
    private javax.swing.JLabel jLblDbURL1;
    private javax.swing.JLabel jLblDbUser1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnDbDriverLib;
    private javax.swing.JComboBox jcboDBDriver;
    private javax.swing.JTextField jtxtDbDriver;
    private javax.swing.JTextField jtxtDbDriverLib;
    private javax.swing.JTextField jtxtDbName;
    private javax.swing.JTextField jtxtDbName1;
    private javax.swing.JTextField jtxtDbOptions;
    private javax.swing.JTextField jtxtDbOptions1;
    private javax.swing.JPasswordField jtxtDbPassword;
    private javax.swing.JPasswordField jtxtDbPassword1;
    private javax.swing.JTextField jtxtDbSchema;
    private javax.swing.JTextField jtxtDbSchema1;
    private javax.swing.JTextField jtxtDbURL;
    private javax.swing.JTextField jtxtDbURL1;
    private javax.swing.JTextField jtxtDbUser;
    private javax.swing.JTextField jtxtDbUser1;
    private com.alee.extended.button.WebSwitch multiDB;
    private com.alee.extended.label.WebLinkLabel webLinkLabel1;
    private com.alee.extended.label.WebLinkLabel webLinkLabel2;
    private com.alee.extended.window.WebPopOver webPopOver1;
    // End of variables declaration//GEN-END:variables

     
}
