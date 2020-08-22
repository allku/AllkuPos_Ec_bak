//    AllkuPOS Ec  - Touch Friendly Point Of Sale
//    https://www.allku.expert
//
//    This file is part of uniCenta oPOS
//
//    AllkuPOS Ec is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    AllkuPOS Ec is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with AllkuPOS Ec.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.admin;

import com.openbravo.basic.BasicException;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author jorgequiguango
 */
public class SequencesRefundEditor extends JPanel implements EditorRecord {

    private Object m_oId;

    private TransactionTableModel transactionModel;
    private List<PeopleNotSequence> peopleNotSequenceList;
    private DataLogicSales dlSales;

    /**
     * Creates new form taxEditor
     *
     * @param app
     * @param dirty
     */
    public SequencesRefundEditor(AppView app, DirtyManager dirty) {

        dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSales");
        initComponents();

        m_jName.getDocument().addDocumentListener(dirty);
        m_jRate.getDocument().addDocumentListener(dirty);

        writeValueEOF();
    }

    /**
     *
     * @throws BasicException
     */
    public void activate() throws BasicException {

    }

    /**
     *
     */
    @Override
    public void refresh() {

    }

    /**
     *
     */
    @Override
    public void writeValueEOF() {
        m_oId = null;
        m_jName.setText(null);
        m_jRate.setText(null);

        m_jName.setEnabled(false);
        m_jRate.setEnabled(false);

        jTablePeopleNotSequence.setEnabled(false);
    }

    /**
     *
     */
    @Override
    public void writeValueInsert() {
        m_oId = null;
        m_jName.setText(null);
        m_jRate.setText(null);

        m_jName.setEnabled(true);
        m_jRate.setEnabled(true);

    }

    /**
     *
     * @param value
     */
    @Override
    public void writeValueDelete(Object value) {

        Object[] tax = (Object[]) value;
        m_oId = tax[1];
        m_jName.setText(Formats.STRING.formatValue(tax[1]));
        m_jRate.setText(Formats.INT.formatValue(tax[0]));

        m_jName.setEnabled(false);
        m_jRate.setEnabled(false);

        transactionModel = new TransactionTableModel(
                getTransactionOfName((String) m_oId));
        jTablePeopleNotSequence.setModel(transactionModel);
        jTablePeopleNotSequence.setEnabled(false);
    }

    /**
     *
     * @param value
     */
    @Override
    public void writeValueEdit(Object value) {

        Object[] tax = (Object[]) value;
        m_oId = tax[1];
        m_jName.setText(Formats.STRING.formatValue(tax[1]));
        m_jRate.setText(Formats.INT.formatValue(tax[0]));

        m_jName.setEnabled(false);
        m_jRate.setEnabled(true);

        jTablePeopleNotSequence.setVisible(false);
        jTablePeopleNotSequence.setEnabled(true);
        resetTranxTable();

        jTablePeopleNotSequence.repaint();
    }

    public void resetTranxTable() {

        jTablePeopleNotSequence.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTablePeopleNotSequence.getColumnModel().getColumn(1).setPreferredWidth(70);

        // set font for headers
        Font f = new Font("Arial", Font.BOLD, 14);
        JTableHeader header = jTablePeopleNotSequence.getTableHeader();
        header.setFont(f);

        jTablePeopleNotSequence.getTableHeader().setReorderingAllowed(true);
        jTablePeopleNotSequence.setAutoCreateRowSorter(true);
        jTablePeopleNotSequence.repaint();

    }

    /**
     *
     * @return @throws BasicException
     */
    @Override
    public Object createValue() throws BasicException {

        Object[] tax = new Object[2];

        tax[1] = m_oId;
        tax[1] = m_jName.getText();
        tax[0] = Formats.INT.parseValue(m_jRate.getText());

        return tax;
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }

    private List<PeopleNotSequence> getTransactionOfName(String cId) {

        try {
            peopleNotSequenceList = dlSales
                    .getUserWithOutSequenceList(cId, "ticketsnum_refund");
        } catch (BasicException ex) {
            Logger.getLogger(SequencesRefundEditor.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        List<PeopleNotSequence> userWithOutSequenceList = new ArrayList<>();

        for (PeopleNotSequence peopleNotSequence : peopleNotSequenceList) {
            String visible = peopleNotSequence.getVisible();
            if (visible.equals(cId)) {
                userWithOutSequenceList.add(peopleNotSequence);
            }
        }

        repaint();
        refresh();

        return userWithOutSequenceList;
    }

    class TransactionTableModel extends AbstractTableModel {

        String serie = AppLocal.getIntString("label.tblHeaderCol1");
        String user = AppLocal.getIntString("label.tblHeaderCol2");

        List<PeopleNotSequence> transactionList;
        String[] columnNames = {serie, user};
        public Double Tamount;

        public TransactionTableModel(List<PeopleNotSequence> list) {
            transactionList = list;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return transactionList.size();
        }

        // this method is called to set the value of each cell
        @Override
        public Object getValueAt(int row, int column) {
            PeopleNotSequence peopleNotSequence = transactionList.get(row);

            jTablePeopleNotSequence.setRowHeight(25);

            switch (column) {

                case 0:
                    return peopleNotSequence.getPeopleId();
                case 1:
                    return peopleNotSequence.getPeopleName();
                default:
                    return "";

            }
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jName = new javax.swing.JTextField();
        m_jRate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTablePeopleNotSequence = new javax.swing.JTable();
        cmdUserWithOutSequence = new javax.swing.JButton();

        m_jName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jName.setPreferredSize(new java.awt.Dimension(0, 30));

        m_jRate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jRate.setPreferredSize(new java.awt.Dimension(0, 30));

        jLabel1.setText("Serie");

        jLabel4.setText("Secuencial");

        jLabel2.setText("Observación");

        jLabel3.setText("<html> <p> El secuencial es el número de la venta. Se debe ingresar el número secuencial de la venta menos uno ( n - 1 ) </p> </html>");

        jTablePeopleNotSequence.setAutoCreateRowSorter(true);
        jTablePeopleNotSequence.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTablePeopleNotSequence.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Serie", "Usuario"
            }
        ));
        jTablePeopleNotSequence.setToolTipText("");
        jTablePeopleNotSequence.setGridColor(new java.awt.Color(102, 204, 255));
        jTablePeopleNotSequence.setOpaque(false);
        jTablePeopleNotSequence.setPreferredSize(new java.awt.Dimension(375, 500));
        jTablePeopleNotSequence.setRowHeight(25);
        jTablePeopleNotSequence.setShowVerticalLines(false);

        cmdUserWithOutSequence.setText("Usuarios sin secuencial");
        cmdUserWithOutSequence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUserWithOutSequenceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(cmdUserWithOutSequence))
                .addGap(103, 103, 103)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTablePeopleNotSequence, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(m_jRate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(m_jName, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdUserWithOutSequence)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jTablePeopleNotSequence, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(227, Short.MAX_VALUE))
        );

    }// </editor-fold>//GEN-END:initComponents

    private void cmdUserWithOutSequenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUserWithOutSequenceActionPerformed
        String cId = "1";
        if (cId != null) {
            transactionModel = new TransactionTableModel(getTransactionOfName(cId));
            jTablePeopleNotSequence.setModel(transactionModel);
            if (transactionModel.getRowCount() > 0) {
                jTablePeopleNotSequence.setVisible(true);
                String TranCount = String.valueOf(transactionModel.getRowCount());
                System.out.println(TranCount + " for " + m_jName.getText());
            } else {
                jTablePeopleNotSequence.setVisible(false);
                JOptionPane.showMessageDialog(null, "No existen usuarios sin secuencia", "Usuarios", JOptionPane.INFORMATION_MESSAGE);
            }
            resetTranxTable();
        }
    }//GEN-LAST:event_cmdUserWithOutSequenceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdUserWithOutSequence;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTable jTablePeopleNotSequence;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jRate;
    // End of variables declaration//GEN-END:variables

}
