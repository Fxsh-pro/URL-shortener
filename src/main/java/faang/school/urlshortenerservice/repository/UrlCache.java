package faang.school.urlshortenerservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UrlCache {
    private final RedisTemplate<String, String> redisUrlTemplate;

    public void put(String hash, String url) {
        System.out.println(hash + " " + url);
        redisUrlTemplate.opsForValue().set(hash, url);
    }

    public Optional<String> getUrl(String hash) {
        var val = redisUrlTemplate.opsForValue().get(hash);
        System.out.println("HERE----" + val);
        return Optional.ofNullable(redisUrlTemplate.opsForValue().get(hash));
    }
}
