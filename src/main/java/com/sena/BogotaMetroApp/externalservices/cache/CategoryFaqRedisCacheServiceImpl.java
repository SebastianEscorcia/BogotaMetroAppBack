package com.sena.BogotaMetroApp.externalservices.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sena.BogotaMetroApp.presentation.dto.supportfaq.CategoryFaqResponseDTO;
import com.sena.BogotaMetroApp.utils.RedisCacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Qualifier("categoryFaqCache")
@Slf4j
public class CategoryFaqRedisCacheServiceImpl extends  AbstractRedisCacheService implements ICategoryFaqCacheService,IListCacheService<CategoryFaqResponseDTO>{
    protected CategoryFaqRedisCacheServiceImpl(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }
    public void cacheCategoryFaqs(List<CategoryFaqResponseDTO> categories) {
        log.info("💾 Intentando cachear Category FAQs - Total: {} items", categories.size());
        serialize(categories).ifPresent(json -> {
            setWithTtl(RedisCacheConstants.ALL_CATEGORY_FAQS_KEY, json,
                    RedisCacheConstants.CATEGORY_FAQS_TTL_MINUTES, TimeUnit.MINUTES);
            log.info("✅ Category FAQs cacheadas exitosamente - Key: {}, TTL: {} minutos",
                    RedisCacheConstants.ALL_CATEGORY_FAQS_KEY, RedisCacheConstants.CATEGORY_FAQS_TTL_MINUTES);
        });
    }

    public Optional<List<CategoryFaqResponseDTO>> getCachedCategoryFaqs() {
        return getValue(RedisCacheConstants.ALL_CATEGORY_FAQS_KEY)
                .flatMap(json -> deserializeList(json, new TypeReference<List<CategoryFaqResponseDTO>>() {}));
    }

    public void invalidateCategoryFaqsCache() {
        deleteKey(RedisCacheConstants.ALL_CATEGORY_FAQS_KEY);
        log.info("Cache de category FAQs invalidado");
    }

    @Override
    public void cacheList(String key, List data, long ttlMinutes) {
        serialize(data).ifPresent(json ->
                setWithTtl(key, json, ttlMinutes, TimeUnit.MINUTES));
    }

    @Override
    public Optional<List<CategoryFaqResponseDTO>> getList(String key) {
        return getValue(key)
                .flatMap(json -> deserializeList(json, new TypeReference<List<CategoryFaqResponseDTO>>() {}));
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
