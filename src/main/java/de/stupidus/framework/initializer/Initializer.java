package de.stupidus.framework.initializer;

import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.Initialize;
import de.stupidus.command.command.Command;
import de.stupidus.framework.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Initializer {
    private List<String> packageList = new ArrayList<>();
    private HashMap<Command, Boolean> containsExecute = new HashMap<>();

    private static JavaPlugin plugin1 = null;

    public void register(JavaPlugin plugin) {
        plugin1 = plugin;
        updateClass(null);
        Bukkit.getConsoleSender().sendMessage("§c"+packageList);
        if (plugin != null) {
            for (Command command : CommandFramework.getCommands()) {

                CommandMap map = getCommandMap();
                map.register(command.getName(), command);
                setTabCompleter(command);
                Bukkit.getPluginManager().registerEvents(command, plugin);

                containsExecute.putIfAbsent(command, checkIfMethodIsOverridden(CMDFWCommand.class, command.getClass(), "execute"));
            }
        }
    }

    public void updateClass(String className) {
        for (String packageName : packageList) {
            List<Class<?>> classes;
            try {
                classes = getClassesFromJar(packageName);
                for (Class<?> clazz : classes) {
                    if (clazz.isAnnotationPresent(Initialize.class)) {
                        if (className == null || clazz.getName().equalsIgnoreCase(className)) {
                            Object instance = clazz.getDeclaredConstructor().newInstance();
                            for (Method method : clazz.getMethods()) {
                                if (method.getName().equalsIgnoreCase("initialize")) {
                                    invokeMethod(method, instance);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTabCompleter(String nameCommand) {
        Command command = CommandFramework.getCommand(nameCommand);
        if (command != null) {
            command.getTabCompleter().updateSubCommands(command.getSubCommands());
        } else {
            Bukkit.getConsoleSender().sendMessage("§cupdateTabCompleter: §a" + nameCommand);
            Bukkit.getConsoleSender().sendMessage("§cCommand is null!");
        }
    }
    private List<Class<?>> getClassesFromJar(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = getClass().getClassLoader().getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if (protocol.equals("jar")) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));

                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                        String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
                jar.close();
            } else if (protocol.equals("file")) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    for (String file : directory.list()) {
                        if (file.endsWith(".class")) {
                            String className = packageName + '.' + file.substring(0, file.length() - 6);
                            classes.add(Class.forName(className));
                        }
                    }
                }
            }
        }
        return classes;
    }

    public void addPackage(String packageName) {
        if (!packageList.contains(packageName)) {
            packageList.add(packageName);
        }
    }

    private void invokeMethod(Method method, Object instance, Object... args) {
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfMethodIsOverridden(Class<?> baseClass, Class<?> subClass, String methodName) {
        try {
            Method baseMethod = baseClass.getDeclaredMethod(methodName, CommandSender.class, Player.class, org.bukkit.command.Command.class, String.class, String[].class);
            Method subMethod = subClass.getDeclaredMethod(methodName, CommandSender.class, Player.class, org.bukkit.command.Command.class, String.class, String[].class);

            return !baseMethod.equals(subMethod);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
    public boolean containsExecuteMethod(Command command) {
        return containsExecute.get(command);
    }

    public static JavaPlugin getJavaPlugin() {
        return plugin1;
    }
    private CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            throw new RuntimeException("Konnte CommandMap nicht abrufen", e);
        }
    }

    private void setTabCompleter(org.bukkit.command.Command command) {
        try {
            Field completerField = org.bukkit.command.Command.class.getDeclaredField("completer");
            completerField.setAccessible(true);
            if (command instanceof org.bukkit.command.Command) {
                completerField.set(command, command);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
