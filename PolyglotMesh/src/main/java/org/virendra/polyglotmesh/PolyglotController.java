
package org.virendra.polyglotmesh;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/polyglot")
public class PolyglotController {

    private final PolyglotService polyglotService;

    public PolyglotController(PolyglotService polyglotService) {
        this.polyglotService = polyglotService;
    }

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> execute(
            @RequestBody Map<String, String> request) {

        String code = request.get("code");

        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Code is required"));
        }

        String result = polyglotService.executeJavaScript(code);

        return ResponseEntity.ok(
                Map.of("result", result)
        );
    }
}
