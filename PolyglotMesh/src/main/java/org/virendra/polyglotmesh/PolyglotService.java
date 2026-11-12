
package org.virendra.polyglotmesh;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;

@Service
public class PolyglotService {

    public String executeJavaScript(String code) {

        try (Context context = Context.create("js")) {

            Value result = context.eval("js", code);

            return result.toString();

        } catch (Exception e) {
            return "Execution Error: " + e.getMessage();
        }
    }
}
