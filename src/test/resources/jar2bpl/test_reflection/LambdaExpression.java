package jar2bpl.test_reflection;

public class LambdaExpression
{
   public static void main(String[] args)
   {
      Runnable r = () -> System.out.println("Hello");
      r.run();
   }
}