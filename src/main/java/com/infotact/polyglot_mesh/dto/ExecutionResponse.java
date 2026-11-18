
package com.infotact.polyglot_mesh.dto;

public record ExecutionResponse(
        boolean success,
        String language,
        String output,
        String error,
        long executionTimeMs
) {
}
