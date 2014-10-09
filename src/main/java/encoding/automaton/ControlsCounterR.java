package encoding.automaton;

/**
 * Created by mrx on 09.10.14.
 */
public class ControlsCounterR {
    private char prev;
    private boolean hasPrev = false;

    private int allChars = 0;
    private int controlsNum = 0;

    public void feed(char[] chars) {
        assert(chars.length != 0);
        int i = 0;
        if(hasPrev) {
            i = 1;
            count(codePointForPair(prev, chars[0]));
            hasPrev = false;
        }
        for(; i < chars.length; i++) {
            if(Character.isHighSurrogate(chars[i])) {
                if(i != chars.length - 1) {
                    count(codePointForPair(chars[i], chars[i + 1]));
                    i += 1;
                } else {
                    hasPrev = true;
                    prev = chars[i];
                }
            } else {
                char[] singleChar = new char[] {chars[i]};
                count(Character.codePointAt(singleChar, 0));
            }
        }
    }

    public void reset() {
        hasPrev = false;
        allChars = 0;
        controlsNum = 0;
    }

    public double getPercentage() {
        return controlsNum * 1.0 / allChars;
    }

    public boolean hasPrev() {
        return hasPrev;
    }

    private int codePointForPair(char fst, char snd) {
        assert(!Character.isHighSurrogate(snd));
        char[] pair = new char[]{fst, snd};
        return Character.codePointAt(pair, 0);
    }

    private void count(int codePoint) {
        allChars += 1;
        assert (Character.isDefined(codePoint) && Character.isValidCodePoint(codePoint));
        if(Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
            controlsNum += 1;
        }
    }
}
