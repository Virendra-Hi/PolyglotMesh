
package com.infotact.polyglot_mesh;

import com.infotact.polyglot_mesh.dto.ExecutionResponse;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;

@Service
public class PolyglotEngineService {

    private static final Set<String> SUPPORTED_LANGUAGES =
            Set.of("python", "js", "ruby");

    public ExecutionResponse execute(
            String language,
            String codeScript
    ) {
        long startTime = System.nanoTime();

        String langId = normalizeLanguage(language);

        if (langId == null) {
            return response(false, language, "",
                    "Unsupported language. Use Python, JavaScript or Ruby.",
                    startTime);
        }

        if (codeScript == null || codeScript.isBlank()) {
            return response(false, langId, "",
                    "Code cannot be empty.",
                    startTime);
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream errors = new ByteArrayOutputStream();

        try (Context context = Context.newBuilder(langId)
                .out(output)
                .err(errors)
                .allowAllAccess(false)
                .allowIO(false)
                .allowCreateProcess(false)
                .allowHostAccess(HostAccess.NONE)
                .build()) {

            context.eval(langId, codeScript);

            String result = output.toString(StandardCharsets.UTF_8);
            String errorOutput = errors.toString(StandardCharsets.UTF_8);

            return response(
                    true,
                    langId,
                    result,
                    errorOutput.isBlank() ? null : errorOutput,
                    startTime
            );

        } catch (PolyglotException e) {

            String errorMessage = e.getMessage();

            if (errorMessage == null || errorMessage.isBlank()) {
                errorMessage = "Code execution failed.";
            }

            return response(
                    false,
                    langId,
                    output.toString(StandardCharsets.UTF_8),
                    errorMessage,
                    startTime
            );

        } catch (Exception e) {

            return response(
                    false,
                    langId,
                    output.toString(StandardCharsets.UTF_8),
                    "Execution Error: " + e.getMessage(),
                    startTime
            );
        }
    }

    // Backward compatibility with the existing endpoint
    public String executeSnippet(String language, String codeScript) {
        ExecutionResponse result = execute(language, codeScript);

        if (!result.success()) {
            return "Execution Error: " + result.error();
        }

        return result.output().isBlank()
                ? "Execution completed successfully (No output)."
                : result.output();
    }

    private String normalizeLanguage(String language) {

        if (language == null || language.isBlank()) {
            return null;
        }

        String lang = language.trim().toLowerCase(Locale.ROOT);

        if (lang.equals("javascript")) {
            lang = "js";
        }

        return SUPPORTED_LANGUAGES.contains(lang) ? lang : null;
    }

    private ExecutionResponse response(
            boolean success,
            String language,
            String output,
            String error,
            long startTime
    ) {
        long executionTime =
                (System.nanoTime() - startTime) / 1_000_000;

        return new ExecutionResponse(
                success,
                language,
                output,
                error,
                executionTime
        );
    }
}
