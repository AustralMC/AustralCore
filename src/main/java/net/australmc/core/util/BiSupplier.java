package net.australmc.core.util;

public interface BiSupplier<T, U, R> {
    R get(T param, U secondParam);
}
