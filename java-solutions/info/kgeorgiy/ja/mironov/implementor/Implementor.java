package info.kgeorgiy.ja.mironov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * Class implementing {@link Impler}. Provides public methods to implement <code>.java</code>
 * files for classes extending or implementing given class of interface.
 *
 * @author mironov
 */

public class Implementor implements JarImpler {

    /**
     * Creates a new instance of {@code Implementor}.
     */
    public Implementor() {
    }


    /**
     * Creates {@link java.lang.String} with <em>default value</em> returned from some {@link java.lang.reflect.Method}.
     *
     * @param token {@link java.lang.Class} returned by some {@link java.lang.reflect.Method}.
     * @return {@link java.lang.String} with <em>default value</em>.
     */
    private static String getDefaultValue(final Class<?> token) {
        if (token.equals(boolean.class)) {
            return " false";
        } else if (token.equals(void.class)) {
            return "";
        } else if (token.isPrimitive()) {
            return " 0";
        }
        return " null";
    }

    /**
     * Main function. Provides console interface for {@link Implementor}.
     * Runs in two modes depending on {@code args}:
     * <ol>
     * <li>2-argument <code>className outputPath</code> creates <code>.java</code> file by executing
     * provided with {@link Impler} method {@link #implement(Class, Path)}</li>
     * <li>3-argument <code>-jar className jarOutputPath</code> creates <code>.jar</code> file by executing
     * provided with {@link JarImpler} method {@link #implementJar(Class, Path)}</li>
     * </ol>
     * All arguments must be correct and not-null. If some arguments are incorrect
     * or an error occurs in runtime an information message is printed and implementation is aborted.
     *
     * @param args command line arguments for application
     */
    public static void main(final String[] args) {
        if (args == null || (args.length != 2 && args.length != 3)) {
            System.err.println("Wrong count of args");
            return;
        }
        final JarImpler implementor = new Implementor();
        try {
            Path impl = Paths.get(args[1]);
            if (args.length == 2) {
                implementor.implement(Class.forName(args[0]), impl);
            } else if (args[0].equals("-jar")) {
                Path implJar = Paths.get(args[2]);
                implementor.implementJar(Class.forName(args[1]), implJar);
            } else {
                System.err.println("Incorrect arguments.");
            }
        } catch (final InvalidPathException e) {
            System.err.println("Incorrect path to root: " + e.getMessage());
        } catch (final ClassNotFoundException e) {
            System.err.println("Incorrect class name: " + e.getMessage());
        } catch (final ImplerException e) {
            System.err.println("An ERROR occurred during implementation: " + e.getMessage());
        }
    }

    /**
     * Create <em>implementation</em> {@code class} that implement base class.
     *
     * @param token {@link java.lang.Class} token to create <em>implementation</em> for.
     * @param root  {@link java.nio.file.Path} to <em>root</em> directory.
     * @throws ImplerException if <em>implementation</em> for given {@link java.lang.Class}
     *                         cannot be generated for one of such reasons:
     *                         <ul>
     *                         <li> Some arguments are {@code null}</li>
     *                         <li> Given {@link java.lang.Class} is {@code primitive}
     *                         or <strong>{@code array}</strong>. </li>
     *                         <li> Given {@link java.lang.Class} is {@code final class} or {@link Enum}. </li>
     *                         <li> Given {@link java.lang.Class} isn't an {@code Interface} </li>
     *                         <li> The problems with I/O occurred during <em>implementation</em>. </li>
     *                         </ul>
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        checkToken(token);
        Path path = createOutputFile(token.getPackage().getName(), getClassName(token), root);
        createDirectories(path);
        if (!token.isInterface()) {
            throw new ImplerException("Token is not an interface");
        }

        try (Writer writer = Files.newBufferedWriter(path)) {
            writer.write(getImpl(token));
        } catch (IOException e) {
            throw new ImplerException("Error writing implementation", e);
        }
    }

    /**
     * Creates parent directories of specified file or directory.
     *
     * @param root {@link java.nio.file.Path} to specified file or directory.
     * @throws ImplerException if catch {@link java.io.IOException} when creating parent directories.
     */
    private void createDirectories(final Path root) throws ImplerException {
        try {
            Files.createDirectories(root.getParent());
        } catch (final IOException e) {
            throw new ImplerException("Can't create directories for output file", e);
        }
    }

    /**
     * Resolves {@code root} {@link java.nio.file.Path} to <em><strong>.java</strong></em> file.
     *
     * @param root      {@link java.nio.file.Path} to {@code root} file.
     * @param className {@link java.lang.Class} token to get {@link java.nio.file.Path} for.
     * @param name      {@link java.lang.Class}
     * @return {@link java.nio.file.Path} from {@code root} directory to file.
     */
    private Path createOutputFile(String name, String className, Path root) {
        return root.resolve(name.replace('.', File.separatorChar))
                .resolve(className + ".java");
    }

    private String getClassName(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }

    /**
     * Check is class can be generated
     *
     * @param token given class to be checked
     * @throws ImplerException if the implementation of given class cannot be generated for one of following reasons:
     *                         <ul>
     *                         <li> Given class is null</li>
     *                         <li> Given class is primitive or array. </li>
     *                         <li> Given class is final class or {@link Enum}. </li>
     *                         </ul>
     */
    private void checkToken(Class<?> token) throws ImplerException {
        if (token.isPrimitive() || token.isArray() || Modifier.isFinal(token.getModifiers())
                || Modifier.isPrivate(token.getModifiers()) || token == Enum.class || token.isEnum()) {
            throw new ImplerException("Class can't implemented");
        }
    }


    /**
     * Creat package and method
     *
     * @param token give class to set package
     * @return {@link java.lang.String} class package and method
     */
    private String getImpl(Class<?> token) throws ImplerException {
        StringBuilder result = new StringBuilder();
        if (!token.getPackageName().isEmpty()) {
            result.append("package ").append(token.getPackage().getName()).append(";").append(System.lineSeparator());
        }

        result.append("public class ").append(token.getSimpleName()).append("Impl implements ")
                .append(token.getCanonicalName()).append(" {").append(System.lineSeparator());

        for (Method method : token.getMethods()) {
            result.append(getMethod(method));
        }

        result.append("}");

        return result.toString();
    }

    /**
     * Add method to main file
     *
     * @param method {@link java.lang}
     * @return {@link java.lang.String} class method
     */
    private String getMethod(Method method) throws ImplerException {
        if (Modifier.isFinal(method.getModifiers())) {
            throw new ImplerException("Cannot override final method " + method);
        }

        StringBuilder result = new StringBuilder();

        result.append(Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT))
                .append(" ")
                .append(method.getReturnType().getCanonicalName())
                .append(" ")
                .append(method.getName())
                .append("(");

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            result.append(method.getParameterTypes()[i].getCanonicalName()).append(" arg").append(i);

            if (i != method.getParameterTypes().length - 1) {
                result.append(", ");
            }
        }

        result.append(") {");

        if (method.getReturnType() != Void.TYPE) {
            result.append("return ");
            result.append(getDefaultValue(method.getReturnType()));
            result.append(";");
        }

        result.append("}");

        return result.toString();
    }


    private String createClassName(String className, Path path) {
        return path.resolve(className).toString();
    }


    /**
     * Creates a <code>.jar</code> file containing compiled sources of class
     * implemented by {@link #implement(Class, Path)} class in location specified by {@code jarFile}.
     *
     * @param token   type token to create implementation for
     * @param jarFile target <code>.jar</code> file
     * @throws ImplerException if any error occurs during the implementation
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Path repDir = Paths.get("test");

        implement(token, repDir);

        String className = createClassName(getClassName(token),
                Paths.get(token.getPackage().getName().replace('.', File.separatorChar)));

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        Path pathToClasspath;
        try {
            pathToClasspath = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new ImplerException("URISyntaxException", e);
        }

        if (compiler.run(null, null, null, "-encoding", "UTF-8", "-classpath",
                repDir.getFileName() + File.pathSeparator +
                        pathToClasspath,
                repDir.resolve(className) + ".java") != 0) {
            throw new ImplerException("Failed to compile files");
        }

        try (JarOutputStream writer = new JarOutputStream(Files.newOutputStream(jarFile))) {
            writer.putNextEntry(new ZipEntry(className.replace(File.separatorChar, '/') + ".class"));
            Files.copy(Paths.get(repDir.resolve(className) + ".class"), writer);
        } catch (IOException e) {
            throw new ImplerException("Failed to write to jar file", e);
        }
    }

}

