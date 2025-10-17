package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A caching wrapper for any BreedFetcher.
 * - Caches successful results (breed -> list of sub-breeds).
 * - If the underlying fetcher throws BreedNotFoundException, we do NOT cache failures,
 *   so a later successful correction (e.g., user fixes typo) can still work.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = breed.toLowerCase();

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        List<String> result = delegate.getSubBreeds(breed);
        cache.put(key, result);
        return result;
    }
}
