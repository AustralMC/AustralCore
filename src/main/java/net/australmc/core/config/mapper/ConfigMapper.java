package net.australmc.core.config.mapper;

import net.australmc.core.annotations.config.mapper.ConfigField;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

import static java.util.Arrays.stream;
import static net.australmc.core.AustralCore.log;
import static net.australmc.core.config.mapper.ConfigFieldType.SUBPOJO;
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
                final Class<?> fieldType = field.getType();

                final Object fieldValueFromConfig;

                if(isSubpojo(fieldType)) {
                    fieldValueFromConfig = mapToObject(section.getConfigurationSection(configKey), fieldType);
                } else {
                    fieldValueFromConfig = ConfigFieldType.getByType(fieldType)
                            .map(ConfigFieldType::getConfigSupplier)
                            .map(supplier -> supplier.get(configKey, section))
                            .orElse(null);
                }

                field.setAccessible(true);
                field.set(instance, fieldType.cast(fieldValueFromConfig));
            }

            return instance;
        } catch (Exception exception) {
            log().severe("Could not map config section to \"" + type.toString() + "\" type. Caused by:"
                    + exception.getCause());
            exception.printStackTrace();
            return null;
        }
    }

    public static <T> void mapToSection(final ConfigurationSection section, final T source) {
        try {
            for(final Field field : getAllFields(source.getClass(), withAnnotation(ConfigField.class))) {
                field.setAccessible(true);

                final Class<?> fieldType = field.getType();
                final String key = field.getAnnotation(ConfigField.class).value();
                final Object value = field.get(source);

                if(isSubpojo(fieldType)) {
                    mapToSection(section.createSection(key), value);
                    continue;
                }

                section.set(key, value);
            }
        } catch (final Exception exception) {
            log().severe("Could not map object to config section.");
            exception.printStackTrace();
        }
    }

    private static boolean isSubpojo(final Class<?> type) {
        return stream(ConfigFieldType.values())
                .filter(configFieldType -> !SUBPOJO.equals(configFieldType))
                .noneMatch(configFieldType -> configFieldType.getType().equals(type));
    }
}
