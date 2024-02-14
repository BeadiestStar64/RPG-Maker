package com.github.beadieststar64.plugins.rpg_maker.Utils.CoreService;

import com.github.beadieststar64.plugins.bsseries.bscore.API.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CoreFileManager implements FileManager {

    private final Plugin plugin;

    public CoreFileManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCustomerPlugin() {
        return plugin.getName();
    }

    /**
     *
     * @param folder -
     * @param s -
     */
    @Override
    public void managerInitialize(File folder, String s) {
        if(new File(folder, s).exists()) {
            return;
        }
        createFile(folder, s);
        try(InputStream stream = Files.newInputStream(new File(folder, s).toPath());
            InputStreamReader input = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(input)) {
            String line;
            while((line = reader.readLine()) != null) {
                if(line.charAt(0) == '#') {
                    //This is comment
                    continue;
                }
                String[] split = line.split(", ");
                if(split.length < 1 || split.length > 2) {
                    plugin.getLogger().warning(String.format("[%s] %s", plugin.getName(), "An error occurred while processing the request folder.\nPlease check the contents of the following folder: <FILENAME>".replace("<FILENAME>", s)));
                    return;
                }
                if(split.length == 1) {
                    createFile(plugin.getDataFolder(), split[0]);
                    continue;
                }
                createFile(new File(plugin.getDataFolder(), split[0]), split[1]);
            }
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param folder -
     * @param s -
     */
    @Override
    public void createFile(File folder, String s) {
        String filePath = folder + File.separator + s;
        File file = new File(s);
        if(!folder.exists()) {
            if(folder.mkdir()) {
                plugin.getLogger().info("Folder created: " + folder);
            }else{
                plugin.getLogger().warning(ChatColor.RED + "Folder creation failed: " + folder);
            }
        }
        if(!file.exists()) {
            String resource = File.separator + s;
            copyResource(resource, filePath);
        }
    }

    private void copyResource(String resourcePath, String targetPath) {
        try {
            File temp = copyTemp(resourcePath);
            copyFile(temp, new File(targetPath));
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File copyTemp(String resourcePath) throws IOException {
        try(InputStream input = plugin.getClass().getResourceAsStream(resourcePath)) {
            File temp = File.createTempFile("temp", null);
            temp.deleteOnExit();

            try(FileOutputStream output = new FileOutputStream(temp)) {
                byte[] buffer = new byte[1024];
                int byteRead;
                while((byteRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, byteRead);
                }
            }
            input.close();
            return temp;
        }
    }

    private void copyFile(File sourceFile, File targetFile) {
        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
