package net.australmc.core.config.mapper;

import net.australmc.core.config.mapper.types.BooleanList;
import net.australmc.core.config.mapper.types.DoubleList;
import net.australmc.core.config.mapper.types.FloatList;
import net.australmc.core.config.mapper.types.IntegerList;
import net.australmc.core.config.mapper.types.LongList;
import net.australmc.core.config.mapper.types.ObjectList;
import net.australmc.core.config.mapper.types.StringList;
import net.australmc.core.util.BiSupplier;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

import static java.util.Arrays.stream;

public enum ConfigFieldType {
    BOOLEAN(Boolean.class, (key, section) -> Boolean.valueOf(section.getBoolean(key))),
    BOOLEAN_LIST(BooleanList.class, (key, section) -> section.getBooleanList(key)),
    DOUBLE(Double.class, (key, section) -> Double.valueOf(section.getDouble(key))),
    DOUBLE_LIST(DoubleList.class, (key, section) -> section.getDoubleList(key)),
    FLOAT_LIST(FloatList.class, (key, section) -> section.getFloatList(key)),
    INTEGER(Integer.class, (key, section) -> Integer.valueOf(section.getInt(key))),
    INTEGER_LIST(IntegerList.class, (key, section) -> section.getIntegerList(key)),
    LONG(Long.class, (key, section) -> Long.valueOf(section.getLong(key))),
    LONG_LIST(LongList.class, (key, section) -> section.getLongList(key)),
    STRING(String.class, (key, section) -> section.getString(key)),
    STRING_LIST(StringList.class, (key, section) -> section.getStringList(key)),
    SUBPOJO(Object.class, (key, section) -> section.get(key)),
    SUBPOJO_LIST(ObjectList.class, (key, section) -> section.getList(key));

    private final Class<?> type;
    private final BiSupplier<String, ConfigurationSection, ?> configSupplier;

    ConfigFieldType(Class<?> type, BiSupplier<String, ConfigurationSection, ?> configSupplier) {
        this.type = type;
        this.configSupplier = configSupplier;
    }

    public Class<?> getType() {
        return type;
    }

    public BiSupplier<String, ConfigurationSection, ?> getConfigSupplier() {
        return configSupplier;
    }

    public static Optional<ConfigFieldType> getByType(final Class<?> type) {
        return stream(ConfigFieldType.values())
                .filter(configFieldType ->
                        configFieldType.getType().isAssignableFrom(type))
                .findFirst();
    }
}
