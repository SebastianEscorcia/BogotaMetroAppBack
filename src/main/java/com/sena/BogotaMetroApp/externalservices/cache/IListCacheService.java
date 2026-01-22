package com.sena.BogotaMetroApp.externalservices.cache;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para servicios de cache que manejan listas
 * @param <T> Tipo de dato en la lista
 */
public interface IListCacheService <T>{
    /**
     * Guarda una lista en cache
     */
    void cacheList(String key, List<T> data, long ttlMinutes);

    /**
     * Obtiene una lista desde cache
     */
    Optional<List<T>> getList(String key);

    /**
     * Invalida una key específica
     */
    void invalidateKey(String key);

    /**
     * Invalida por patrón
     */
    void invalidateByPattern(String pattern);
}
