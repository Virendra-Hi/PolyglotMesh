package com.infotact.polyglot_mesh;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class PolyglotEngineService {

    public String executeSnippet(String language, String codeScript) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream errors = new ByteArrayOutputStream();

        String langId = (language == null || language.isEmpty()) ? "python" : language.toLowerCase();
        if (langId.equals("javascript")) {
            langId = "js";
        }

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
            String errStr = errors.toString(StandardCharsets.UTF_8);

            if (!errStr.isEmpty()) {
                return "Runtime Error / Stacktrace:\n" + errStr;
            }

            return result.isEmpty()
                    ? "Execution completed successfully (No output)."
                    : result;

        } catch (Exception e) {
            if (langId.equals("ruby")) {
                return "Ruby Execution Notice: TruffleRuby component requires GraalVM language installation. " +
                        "Error details: " + e.getMessage();
            }
            return "Execution Error (" + langId + "): " + e.getMessage();
        }
    }
}