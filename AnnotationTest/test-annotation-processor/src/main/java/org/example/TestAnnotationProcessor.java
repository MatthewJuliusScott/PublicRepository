package org.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("org.example.TestAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class TestAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(TestAnnotation.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                // Process the annotated setter method
                injectPrint(element);
            }
        }
        return true;
    }

    private void injectPrint(Element element) {
        try {

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Attempting to inject print into " + element.getSimpleName());

            // Get the fully qualified class name and the method
            String className = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
            String methodName = element.getSimpleName().toString();

            // Load the class using Javassist
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.get(className);

            // Get the setter method
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

            // Insert RMI logic at the start of the method
            ctMethod.insertBefore("{ System.out.println(\"This was added by a custom annotation.\"); }");

            // Write the modified class to disk (this step can vary based on your environment)
            ctClass.writeFile();

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Successfully injected print into " + methodName);

        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }
}
