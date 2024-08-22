package tip.utils.variables;

import org.objectweb.asm.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.objectweb.asm.Opcodes.*;

public class ASMTemplateCompiler {
    private static final Pattern variablePattern = Pattern.compile("\\{([^}]+)}");

    private static final Map<String, Function<Map<String, String>, String>> templateCache = new ConcurrentHashMap<>();

    private Function<Map<String, String>, String> templateFunc;

    public ASMTemplateCompiler(String template) {
        templateFunc = templateCache.computeIfAbsent(template, ASMTemplateCompiler::createTemplateFunction);
    }

    public static ASMTemplateCompiler compile(String template) {
        return new ASMTemplateCompiler(template);
    }

    public String strReplace(Map<String, String> variables) {
        // 调用模板函数，进行替换
        return templateFunc.apply(variables);
    }

    private static Function<Map<String, String>, String> createTemplateFunction(String template) {
        try {
            String className = "GeneratedTemplate_" + template.hashCode();

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            cw.visit(V17, ACC_PUBLIC, className, null, "java/lang/Object", new String[]{Function.class.getName().replace('.', '/')});

            // 生成默认构造函数
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();

            // 生成superReplace方法
            mv = cw.visitMethod(ACC_PUBLIC, "superReplace", "(Ljava/util/Map;)Ljava/lang/String;", null, null);
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
                mv.visitVarInsn(ALOAD, 1);  // 加载Map对象
                mv.visitLdcInsn("{" + matcher.group(1) + "}");  // 变量有大括号
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);

                // 判断是否为null，如果是null，使用原始变量字符串
                Label notNullLabel = new Label();
                mv.visitInsn(DUP);  // 复制Map.get的结果到栈顶
                mv.visitJumpInsn(IFNONNULL, notNullLabel);  // 如果非null跳到notNullLabel
                mv.visitInsn(POP);  // 弹出null值
                mv.visitLdcInsn("{" + matcher.group(1) + "}");  // 使用原始变量字符串
                mv.visitJumpInsn(GOTO, notNullLabel);

                mv.visitLabel(notNullLabel);  // 标签位置
                mv.visitTypeInsn(CHECKCAST, "java/lang/String");  // 将结果转换为String
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
            mv.visitMaxs(0, 0); // 自动计算
            mv.visitEnd();

            // 将生成的字节码加载为Class
            byte[] bytecode = cw.toByteArray();
            Class<?> generatedClass = new DynamicClassLoader().defineClass(className, bytecode);

            // 使用lambda表达式包装生成的类，并调用superReplace方法
            return variables -> {
                try {
                    Object instance = generatedClass.getDeclaredConstructor().newInstance();
                    return (String) generatedClass.getDeclaredMethod("superReplace", Map.class).invoke(instance, variables);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };

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
