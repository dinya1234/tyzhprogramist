package ru.shop.tyzhprogramist.tyzhprogramist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.InputStream;

@SpringBootApplication
public class TyzhprogramistApplication {
//проверка для гита
    private static Process reactProcess;

    public static void main(String[] args) {
        SpringApplication.run(TyzhprogramistApplication.class, args);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (reactProcess != null && reactProcess.isAlive()) {
                System.out.println("\nShutting down React dev server...");

                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    try {
                        new ProcessBuilder("cmd.exe", "/c", "taskkill", "/F", "/PID",
                                String.valueOf(reactProcess.pid())).start();
                    } catch (Exception e) {
                        reactProcess.destroy();
                    }
                } else {
                    reactProcess.destroy();
                    try {
                        Thread.sleep(1000);
                        if (reactProcess.isAlive()) {
                            reactProcess.destroyForcibly();
                        }
                    } catch (InterruptedException e) {
                        reactProcess.destroyForcibly();
                    }
                }

                System.out.println("React dev server stopped");
            }
        }));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startReactDevServer() {
        String os = System.getProperty("os.name").toLowerCase();
        String projectPath = System.getProperty("user.dir");
        String staticPath = projectPath + "/src/main/resources/static";

        File packageJson = new File(staticPath + "/package.json");
        if (!packageJson.exists()) {
            System.out.println("React project not found in: " + staticPath);
            System.out.println("Make sure package.json exists in src/main/resources/static/");
            return;
        }

        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder();

                if (os.contains("win")) {
                    processBuilder.command("cmd.exe", "/c", "npm run dev");
                } else {
                    processBuilder.command("bash", "-c", "npm run dev");
                }

                processBuilder.directory(new File(staticPath));
                processBuilder.redirectErrorStream(true);

                reactProcess = processBuilder.start();

                System.out.println("React dev server starting on http://localhost:3000");
                System.out.println("React directory: " + staticPath);

                try (InputStream inputStream = reactProcess.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        System.out.print(new String(buffer, 0, length));
                    }
                }

                int exitCode = reactProcess.waitFor();
                System.out.println("React dev server exited with code: " + exitCode);

            } catch (Exception e) {
                System.err.println("Failed to start React dev server: " + e.getMessage());
            }
        }).start();
    }
}