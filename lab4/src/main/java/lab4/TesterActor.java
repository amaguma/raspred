package lab4;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TesterActor extends AbstractActor {
    private ActorRef storageActor;

    public TesterActor(ActorRef storageActor) {
        this.storageActor = storageActor;
    }

    private String runTest(TestData testData) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName()
    }
}
