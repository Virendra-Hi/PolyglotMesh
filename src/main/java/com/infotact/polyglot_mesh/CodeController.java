
package com.infotact.polyglot_mesh;

import com.infotact.polyglot_mesh.dto.ExecutionRequest;
import com.infotact.polyglot_mesh.dto.ExecutionResponse;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/polyglot")
@CrossOrigin(origins = "http://localhost:5173")
public class CodeController {

    private final PolyglotEngineService polyglotEngineService;

    public CodeController(
            PolyglotEngineService polyglotEngineService
    ) {
        this.polyglotEngineService = polyglotEngineService;
    }

    // New generic execution API
    @PostMapping("/execute")
    public Mono<ExecutionResponse> executeCode(
            @RequestBody ExecutionRequest request
    ) {
        if (request == null) {
            return Mono.just(new ExecutionResponse(
                    false, null, "", "Request body is required.", 0
            ));
        }

        ExecutionResponse response =
                polyglotEngineService.execute(
                        request.language(),
                        request.code()
                );

        return Mono.just(response);
    }

    // Existing API preserved for backward compatibility
    @PostMapping("/run")
    public Mono<String> runCode(
            @RequestBody ExecutionRequest request
    ) {
        if (request == null) {
            return Mono.just("Request body is required.");
        }

        String output = polyglotEngineService.executeSnippet(
                request.language(),
                request.code()
        );

        return Mono.just(output);
    }
}
