package net.australmc.core.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.australmc.core.annotations.command.CommandClass;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.australmc.core.AustralCore.log;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandRegistrator {

    public static void registerAllCommands(JavaPlugin plugin, Set<Class<?>> classess) {
        log().info("Registering commands of " + plugin.getName() + " plugin...");
        Set<Class<?>> commandClasses = classess.stream()
                .filter(clasz -> clasz.isAnnotationPresent(CommandClass.class))
                .collect(Collectors.toSet());
        log().info(commandClasses.size() + " commands found for " + plugin.getName());

        commandClasses.forEach(clasz -> {
            if(stream(clasz.getInterfaces()).noneMatch(CommandExecutor.class::equals)) {
                log().warning("Command class " + clasz.getName() + " is not implementing CommandExecutor, ignoring...");
                return;
            }

            CommandClass commandClassAnnotation = clasz.getAnnotation(CommandClass.class);
            String commandName = commandClassAnnotation.name();
            try {
                plugin.getCommand(commandName).setExecutor((CommandExecutor) clasz.newInstance());
                log().info("Command \"" + commandName + "\" loaded on plugin " + plugin.getName());
            } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
                log().severe(
                        "Could not load command \"" + commandName + "\" from plugin \"" + plugin.getName() + "\"!");
                e.printStackTrace();
            }
        });
    }

}
