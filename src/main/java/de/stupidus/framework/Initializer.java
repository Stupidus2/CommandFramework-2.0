package de.stupidus.framework;

import de.stupidus.api.Initialize;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Initializer {
    private List<String> packageList = new ArrayList<>();

    public void register(JavaPlugin plugin) {
        for (String packageName : packageList) {
            try {
                Bukkit.getConsoleSender().sendMessage("[CommandFramework] §cInitializing commands");
                List<Class<?>> classes = getClassesFromJar(packageName);
                for (Class<?> clazz : classes) {
                    if (clazz.isAnnotationPresent(Initialize.class)) {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        for (Method method : clazz.getMethods()) {
                            if (method.getName().equalsIgnoreCase("subCommandInitialize")) {
                                invokeMethod(method, instance);
                            }
                        }
                    }
                }
                Bukkit.getConsoleSender().sendMessage("[CommandFramework] §cInitializing commands was successful!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (plugin != null) {
            for (String name : CommandFramework.getAllCommandNamesCommandExecutor().keySet()) {
                plugin.getCommand(name).setExecutor(CommandFramework.getAllCommandNamesCommandExecutor().get(name));
                plugin.getCommand(name).setTabCompleter(CommandFramework.getAllCommandNamesTabCompleter().get(name));
            }
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
}
