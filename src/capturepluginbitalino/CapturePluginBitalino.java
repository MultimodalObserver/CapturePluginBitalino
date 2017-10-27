/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capturepluginbitalino;

import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mo.capture.CaptureProvider;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StagePlugin;

@Extension(
        xtends = {
            @Extends(extensionPointId = "mo.capture.CaptureProvider")
        }
)

public class CapturePluginBitalino implements CaptureProvider {

     List<Configuration> configurations;   
    
    private static final Logger logger = Logger.getLogger(CapturePluginBitalino.class.getName());
    private BitalinoCaptureConfigurationDialog dialog;

    public CapturePluginBitalino() {
        configurations = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "Bitalino";
    }

    @Override
    public Configuration initNewConfiguration(ProjectOrganization organization) {

         dialog = new BitalinoCaptureConfigurationDialog(organization);

        boolean accepted = dialog.showDialog();

        if (accepted) {
            BitalinoCaptureConfiguration configuration = new BitalinoCaptureConfiguration(dialog.getConfigurationName(),dialog.sensor_rec);

            configurations.add(configuration);
            return configuration;
        }

        return null;
    }

    @Override
    public List<Configuration> getConfigurations() {
        return configurations;
    }

    @Override
    public StagePlugin fromFile(File file) {
        if (file.isFile()) {
            try {
                CapturePluginBitalino mc = new CapturePluginBitalino();
                XElement root = XIO.readUTF(new FileInputStream(file));
                XElement[] pathsX = root.getElements("path");
                for (XElement pathX : pathsX) {
                    String path = pathX.getString();
                    BitalinoCaptureConfiguration c = new BitalinoCaptureConfiguration();
                    Configuration config = c.fromFile(new File(file.getParentFile(), path));
                    if (config != null) {
                        mc.configurations.add(config);
                    }
                }
                return mc;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public File toFile(File parent) {
         File file = new File(parent, "bitalino-capture.xml");
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        XElement root = new XElement("capturers");
        for (Configuration config : configurations) {
            File p = new File(parent, "bitalino-capture");
            p.mkdirs();
            File f = config.toFile(p);

            XElement path = new XElement("path");
            Path parentPath = parent.toPath();
            Path configPath = f.toPath();
            path.setString(parentPath.relativize(configPath).toString());
            root.addElement(path);
        }
        try {
            XIO.writeUTF(root, new FileOutputStream(file));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return file;
    }
}

