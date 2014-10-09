package encoding;

import encoding.automaton.AutomatonR;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mrx on 08.10.14.
 */
public class ParallelDetector {
    private final List<AutomatonR> automata;
    private List<AutomatonR> workingCopy;

    private final ByteBuffer bytes = ByteBuffer.allocateDirect(2 * 1024);

    public ParallelDetector(List<AutomatonR> automata) throws FileNotFoundException {
        this.automata = automata;
    }

    public DetectionResult detect(String filePath) throws IOException {
        reset();
        workingCopy = new LinkedList<>(automata);
        ReadableByteChannel inChannel = null;
        try {
            inChannel = new FileInputStream(filePath).getChannel();
            while(inChannel.read(bytes) != -1) {
                feedAutomata(bytes, false);
                if (workingCopy.size() == 0) {
                    return null;
                }
                bytes.clear();
            }
            feedAutomata(bytes, true);
            if (workingCopy.size() == 0) {
                return null;
            }
            AutomatonR bestFit = bestFitAutomaton();
            return new DetectionResult(bestFit.getCharset(), bestFit.getConfidence());
        } finally {
            if(inChannel != null) {
                inChannel.close();
            }
        }
    }

    private void feedAutomata(ByteBuffer bytes, boolean isEnd) {
        Iterator<AutomatonR> it = workingCopy.iterator();
        bytes.flip();
        while (it.hasNext()) {
            AutomatonR automaton = it.next();
            automaton.feed(bytes, isEnd);
            if(automaton.getState().equals(AutomatonR.State.ERROR)) {
                it.remove();
            }
            bytes.rewind();
        }
    }

    private AutomatonR bestFitAutomaton() {
        if(automata.size() != 0) {
            Iterator<AutomatonR> it = workingCopy.iterator();
            AutomatonR best = it.next();
            while (it.hasNext()) {
                AutomatonR current = it.next();
                if(current.getConfidence() > best.getConfidence()) {
                    best = current;
                }
            }
            return best;
        }
        return null;
    }

    private void reset() {
        for(AutomatonR e : automata) {
            e.reset();
        }
        bytes.clear();
    }
//
//    public static ByteBuffer clone(ByteBuffer original) {
//        ByteBuffer clone = ByteBuffer.allocate(original.capacity());
//        original.rewind();//copy from the beginning
//        clone.put(original);
//        original.rewind();
//        clone.flip();
//        return clone;
//    }
}
