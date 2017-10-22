package capturepluginbitalino;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mo.capture.RecordableConfiguration;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;

public class BitalinoCaptureConfiguration implements RecordableConfiguration {
    
    private String id;
    BitalinoRecorder sr;    
    private static final Logger logger = Logger.getLogger(BitalinoRecorder.class.getName());

    BitalinoCaptureConfiguration(String id) {
        this.id = id;
    }
    
    BitalinoCaptureConfiguration(){
        
    }

    @Override
    public void setupRecording(File stageFolder, ProjectOrganization org, Participant p) {
         sr = new BitalinoRecorder(stageFolder, org, p, this);
    }

    @Override
    public void startRecording() {
            sr.StartRecord();
    }

    @Override
    public void stopRecording() {
        sr.StopRecord();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public File toFile(File parent) {
        try {
            File f = new File(parent, "bitalino_"+id+".xml");
            f.createNewFile();
            return f;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Configuration fromFile(File file) {
        String fileName = file.getName();
        if (fileName.contains("_") && fileName.contains(".")){
            String newId = fileName.substring(fileName.indexOf('_') + 1, fileName.lastIndexOf("."));
            BitalinoCaptureConfiguration c = new BitalinoCaptureConfiguration(newId);
            return c;
        }
        return null;
    }

    @Override
    public void cancelRecording() {
        sr.CancelRecord();
    }

    @Override
    public void pauseRecording() {
        sr.PauseRecord();
    }

    @Override
    public void resumeRecording() {
        sr.ResumeRecod();
    }
    
}
