package encoding.automaton;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * Created by mrx on 09.10.14.
 */
public class Validator {
    private final ByteBuffer localBuffer = ByteBuffer.allocate(4 * 1024);
    private final byte[] exchangeBuffer = new byte[2 * 1024];
    private final CharsetDecoder decoder;
    private final CharBuffer chars = CharBuffer.allocate(2 * 1024);
    private final char[] array = chars.array();

    private State state = State.OK;

    public Validator(Charset charset) {
        decoder = charset.newDecoder();
    }

    public char[] feed(ByteBuffer bytes, boolean isEnd) {
        if(!state.equals(State.ERROR)) {
            int availableBytes = bytes.remaining();
            bytes.get(exchangeBuffer, 0, availableBytes);
            localBuffer.put(exchangeBuffer, 0, availableBytes);
            localBuffer.flip();
            if (!isEnd) {
                CoderResult result = decoder.decode(localBuffer, chars, false);
                if (result.isError()) {
                    state = State.ERROR;
                    return null;
                }
                chars.flip();
                int offs = chars.position();
                int len = chars.remaining();
                char[] decodedChars = new char[len];
                System.arraycopy(array, offs, decodedChars, 0, len);
                chars.clear();
                localBuffer.compact();
                return decodedChars;
            } else {
                CoderResult result = decoder.decode(localBuffer, chars, true);
                if(result.isError()) {
                    state = State.ERROR;
                    return null;
                }
                decoder.flush(chars);
                chars.flip();
                int offs = chars.position();
                int len = chars.remaining();
                char[] decodedChars = new char[len];
                System.arraycopy(array, offs, decodedChars, 0, len);
                chars.clear();
                localBuffer.compact();
                return decodedChars;
            }
        }
        return null;
    }

    public void reset() {
        decoder.reset();
        chars.clear();
        state = State.OK;
        localBuffer.clear();
    }

    public State getState() {
        return state;
    }

    public enum State {
        ERROR, OK
    }
}
