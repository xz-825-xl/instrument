import java.lang.instrument.Instrumentation;

/**
 * Created by Mmn on 2019/6/29.
 * To be a happy coder!
 */
public class AgentMain {

    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer(new Transformer());
    }

}
