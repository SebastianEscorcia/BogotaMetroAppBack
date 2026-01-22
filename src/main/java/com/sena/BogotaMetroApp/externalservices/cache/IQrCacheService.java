package com.sena.BogotaMetroApp.externalservices.cache;

import java.util.Optional;

/**
 * Interfaz base para servicios de cache.
 * @param <T> Tipo de dato a cachear
 * @param <ID> Tipo de identificador
 */
public interface IQrCacheService<T,ID>{
    /**
     * Guarda un elemento en cache
     */
    void cache(T data);

    /**
     * Obtiene un elemento por su identificador
     */
    Optional<T> get(ID id);

    /**
     * Invalida el cache de un elemento específico
     */
    void invalidate(ID id);

    /**
     * Invalida todo el cache de este tipo
     */
    void invalidateAll();

    /**
     * Verifica si existe en cache
     */
    boolean exists(ID id);
    void invalidateFull(T data);
}
