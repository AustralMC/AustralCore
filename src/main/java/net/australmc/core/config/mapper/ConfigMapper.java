package net.australmc.core.config.mapper;

import net.australmc.core.annotations.config.mapper.ConfigField;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

import static net.australmc.core.AustralCore.log;
import static net.australmc.core.config.mapper.ConfigFieldType.SUBPOJO;
import static net.australmc.core.config.mapper.ConfigFieldType.getByType;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

public class ConfigMapper {

    private ConfigMapper() {}

    @Nullable
    public static <R> R mapToObject(final ConfigurationSection section, final Class<R> type) {
        try {
            final R instance = type.getDeclaredConstructor().newInstance();

            for(final Field field : getAllFields(type, withAnnotation(ConfigField.class))) {
                final String configKey = field.getAnnotation(ConfigField.class).value();
                final ConfigFieldType configFieldType = getByType(field.getType());

                final Object fieldValueFromConfig;

                if(SUBPOJO.equals(configFieldType)) {
                    fieldValueFromConfig = mapToObject(section.getConfigurationSection(configKey), field.getType());
                } else {
                    fieldValueFromConfig = configFieldType.getConfigSupplier().get(configKey, section);
                }

                field.setAccessible(true);
                field.set(instance, field.getType().cast(fieldValueFromConfig));
            }

            return instance;
        } catch (Exception exception) {
            log().severe("Could not map config section to \"" + type.toString() + "\" type. Caused by:"
                    + exception.getCause());
            exception.printStackTrace();
            return null;
        }
    }

}
