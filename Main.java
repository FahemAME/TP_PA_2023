import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class Main {
    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static String generateUnitTestClass(Class<?> c) {
        String className = capitalize(c.getName()) + "Test";
        Method[] methods = c.getDeclaredMethods();
        String testClassCode = "public class " + className + "{\n";
        String testMethodsCode = "";

        for(Method method : methods) {
            testMethodsCode+= "\t" + generateUnitTestMethod(c.getName(), method);
        }
        testClassCode += testMethodsCode;
        testClassCode += "}\n";
        return testClassCode;
    }

    public static String genUnitTestInitializeParams(Method method) {
        String initialize_seq_parms = "";
        Parameter[] parameters = method.getParameters();       

        for (Parameter parameter : parameters) {
            initialize_seq_parms += "\t\t" + parameter.getType().getSimpleName();
            initialize_seq_parms += " " + parameter.getName() + " = ";
            initialize_seq_parms += initializeTypes(parameter.getType().getSimpleName());
            initialize_seq_parms += ";\n";
        }
        return initialize_seq_parms;
    }

    public static String initializeTypes(String type) {
        String initialize_Method_Type = "";
        switch(type) {
          case "char":
          case "String":
            initialize_Method_Type = "\"\"";
            break;
          case "int":
          case "byte":
          case "double":
          case "float":
          case "long":
          case "short":
            initialize_Method_Type = "0";
            break;
          case "boolean":
            initialize_Method_Type = "false";
            break;
          default:
            initialize_Method_Type = "Null";
        }
        return initialize_Method_Type;
    }

    public static String generateUnitTestMethod(String className, Method method) {
        String testName = "Test" + capitalize(method.getName());
        String expectedResultType = method.getReturnType().getSimpleName();
        String lineexpectedResult = expectedResultType + " expectedResult = ";
        lineexpectedResult += initializeTypes(expectedResultType) +";\n";

        String instance = className + " instance = new " + className + "();\n";
        String params = generateUnitTestParametersMethod(method);
        String init_params = genUnitTestInitializeParams(method);
        String lineactualResult = expectedResultType + " actualResult = ";
        lineactualResult += "instance." + method.getName() + "(" + params + ");\n";


        String testMethodCode = "";
        testMethodCode += "public void " + testName + "(){\n";
        testMethodCode += "\t\t// Arrange\n";
        testMethodCode += init_params;
        testMethodCode += "\t\t" + lineexpectedResult;
        testMethodCode += "\t\t" + instance;
        testMethodCode += "\t\t// Act\n";
        testMethodCode += "\t\t" + lineactualResult;
        testMethodCode += "\t\t// Assert\n";
        testMethodCode += "\t\tassertEquals(actualResult, expectedResult);\n";
        testMethodCode += "\t\t}\n";
        return testMethodCode;
    }

    public static String generateUnitTestParametersMethod(Method method) {
        String seq_parms_method = "";
        Parameter[] parameters = method.getParameters();       
        for (Parameter parameter : parameters) {
            seq_parms_method = seq_parms_method + parameter.getName() + ", ";
        }
        if(seq_parms_method.length() > 0) {
            seq_parms_method = seq_parms_method.substring(0, seq_parms_method.length() - 2);
        }
        
        return seq_parms_method;
    }

    public static void main(String[] args) {
        try {   
            String genTest1 = "";
            String[] classes = {"Class_1", "Class_2", "NimporteQuelleClasse"};
            
            
            for (int i = 0; i < classes.length; i++) {
                Class my_class2 = Class.forName(classes[i]);
                Object o2 = my_class2.newInstance();
                Class<?> co2 = o2.getClass();
                genTest1 += generateUnitTestClass(co2);
            }

            System.out.println(genTest1); 
            
            } catch (Exception e) {e.printStackTrace();} 
    }

}

class Class_1 {
    public double method_1_1(int a, String b, double c ) {return 0;}
    public byte method_1_2(int a) {return 0;}
    public float method_1_3(int a,int b ) {return 0;}
}
class Class_2 {
    public String method_2_1(long a, short b, char c ) {return "";}
}
class NimporteQuelleClasse {
    public boolean NimporteQuelleMethode(double a, int b ) {return false;}
}
