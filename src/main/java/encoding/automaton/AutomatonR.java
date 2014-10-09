package encoding.automaton;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by mrx on 09.10.14.
 */
public class AutomatonR {
    private final ValidatorR validatorR;
    private final ControlsCounterR controlsCounterR = new ControlsCounterR();
    private final Charset charset;

    private State state = State.OK;

    public AutomatonR(Charset charset) {
        this.charset = charset;
        this.validatorR = new ValidatorR(charset);
    }

    public void feed(ByteBuffer bytes, boolean isEnd) {
        if(!state.equals(State.ERROR)) {
            char[] decodedChars = validatorR.feed(bytes, isEnd);
            if(validatorR.getState().equals(ValidatorR.State.ERROR)) {
                state = State.ERROR;
                return;
            }
            controlsCounterR.feed(decodedChars);
            if(isEnd) {
                assert(!controlsCounterR.hasPrev());
            }
        }
    }

    public State getState() {
        return state;
    }

    public double getConfidence() {
        return 1 - controlsCounterR.getPercentage();
    }

    public Charset getCharset() {
        return charset;
    }

    public void reset() {
        validatorR.reset();
        controlsCounterR.reset();
        state = State.OK;
    }

    public enum State {
        ERROR, OK
    }
}
