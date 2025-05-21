package com.Batman.restcontroller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.Batman.constants.GameConstants;
import com.batman.controller.IDevHelperController;
import com.batman.elastic.ElasticSearchClientConfiguration;
import com.batman.helpers.DevelopmentTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/rest/dev")
public class GameServiceDevController implements IDevHelperController {

    private final ElasticsearchClient elasticsearchClient;

    public GameServiceDevController(ElasticSearchClientConfiguration elasticSearchClientConfiguration) {
        Assert.notNull(elasticSearchClientConfiguration, "Elastic Search Client Configuration is Empty");
        this.elasticsearchClient = elasticSearchClientConfiguration.getElasticsearchClient();
    }

    @PostMapping("/game/index")
    @Override
    public ResponseEntity<?> createIndex() {
        try {
            boolean isIndexed = DevelopmentTools.createIndex(this.elasticsearchClient, GameConstants.GAME_ES_INDEX_MAPPING_FILE_NAME, GameConstants.GAME_INDEX_NAME);
            return isIndexed ? new ResponseEntity<>("SuccessFully Indexed", HttpStatus.CREATED) : new ResponseEntity<>("Indexing Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

