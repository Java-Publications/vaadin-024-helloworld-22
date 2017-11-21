package org.rapidpm.vaadin.trainer.modules.mainview.calc.basics;

import static java.lang.System.nanoTime;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.rapidpm.frp.model.serial.Tripel;

/**
 *
 */
public class CalcBasicsService {

  public enum Operator {

    ADD((a , b) -> (float) (a + b) , "+"),
    SUB((a , b) -> (float) (a - b) , "-"),
    MUL((a , b) -> (float) (a * b) , "*"),
    DIV((a , b) -> (float) (a / b) , ":");

    private String sign;
    private static final Random random = new Random(System.nanoTime());
    private BiFunction<Integer, Integer, Float> fkt;

    Operator(BiFunction<Integer, Integer, Float> fkt , String sign) {
      this.fkt = fkt;
      this.sign = sign;
    }

    public BiFunction<Integer, Integer, Float> fkt() {
      return fkt;
    }

    public String sign() {return sign;}

    public static Operator rndOperator() {
      final Operator[] values = Operator.values();
      return values[random.nextInt(values.length)];
    }

    public static Operator rndNoDivOperator() {
      final Operator[] values = Operator.values();
      return values[random.nextInt(values.length - 1)];
    }

  }

  public static class BasicArithmeticTask
      extends Tripel<Integer, Integer, Operator> {

    public BasicArithmeticTask(Integer a , Integer b , Operator o) {
      super(a , b , o);
    }

    public Integer a() {return getT1();}

    public Integer b() {return getT2();}

    public Operator operator() {return getT3();}

  }

  public static Function<Integer, BasicArithmeticTask> nextRndTask() {
    return (bound) -> nextTask().apply(Operator.rndOperator() , bound);
  }

  public static Function<Integer, BasicArithmeticTask> nextRndNoDivTask() {
    return (bound) -> nextTask().apply(Operator.rndNoDivOperator() , bound);
  }

  public static BiFunction<Operator, Integer, BasicArithmeticTask> nextTask() {
    return (op , bound) -> {
      final Random random = new Random(nanoTime());
      final int a = random.nextInt(bound);
      final int b = random.nextInt(bound);

      switch (op) {
//        case ADD:break;
//        case MUL:break;
        case SUB:
          return (a > b) ? new BasicArithmeticTask(a , b , op)
                         : new BasicArithmeticTask(b , a , op);
        case DIV: return new BasicArithmeticTask((a*b) , b , op);
        default:
          return new BasicArithmeticTask(a , b , op);
      }
    };
  }

}
