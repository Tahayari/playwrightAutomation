package ro.carrefour.ucare.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthStateManager {

    private static final Object AUTH_LOCK = new Object();

    private final String authStatePath;

    public AuthStateManager(String authStatePath) {
        this.authStatePath = authStatePath;
    }

    /**
     * Guarantees the auth-state file is valid before any test context is created. Exactly one login
     * fires per suite run — all other threads wait, then skip.
     *
     * @param loginAction The login + serialise routine supplied by the caller.
     */
    public void ensureAuthState(Runnable loginAction) {
        if (hasValidAuthState()) return; // Fast path — no locking overhead

        synchronized (AUTH_LOCK) {
            if (hasValidAuthState()) return; // Another thread logged in while we waited
            loginAction.run();
        }
    }

    /**
     * Returns {@code true} only when the file exists and holds a real session — not missing, empty,
     * or the reset sentinel "{}".
     */
    public boolean hasValidAuthState() {
        Path path = Paths.get(authStatePath);
        if (!Files.exists(path)) return false;
        try {
            String content = new String(Files.readAllBytes(path)).trim();
            return !content.isEmpty() && !content.equals("{}");
        } catch (IOException e) {
            System.err.println("Warning: could not read auth state file — " + e.getMessage());
            return false;
        }
    }

    /**
     * Resets the file to an empty JSON object so the next suite run always performs a fresh login —
     * prevents stale or expired sessions being reused.
     */
    public void clearAuthStateFile() {
        try {
            Files.write(Paths.get(authStatePath), "{}".getBytes());
        } catch (IOException e) {
            System.err.println("Warning: failed to clear auth state file — " + e.getMessage());
        }
    }
}
