package com.curtin.securehire.repository.es;

import com.curtin.securehire.entity.es.LocationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationSearchRepository extends ElasticsearchRepository<LocationDocument, Integer> {
    List<LocationDocument> findByNameContaining(String name);

    List<LocationDocument> findByNameStartingWith(String prefix);
}
