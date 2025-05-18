package com.curtin.securehire.repository.db;

import com.curtin.securehire.constant.LocationType;
import com.curtin.securehire.entity.db.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByName(String name);
    List<Location> findByType(LocationType type);
    @Query("SELECT l FROM Location l WHERE l.parent.id = :parentId")
    List<Location> findByParent(@Param("parentId") Integer parentId);
    List<Location> findByNameContaining(String name);
    boolean existsByName(String name);
}
