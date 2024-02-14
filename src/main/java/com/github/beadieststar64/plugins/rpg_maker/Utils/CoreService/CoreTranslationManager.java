package com.github.beadieststar64.plugins.rpg_maker.Utils.CoreService;

import com.github.beadieststar64.plugins.bsseries.bscore.API.Translator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

public class CoreTranslationManager implements Translator {

    private final Plugin plugin;
    private String language;
    private String folder;

    /**
     *
     * @param plugin -
     * @param language -
     * @param path -
     */
    public CoreTranslationManager(Plugin plugin, String language, String path) {
        this.plugin = plugin;
        CoreYamlLoader config = new CoreYamlLoader(plugin);
        this.language = config.getString(language);
        this.folder = config.getString(path);
    }

    @Override
    public String getCustomerPlugin() {
        return plugin.getName();
    }

    @Override
    public String getTranslator(String s) {
        Properties prop = new Properties();
        File file = new File(plugin.getDataFolder() + File.separator + folder, language + ".properties");
        try(InputStream stream = Files.newInputStream(file.toPath());
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            prop.load(reader);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.getProperty(s);
    }

    @Override
    public String getTranslator(String s, String[] strings, Object[] objects) {
        return null;
    }
}
