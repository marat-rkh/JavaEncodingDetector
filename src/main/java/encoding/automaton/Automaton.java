package encoding.automaton;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by mrx on 09.10.14.
 */
public class Automaton {
    private final Validator validator;
    private final ControlsCounter controlsCounter = new ControlsCounter();
    private final Charset charset;

    private State state = State.OK;

    public Automaton(Charset charset, int bytesBufferSize) {
        this.charset = charset;
        this.validator = new Validator(charset, bytesBufferSize);
    }

    public void feed(ByteBuffer bytes, boolean isEnd) {
        if(!state.equals(State.ERROR)) {
            char[] decodedChars = validator.feed(bytes, isEnd);
            if(validator.getState().equals(Validator.State.ERROR)) {
                state = State.ERROR;
                return;
            }
            controlsCounter.feed(decodedChars);
            if(isEnd) {
                assert(!controlsCounter.hasPrev());
            }
        }
    }

    public State getState() {
        return state;
    }

    public double getConfidence() {
        return 1 - controlsCounter.getPercentage();
    }

    public Charset getCharset() {
        return charset;
    }

    public void reset() {
        validator.reset();
        controlsCounter.reset();
        state = State.OK;
    }

    public enum State {
        ERROR, OK
    }
}
