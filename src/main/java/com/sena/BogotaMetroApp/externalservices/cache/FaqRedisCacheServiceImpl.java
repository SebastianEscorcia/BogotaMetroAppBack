package com.sena.BogotaMetroApp.externalservices.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.SupportFaqResponseDTO;
import com.sena.BogotaMetroApp.utils.RedisCacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Qualifier("supportFaqCache")
@Slf4j
public class FaqRedisCacheServiceImpl extends AbstractRedisCacheService implements  ISupportFaqCacheService, IListCacheService<SupportFaqResponseDTO>{

    protected FaqRedisCacheServiceImpl(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    public void cacheSupportFaqs(List<SupportFaqResponseDTO> faqs) {
        log.info("💾 Intentando cachear Support FAQs - Total: {} items", faqs.size());
        serialize(faqs).ifPresent(json -> {
            setWithTtl(RedisCacheConstants.ALL_SUPPORT_FAQS_KEY, json,
                    RedisCacheConstants.SUPPORT_FAQS_TTL_MINUTES, TimeUnit.MINUTES);
            log.info("✅ Support FAQs cacheadas exitosamente - Key: {}, TTL: {} minutos",
                    RedisCacheConstants.ALL_SUPPORT_FAQS_KEY, RedisCacheConstants.SUPPORT_FAQS_TTL_MINUTES);
        });
    }

    public Optional<List<SupportFaqResponseDTO>> getCachedSupportFaqs() {
        return getValue(RedisCacheConstants.ALL_SUPPORT_FAQS_KEY)
                .flatMap(json -> deserializeList(json, new TypeReference<List<SupportFaqResponseDTO>>() {}));
    }

    public void cacheSupportFaqsByCategory(Long categoryId, List<SupportFaqResponseDTO> faqs) {
        String key = RedisCacheConstants.supportFaqsByCategoryKey(categoryId);
        serialize(faqs).ifPresent(json -> {
            setWithTtl(key, json, RedisCacheConstants.SUPPORT_FAQS_TTL_MINUTES, TimeUnit.MINUTES);
            log.info("Support FAQs por categoría {} cacheadas: {} items", categoryId, faqs.size());
        });
    }

    public Optional<List<SupportFaqResponseDTO>> getCachedSupportFaqsByCategory(Long categoryId) {
        String key = RedisCacheConstants.supportFaqsByCategoryKey(categoryId);
        return getValue(key)
                .flatMap(json -> deserializeList(json, new TypeReference<List<SupportFaqResponseDTO>>() {}));
    }

    public void invalidateSupportFaqsCache() {
        deleteByPattern(RedisCacheConstants.SUPPORT_FAQS_PATTERN);
        log.info("Cache de support FAQs invalidado");
    }

    public void invalidateAllFaqsCache() {
        deleteByPattern(RedisCacheConstants.ALL_FAQS_PATTERN);
        log.info("Todo el cache de FAQs invalidado");
    }

    // ============================================================
    // =========== IMPLEMENTACIÓN DE ListCacheService =============
    // ============================================================


    @Override
    public void cacheList(String key, List data, long ttlMinutes) {
        serialize(data).ifPresent(json ->
                setWithTtl(key, json, ttlMinutes, TimeUnit.MINUTES));
    }

    @Override
    public Optional<List<SupportFaqResponseDTO>> getList(String key) {
        return getValue(key)
                .flatMap(json -> deserializeList(json, new TypeReference<List<SupportFaqResponseDTO>>() {}));
    }

    @Override
    public void invalidateKey(String key) {
        deleteKey(key);
    }

    @Override
    public void invalidateByPattern(String pattern) {
        deleteByPattern(pattern);
    }
}
