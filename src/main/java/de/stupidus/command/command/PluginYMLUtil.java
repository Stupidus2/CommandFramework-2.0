package de.stupidus.command.command;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PluginYMLUtil {

    private static final Path PLUGIN_YML = Paths.get("src/main/resources/plugin.yml");

    public static void newCommand(String name, String description, String usage) {
        try {
            List<String> lines = new ArrayList<>();

            if (java.nio.file.Files.notExists(PLUGIN_YML)) {
                java.nio.file.Files.createDirectories(PLUGIN_YML.getParent());
                lines.add("name: MyPlugin");
                lines.add("main: com.example.Main");
                lines.add("version: 1.0");
                lines.add("api-version: 1.20");
                lines.add("commands:");
            } else {
                lines = java.nio.file.Files.readAllLines(PLUGIN_YML);
            }

            int commandsIndex = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equals("commands:")) {
                    commandsIndex = i;
                    break;
                }
            }

            if (commandsIndex == -1) {
                lines.add("commands:");
                commandsIndex = lines.size() - 1;
            }

            boolean exists = false;
            for (int i = commandsIndex + 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.startsWith(name + ":")) {
                    exists = true;
                    break;
                }
                if (!line.startsWith("  ")) break;
            }

            if (!exists) {
                lines.add(commandsIndex + 1, "  " + name + ":");
                if (!description.isBlank())
                    lines.add(commandsIndex + 2, "    description: " + description);
                if (!usage.isBlank())
                    lines.add(commandsIndex + 3, "    usage: " + usage);
            }

            java.nio.file.Files.write(PLUGIN_YML, lines);
            System.out.println("Command '" + name + "' added to plugin.yml");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
