package ru.sharepos;

import org.bukkit.plugin.java.JavaPlugin;

public class SharePosPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("SharePos plugin enabled!");
        
        SharePosCommand commandExecutor = new SharePosCommand();
        this.getCommand("sharepos").setExecutor(commandExecutor);
        this.getCommand("sharepos").setTabCompleter(commandExecutor);
        
        getServer().getPluginManager().registerEvents(new ChatClickListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("SharePos plugin disabled!");
    }
}