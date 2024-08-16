package faang.school.urlshortenerservice.service;

import faang.school.urlshortenerservice.dto.UpdateDto;
import faang.school.urlshortenerservice.dto.UrlDto;
import faang.school.urlshortenerservice.exception.UrlNotFoundException;
import faang.school.urlshortenerservice.repository.HashRepository;
import faang.school.urlshortenerservice.repository.UrlCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {
    private final HashCache hashCache;
    private final HashRepository hashRepository;
    private final UrlCache urlCache;

    public String createShortLink(UrlDto url) {
        var hash = hashCache.getHash();
        urlCache.put(hash, url.getLongUrl());
        return hashRepository.saveUrlAndHash(url.getLongUrl(), hash);
    }

    public String getOriginUrl(String url) {
        log.info("Getting origin URL for: " + url);

        String originUrl = urlCache.getUrl(url)
                .orElseGet(
                        () -> hashRepository.getOriginalUrl(url)
                        .orElseThrow(() -> new UrlNotFoundException("404", "Original URL not found for the given short URL"))
                );
//        String originUrl = hashRepository.getOriginalUrl(url)
//                .orElseThrow(() -> new UrlNotFoundException("404", "Original URL not found for the given short URL"));
        return originUrl;
    }

    public void update(UpdateDto dto) {
        hashRepository.update(dto.getOldUrl(), dto.getNewUrl());
    }
}