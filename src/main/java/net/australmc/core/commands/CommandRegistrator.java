package net.australmc.core.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.australmc.core.annotations.command.CommandClass;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.australmc.core.AustralCore.log;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandRegistrator {

    public static void registerAllClasses(JavaPlugin plugin, Set<Class<?>> classess) {
        Set<Class<?>> commandClasses = classess.stream()
                .filter(clasz -> stream(clasz.getAnnotations()).map(Annotation::annotationType)
                        .anyMatch(CommandClass.class::equals))
                .collect(Collectors.toSet());

        commandClasses.forEach(clasz -> {
            if(stream(clasz.getInterfaces()).noneMatch(CommandExecutor.class::equals)) {
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
