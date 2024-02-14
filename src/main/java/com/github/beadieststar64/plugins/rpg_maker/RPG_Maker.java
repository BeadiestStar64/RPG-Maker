package com.github.beadieststar64.plugins.rpg_maker;

import com.github.beadieststar64.plugins.bsseries.bscore.API.FileManager;
import com.github.beadieststar64.plugins.rpg_maker.Utils.CoreService.CoreFileManager;
import com.github.beadieststar64.plugins.rpg_maker.Utils.Commands;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPG_Maker extends JavaPlugin {

    @Override
    public void onEnable() {
        //File initialize
        CoreFileManager manager = new CoreFileManager(this);
        manager.managerInitialize(getDataFolder(), "RequestFiles.txt");

        getServer().getServicesManager().register(FileManager.class, manager, this, ServicePriority.Normal);

        final PluginCommand command = getCommand("rpg-info");
        if(command != null) {
            command.setExecutor(new Commands());
            command.setTabCompleter(new Commands());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
