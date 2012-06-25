package demo.aggregator;

import com.m3.multilane.action.Action;
import com.m3.multilane.action.HttpGetToStringAction;
import com.m3.scalaflavor4j.Either;
import com.m3.scalaflavor4j.Right;

public class EchoAction extends HttpGetToStringAction implements Action<String, String> {

    private String value;

    public EchoAction(String value) {
        super(value, 1000);
        this.value = value;
    }

    @Override
    public Either<Throwable, String> apply() {
        return Right._(value);
    }

}

