package net.australmc.core;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AustralCore extends JavaPlugin {

    private static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();

        logger.info("Enabled AustralCore!");
    }

    public static Logger log() {
        return logger;
    }

}
