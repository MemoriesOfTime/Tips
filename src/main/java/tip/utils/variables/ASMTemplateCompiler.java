package tip.utils.variables;

import org.objectweb.asm.*;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.objectweb.asm.Opcodes.*;

public class ASMTemplateCompiler {
    private static final Pattern variablePattern = Pattern.compile("\\{([^}]+)}");

    private static final Map<String, Function<Map<String, String>, String>> templateCache = new ConcurrentHashMap<>();

    public static Function<Map<String, String>, String> compile(String template) {
        return templateCache.computeIfAbsent(template, ASMTemplateCompiler::createTemplateFunction);
    }

    private static Function<Map<String, String>, String> createTemplateFunction(String template) {
        try {
            String className = "GeneratedTemplate_" + template.hashCode();
            ClassWriter cw = new ClassWriter(0);
            cw.visit(V1_8, ACC_PUBLIC, className, null, "java/lang/Object", new String[]{Function.class.getName().replace('.', '/')});

            // 生成默认构造函数
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();

            // 生成apply方法
            mv = cw.visitMethod(ACC_PUBLIC, "apply", "(Ljava/util/Map;)Ljava/lang/String;", null, null);
            mv.visitCode();

            // 创建StringBuilder实例
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

            // 解析模板并生成字节码
            int lastIndex = 0;
            java.util.regex.Matcher matcher = variablePattern.matcher(template);

            while (matcher.find()) {
                // 插入静态字符串片段
                if (matcher.start() > lastIndex) {
                    mv.visitLdcInsn(template.substring(lastIndex, matcher.start()));
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                }
                // 插入变量片段
                mv.visitVarInsn(ALOAD, 1);
                mv.visitLdcInsn(matcher.group(1));
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
                mv.visitTypeInsn(CHECKCAST, "java/lang/String");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

                lastIndex = matcher.end();
            }

            // 插入最后的静态字符串片段
            if (lastIndex < template.length()) {
                mv.visitLdcInsn(template.substring(lastIndex));
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }

            // 调用StringBuilder.toString()
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();

            cw.visitEnd();

            // 将生成的字节码加载为Class
            byte[] bytecode = cw.toByteArray();
            Class<?> generatedClass = new DynamicClassLoader().defineClass(className, bytecode);
            return (Function<Map<String, String>, String>) generatedClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to compile template", e);
        }
    }

    static class DynamicClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
