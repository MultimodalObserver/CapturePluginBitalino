package capturepluginbitalino;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mo.core.ui.GridBConstraints;
import mo.core.ui.Utils;
import mo.organization.ProjectOrganization;

public class BitalinoCaptureConfigurationDialog extends JDialog implements DocumentListener {

    JLabel errorLabel;
    JLabel srValue;
    JSlider sSR;
    JTextField nameField;
    JButton accept;
    ProjectOrganization org;
    
    JCheckBox EMG;
    JCheckBox ECG;
    JCheckBox EDA;
    public int sensor_rec;
    int SR;
    ResourceBundle dialogBundle = java.util.ResourceBundle.getBundle("properties/principal");

    boolean accepted = false;

    public BitalinoCaptureConfigurationDialog() {
        super(null, "Bitalino Capture Configuration", Dialog.ModalityType.APPLICATION_MODAL);
    }

    public BitalinoCaptureConfigurationDialog(ProjectOrganization organization) {
        super(null, "Bitalino Capture Configuration", Dialog.ModalityType.APPLICATION_MODAL);
        org = organization;
    }

    public boolean showDialog() {

        setLayout(new GridBagLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                accepted = false;
                super.windowClosing(e);
            }
        });

        setLayout(new GridBagLayout());
        GridBConstraints gbc = new GridBConstraints();

        JLabel label = new JLabel(dialogBundle.getString("configuration_n"));
        JLabel samplerate = new JLabel(dialogBundle.getString("sr"));
        nameField = new JTextField();
        nameField.getDocument().addDocumentListener(this);
        sSR = new JSlider(0,100);
        sSR.setValue(100);
        srValue = new JLabel("100");
        
        ECG = new JCheckBox();
        EMG = new JCheckBox();
        EDA = new JCheckBox();
        JLabel sen_ecg = new JLabel("ECG");
        JLabel sen_emg = new JLabel("EMG");
        JLabel sen_eda = new JLabel("EDA");

        JLabel sensores = new JLabel(dialogBundle.getString("sens"));
        gbc.gx(0).gy(0).f(GridBConstraints.HORIZONTAL).a(GridBConstraints.FIRST_LINE_START).i(new Insets(5, 5, 5, 5));
        add(label, gbc);
        add(nameField, gbc.gx(2).wx(1).gw(6));
        add(sensores,gbc.gy(2).gx(0));
        add(sen_ecg, gbc.gy(2).gx(1).gw(1));
        add(ECG,gbc.gy(2).gx(2).gw(1));
        add(sen_emg, gbc.gy(2).gx(3).gw(1));
        add(EMG,gbc.gy(2).gx(4).gw(1));
        add(sen_eda, gbc.gy(2).gx(5).gw(1));
        add(EDA,gbc.gy(2).gx(6).gw(1));     
        add(samplerate, gbc.gy(4).gx(0));
        add(srValue,gbc.gx(2).gy(4).wx(1).gw(1));
        add(sSR,gbc.gy(6).wx(1).gw(6));
          
        sSR.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                srValue.setText(String.valueOf(sSR.getValue()));
            }
        });
        
        ECG.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {            
                updateState();
            }            
        });
        EMG.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {            
                updateState();
            }            
        });
        EDA.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {            
                updateState();
            }            
        });

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.red);
        add(errorLabel, gbc.gx(0).gy(10).gw(5).a(GridBConstraints.LAST_LINE_START).wy(1));

        accept = new JButton(dialogBundle.getString("accept"));
        
        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ECG.isSelected()||EMG.isSelected()||EDA.isSelected()){                    
                    accepted = true;
                    setVisible(false);
                    sensor_rec=0;
                    if(ECG.isSelected()){
                        sensor_rec=sensor_rec+1;
                    }if(EMG.isSelected()){
                        sensor_rec=sensor_rec+10;                    
                    }if(EDA.isSelected()){
                        sensor_rec=sensor_rec+100;                    
                    }
                    SR=sSR.getValue();
                    dispose();
                }
            }
        });

        gbc.gx(0).gy(8).a(GridBConstraints.LAST_LINE_END).gw(3).wy(1).f(GridBConstraints.NONE);
        add(accept, gbc);

        setMinimumSize(new Dimension(400, 150));
        setPreferredSize(new Dimension(400, 300));
        pack();
        Utils.centerOnScreen(this);
        updateState();
        setVisible(true);

        return accepted;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateState();
    }

    private void updateState() {
        if (nameField.getText().isEmpty()) {
            errorLabel.setText(dialogBundle.getString("name"));
            accept.setEnabled(false);
        }
        else if(!ECG.isSelected() && !EMG.isSelected() && !EDA.isSelected()){
            errorLabel.setText(dialogBundle.getString("sensor"));
            accept.setEnabled(false);            
        }
        
        else {
            errorLabel.setText("");
            accept.setEnabled(true);
        }
    }

    public String getConfigurationName() {
        return nameField.getText();
    }
}

