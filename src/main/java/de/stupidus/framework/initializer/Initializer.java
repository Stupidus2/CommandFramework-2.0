package de.stupidus.framework.initializer;

import de.stupidus.api.CMDFWCommand;
import de.stupidus.api.CommandListener;
import de.stupidus.api.Initialize;
import de.stupidus.api.InitializeMethod;
import de.stupidus.command.command.BaseCommand;
import de.stupidus.framework.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Initializer {
    private List<String> packageList = new ArrayList<>();
    private HashMap<BaseCommand, Boolean> containsExecute = new HashMap<>();

    private static JavaPlugin plugin1 = null;

    public void register(JavaPlugin plugin) {

       // GeneratePluginYML.generate();

        plugin1 = plugin;
        updateInitializeMethods();
        listenerAnnotation();
        executeMethod(null, "initialize", Initialize.class);
        if (plugin != null) {

            List<BaseCommand> copy = new ArrayList<>(CommandFramework.getCommands());

            for (BaseCommand command : copy) {

                if (!command.getNormalRegistration()) {
                    CommandMap map = getCommandMap();
                    if (map.getCommand(command.getName()) == null) {
                        map.register(command.getName(), command);
                    }
                } else {

                    plugin1.getCommand(command.getName()).setExecutor(command);
                    plugin1.getCommand(command.getName()).setTabCompleter(command);

                }

                Bukkit.getPluginManager().registerEvents(command, plugin);

                containsExecute.putIfAbsent(command, checkIfMethodIsOverridden(CMDFWCommand.class, command.getClass(), "onCommandCustom"));
            }
        }
    }

    @Deprecated()
    public void executeMethod(String className, String methodName, Class<? extends Annotation> annotation) {
        for (String packageName : packageList) {
            List<Class<?>> classes;
            try {
                classes = getClassesFromJar(packageName);
                for (Class<?> clazz : classes) {
                    if (clazz.isAnnotationPresent(annotation)) {
                        if (className == null || clazz.getName().equalsIgnoreCase(className)) {

                            if (clazz.getName().equals("de.stupidus.command.command.CommandBuilder")) continue;

                            Object instance = clazz.getDeclaredConstructor().newInstance();
                            for (Method method : clazz.getMethods()) {
                                if (method.getName().equalsIgnoreCase(methodName)) {
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
        BaseCommand command = CommandFramework.getCommand(nameCommand);
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

    public boolean containsExecuteMethod(BaseCommand command) {
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

    public static PluginCommand createPluginCommand(String name, JavaPlugin plugin) {
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            return c.newInstance(name, plugin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerCommand(PluginCommand command) {
        try {
            Field f = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap map = (CommandMap) f.get(Bukkit.getPluginManager());
            map.register(command.getName(), command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInitializeMethods() {
        for (String packageName : packageList) {
            List<Class<?>> classes;
            try {
                classes = getClassesFromJar(packageName);
                for (Class<?> clazz : classes) {
                    for (Method method : clazz.getMethods()) {
                        if (method.isAnnotationPresent(InitializeMethod.class)) {

                            Object instance = clazz.getDeclaredConstructor().newInstance();

                            invokeMethod(method, instance);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listenerAnnotation() {
        for (String packageName : packageList) {
            List<Class<?>> classes;
            try {
                classes = getClassesFromJar(packageName);
                for (Class<?> clazz : classes) {
                    if (clazz.isAnnotationPresent(CommandListener.class)) {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        Bukkit.getPluginManager().registerEvents((Listener) instance, plugin1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

