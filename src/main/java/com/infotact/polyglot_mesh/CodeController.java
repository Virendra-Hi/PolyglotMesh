package com.infotact.polyglot_mesh;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/polyglot")
@CrossOrigin(origins = "http://localhost:5173")
public class CodeController {

    private final PolyglotEngineService polyglotEngineService;

    public CodeController(PolyglotEngineService polyglotEngineService) {
        this.polyglotEngineService = polyglotEngineService;
    }

    @PostMapping("/run")
    public Mono<String> runCode(@RequestBody Map<String, String> payload) {
        String language = payload.get("language"); // e.g., "python", "js", "ruby"
        String code = payload.get("code");

        String output = polyglotEngineService.executeSnippet(language, code);
        return Mono.just(output);
    }
}