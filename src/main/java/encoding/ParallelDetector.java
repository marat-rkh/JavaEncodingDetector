package encoding;

import encoding.automaton.Automaton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mrx on 08.10.14.
 */
public class ParallelDetector {
    private final List<Automaton> automata;
    private List<Automaton> workingCopy;

    private final ByteBuffer bytes;

    private final double CONFIDENCE_THRESHOLD = 0.95;

    private ParallelDetector(List<Automaton> automata, int readPortionSize) throws FileNotFoundException {
        this.automata = automata;
        this.bytes = ByteBuffer.allocateDirect(readPortionSize);
    }

    public DetectionResult detect(String filePath) throws IOException {
        reset();
        workingCopy = new LinkedList<>(automata);
        ReadableByteChannel inChannel = null;
        try {
            inChannel = new FileInputStream(filePath).getChannel();
            while(inChannel.read(bytes) != -1) {
                if(!doAutomataIteration(false)) {
                    return null;
                }
            }
            if(!doAutomataIteration(true)) {
                return null;
            }
            Automaton bestFit = bestFitAutomaton();
            return bestFit == null ? null : new DetectionResult(bestFit.getCharset(), bestFit.getConfidence());
        } finally {
            if(inChannel != null) {
                inChannel.close();
            }
        }
    }

    private boolean doAutomataIteration(boolean isEnd) {
        feedAutomata(isEnd);
        if (workingCopy.size() == 0) {
            return false;
        }
        bytes.clear();
        return true;
    }

    private void feedAutomata(boolean isEnd) {
        Iterator<Automaton> it = workingCopy.iterator();
        bytes.flip();
        while (it.hasNext()) {
            Automaton automaton = it.next();
            automaton.feed(bytes, isEnd);
            if(automaton.getState().equals(Automaton.State.ERROR)) {
                it.remove();
            }
            bytes.rewind();
        }
    }

    private Automaton bestFitAutomaton() {
        if(automata.size() != 0) {
            Iterator<Automaton> it = workingCopy.iterator();
            Automaton best = it.next();
            while (it.hasNext()) {
                Automaton current = it.next();
                if(current.getConfidence() > best.getConfidence()) {
                    best = current;
                }
            }
            if(best.getConfidence() < CONFIDENCE_THRESHOLD) {
                return null;
            }
            return best;
        }
        return null;
    }

    private void reset() {
        for(Automaton e : automata) {
            e.reset();
        }
        bytes.clear();
    }

    public static ParallelDetector standardDetector() throws FileNotFoundException {
        int readPortionSize = 2 * 1024;
        List<Automaton> automata = new LinkedList<>();
        automata.add(new Automaton(StandardCharsets.UTF_8, readPortionSize));
        automata.add(new Automaton(StandardCharsets.US_ASCII, readPortionSize));
        automata.add(new Automaton(StandardCharsets.ISO_8859_1, readPortionSize));
        automata.add(new Automaton(StandardCharsets.UTF_16, readPortionSize));
        automata.add(new Automaton(StandardCharsets.UTF_16BE, readPortionSize));
        automata.add(new Automaton(StandardCharsets.UTF_16LE, readPortionSize));
        return new ParallelDetector(automata, readPortionSize);
    }
}
