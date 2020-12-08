package lab4;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TesterActor extends AbstractActor {

    private static final String JS_VERSION = "nashorn";

    private ActorRef storageActor;

    public TesterActor(ActorRef storageActor) {
        this.storageActor = storageActor;
    }

    private String runTest(TestData testData) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName(JS_VERSION);
        engine.eval(testData.getParentPackage().getCode());
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(testData.getParentPackage().getFunctionName(), testData.getParams()).toString();
    }

    private TestData checkRes(TestData testData) {
        try {
            String getResult = runTest(testData);
            testData.setActualResult(getResult);
        } catch (Exception exception) {
            String error = exception.toString();
            testData.setActualResult(error);
        }
        return testData;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder
                .create()
                .match(TestData.class, msg -> storageActor.tell(checkRes(msg), ActorRef.noSender()))
                .build();
    }
}
