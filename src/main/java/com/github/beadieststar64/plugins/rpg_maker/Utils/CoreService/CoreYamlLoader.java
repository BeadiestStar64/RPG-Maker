package com.github.beadieststar64.plugins.rpg_maker.Utils.CoreService;

import com.github.beadieststar64.plugins.bsseries.bscore.API.YamlReader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class CoreYamlLoader implements YamlReader {

    private FileConfiguration yaml = null;
    private File yamlFile;
    private final String file;
    private final Plugin plugin;

    public CoreYamlLoader(Plugin plugin) {
        this(plugin, "");
    }

    public CoreYamlLoader(Plugin plugin, String folderName) {
        this(plugin, folderName, "config.yml");
    }

    public CoreYamlLoader(Plugin plugin, String folderName, String fileName) {
        this.plugin = plugin;
        this.file = fileName;
        if(folderName.isEmpty()) {
            yamlFile = new File(plugin.getDataFolder(), file);
        }else{
            yamlFile = new File(plugin.getDataFolder() + File.separator + folderName, fileName);
            saveDefault(new File(plugin.getDataFolder(), folderName), fileName);
        }
    }

    @Override
    public String getCustomerPlugin() {
        return plugin.getName();
    }

    @Override
    public void saveDefault(String fileName) {
        if(!yamlFile.exists()) {
            CoreFileManager fm = new CoreFileManager(plugin);
            fm.createFile(plugin.getDataFolder(), fileName);
        }
    }

    @Override
    public void saveDefault(File folder, String fileName) {
        if(!yamlFile.exists()) {
            CoreFileManager fm = new CoreFileManager(plugin);
            fm.createFile(folder, fileName);
        }
    }

    @Override
    public void reloadYaml() {
        yaml = YamlConfiguration.loadConfiguration(yamlFile);
        final InputStream defYamlStream = plugin.getResource(file);
        if(defYamlStream == null) {
            return;
        }
        yaml.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defYamlStream, StandardCharsets.UTF_8)));
    }

    @Override
    public FileConfiguration getYaml() {
        if(yaml == null) {
            reloadYaml();
        }
        return yaml;
    }

    @Override
    public void saveConfig() {
        if (yaml == null) {
            return;
        }
        try {
            getYaml().save(yamlFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + yamlFile, ex);
        }
        reloadYaml();
    }

    @Override
    public void setAutoReloadPath(String s) {

    }

    @Override
    public boolean checkAutoReload() {
        return false;
    }

    @Override
    public void updateAutoReload(boolean b) {

    }

    @Override
    public String getString(String key) {
        return getYaml().getString(key);
    }

    @Override
    public void setString(String key, String var) {
        if(yaml == null) {
            getYaml();
        }
        yaml.set(key, var);
        saveConfig();
    }

    @Override
    public int getInt(String key) {
        return getYaml().getInt(key);
    }

    @Override
    public void setInt(String s, int i) {
        if(yaml == null) {
            getYaml();
        }
        yaml.set(s, i);
        saveConfig();
    }

    @Override
    public boolean getBoolean(String key) {
        return getYaml().getBoolean(key);
    }

    @Override
    public void setBoolean(String key, boolean var) {
        if(yaml == null) {
            getYaml();
        }
        yaml.set(key, var);
        saveConfig();
    }
}
