package com.codefactorygroup.betting.dto;

import lombok.Builder;

@Builder
public record EventShortDTO(Integer id, String name, String startTime, String shortName, String sportName) {
}
