package com.curtin.securehire.dto;


import com.curtin.securehire.constant.LocationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Data Transfer Object for Location")
public class LocationDTO {

    @Schema(description = "ID of the location")
    private Integer id;

    @Schema(description = "Name of the location")
    private String name;

    @Schema(description = "Type of the location")
    private LocationType type;

    @Schema(description = "Parent location (non-recursive)")
    private ParentLocationDTO parent;

    @Schema(description = "List of child locations")
    private List<ChildLocationDTO> children;

    @Data
    @Schema(description = "Simplified parent location DTO")
    public static class ParentLocationDTO {
        private Integer id;
        private String name;
    }

    @Data
    @Schema(description = "Simplified child location DTO")
    public static class ChildLocationDTO {
        private Integer id;
        private String name;
    }
}
