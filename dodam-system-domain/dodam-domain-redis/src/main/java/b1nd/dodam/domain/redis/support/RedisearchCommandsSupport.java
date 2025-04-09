package b1nd.dodam.domain.redis.support;

import com.redis.lettucemod.api.sync.RediSearchCommands;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import com.redis.lettucemod.search.SearchOptions;
import com.redis.lettucemod.search.SearchResults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RedisearchCommandsSupport {

    private final RedisModulesCommands<String, String> masterCommands;
    private final List<RedisModulesCommands<String, String>> slaveCommandsList;
    private final AtomicInteger slaveIndex = new AtomicInteger(0);

    public RedisearchCommandsSupport(
            @Qualifier("masterRedisearchCommands") RedisModulesCommands<String, String> masterCommands,
            @Qualifier("slaveRedisearchCommandsList") List<RedisModulesCommands<String, String>> slaveCommandsList
    ) {
        this.masterCommands = masterCommands;
        this.slaveCommandsList = slaveCommandsList;
    }

    public SearchResults<String, String> search(
            String indexName, Optional<String> query, Optional<SearchOptions<String, String>> options
    ) {
        int idx = slaveIndex.getAndUpdate(i -> (i + 1) % slaveCommandsList.size());
        RediSearchCommands<String, String> slaveCommand = slaveCommandsList.get(idx);

        String searchQuery = query.orElse("*");
        SearchOptions<String, String> searchOptions = options.orElse(SearchOptions.<String, String>builder().build());

        return slaveCommand.ftSearch(indexName, searchQuery, searchOptions);
    }

    public void addHset(String key, Map<String, String> value) {
        masterCommands.hset(key, value);
    }

    public void deleteFromIndex(String indexName, String documentId) {
        String command = String.format("FT.DEL %s %s", indexName, documentId);
        executeCommandOnMaster(command);
    }

    private void executeCommandOnMaster(String command) {
        masterCommands.dispatch(io.lettuce.core.protocol.CommandType.valueOf(command), null);
    }
}
