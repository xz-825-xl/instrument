import java.lang.instrument.Instrumentation;

/**
 * @author zy
 * To be a happy coder!
 */
public class AgentMain {

    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new Transformer());
    }

}
