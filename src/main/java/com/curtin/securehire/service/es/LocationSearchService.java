package com.curtin.securehire.service.es;

import com.curtin.securehire.entity.db.Location;
import com.curtin.securehire.entity.es.LocationDocument;
import com.curtin.securehire.repository.es.LocationSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocationSearchService {

    @Autowired
    private LocationSearchRepository locationSearchRepository;

    public void indexLocation(Location location) {
        LocationDocument document = convertToDocument(location);
        locationSearchRepository.save(document);
        log.info("Indexed location with ID: {}", location.getId());
    }

    public void indexLocations(List<Location> locations) {
        List<LocationDocument> documents = locations.stream()
                .map(this::convertToDocument)
                .collect(Collectors.toList());
        locationSearchRepository.saveAll(documents);
        log.info("Indexed {} locations", locations.size());
    }

    public List<LocationDocument> searchLocations(String query) {
        log.info("Searching locations with query: {}", query);
        return locationSearchRepository.findByNameContaining(query);
    }

    public List<LocationDocument> suggestLocations(String prefix) {
        log.info("Getting location suggestions with prefix: {}", prefix);
        return locationSearchRepository.findByNameStartingWith(prefix);
    }

    private LocationDocument convertToDocument(Location location) {
        LocationDocument document = new LocationDocument();
        Integer id = location.getId();
        document.setId(id.toString());
        document.setName(location.getName());
        document.setType(location.getType() != null ? location.getType().name() : null);
        document.setParentId(location.getParent().getId());
        return document;
    }
}
