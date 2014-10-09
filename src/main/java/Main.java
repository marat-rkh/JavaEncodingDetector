import encoding.DetectionResult;
import encoding.ParallelDetector;
import encoding.automaton.Automaton;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mrx on 09.10.14.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        test();
    }

    private static void test() throws IOException {
        List<Automaton> automata = new LinkedList<>();
        automata.add(new Automaton(StandardCharsets.UTF_8));
        automata.add(new Automaton(StandardCharsets.US_ASCII));
        automata.add(new Automaton(StandardCharsets.ISO_8859_1));
        automata.add(new Automaton(StandardCharsets.UTF_16));
        automata.add(new Automaton(StandardCharsets.UTF_16BE));
        automata.add(new Automaton(StandardCharsets.UTF_16LE));
        ParallelDetector detector = new ParallelDetector(automata);
        String hpIso = "/home/mrx/hpIso";
        parallelDetect(hpIso, detector);
        String hpAnsi = "/home/mrx/hpAnsi";
        parallelDetect(hpAnsi, detector);
        String hp8 = "/home/mrx/hp8";
        parallelDetect(hp8, detector);
        String hp16 = "/home/mrx/hp16";
        parallelDetect(hp16, detector);
        String hp16be = "/home/mrx/hp16be";
        parallelDetect(hp16be, detector);
        String hp16le = "/home/mrx/hp16le";
        parallelDetect(hp16le, detector);

        System.out.println();
        String vimIso = "/home/mrx/vimIso";
        parallelDetect(vimIso, detector);
        String vimAnsi = "/home/mrx/vimAnsi";
        parallelDetect(vimAnsi, detector);
        String vim8 = "/home/mrx/vim8";
        parallelDetect(vim8, detector);
        String vim16 = "/home/mrx/vim16";
        parallelDetect(vim16, detector);
        String vim16be = "/home/mrx/vim16be";
        parallelDetect(vim16be, detector);
        String vim16le = "/home/mrx/vim16le";
        parallelDetect(vim16le, detector);

        System.out.println();
        String pdfJava = "/home/mrx/Dropbox/Study/Term2/Java/Java2014_01.pdf";
        parallelDetect(pdfJava, detector);
        String zipJava = "/home/mrx/Dropbox/Study/Term2/Java/hw1";
        parallelDetect(zipJava, detector);
        String jarJava = "/home/mrx/Dropbox/Study/Term2/Java/hw1.jar";
        parallelDetect(jarJava, detector);
        String ninja = "/home/mrx/GitRepos/depot_tools/ninja.exe";
        parallelDetect(ninja, detector);
        String cr = "/home/mrx/GitRepos/depot_tools/create-ntfs-junction.exe";
        parallelDetect(cr, detector);
        String ninja_l32 = "/home/mrx/GitRepos/depot_tools/ninja-linux32";
        parallelDetect(ninja_l32, detector);
        String ninja_l64 = "/home/mrx/GitRepos/depot_tools/ninja-linux64";
        parallelDetect(ninja_l64, detector);

        System.out.println();
        String vid = "/home/mrx/Downloads/2014.07.16 - DUM SPIRO SPERO AT NIPPON BUDOKAN/DVD 01/VIDEO_TS.VOB";
        parallelDetect(vid, detector);
        String unkn = "/home/mrx/Downloads/2014.07.16 - DUM SPIRO SPERO AT NIPPON BUDOKAN/DVD 01/VIDEO_TS.BUP";
        parallelDetect(unkn, detector);
        String musFlac = "/home/mrx/Downloads/Pallbearer - Foundations of Burden/01 - Worlds Apart.flac";
        parallelDetect(musFlac, detector);
        String img = "/home/mrx/Downloads/Pallbearer - Foundations of Burden/00.jpg";
        parallelDetect(img, detector);
        String tar1 = "/home/mrx/Downloads/libreadline-java-0.8.0-src.tar.gz";
        parallelDetect(tar1, detector);
        String zip = "/home/mrx/Downloads/events-KeyEventDemoProject.zip";
        parallelDetect(zip, detector);
        String emuArm = "/home/mrx/Installed/android-sdk-linux/tools/emulator64-arm";
        parallelDetect(emuArm, detector);
        String emu86 = "/home/mrx/Installed/android-sdk-linux/tools/emulator64-x86";
        parallelDetect(emu86, detector);

        System.out.println();
        String srcJava1 = "/home/mrx/GitRepos/intellij-community/python/src/com/jetbrains/pyqt/CompileQrcAction.java";
        parallelDetect(srcJava1, detector);
        String srcJava2 = "/home/mrx/GitRepos/intellij-community/plugins/ant/src/com/intellij/lang/ant/config/execution/AntBuildMessageView.java";
        parallelDetect(srcJava2, detector);
        String srcJava3 = "/home/mrx/GitRepos/intellij-community/plugins/ant/src/com/intellij/lang/ant/config/execution/AntMessageCustomizer.java";
        parallelDetect(srcJava3, detector);
        String srcJava4 = "/home/mrx/GitRepos/intellij-community/plugins/ant/src/com/intellij/lang/ant/config/execution/TreeView.java";
        parallelDetect(srcJava4, detector);
        String srcPy1 = "/home/mrx/GitRepos/depot_tools/gclient.py";
        parallelDetect(srcPy1, detector);
        String srcPy2 = "/home/mrx/GitRepos/depot_tools/gclient-new-workdir.py";
        parallelDetect(srcPy2, detector);
        String srcCpp1 = "/home/mrx/emacs/src/dispextern.h";
        parallelDetect(srcCpp1, detector);
        String srcCpp2 = "/home/mrx/emacs/src/data.c";
        parallelDetect(srcCpp2, detector);
        String srcCpp3 = "/home/mrx/emacs/src/ftfont.c";
        parallelDetect(srcCpp3, detector);
    }

    private static void parallelDetect(String filePath, ParallelDetector detector) throws IOException {
        DetectionResult result = detector.detect(filePath);
        if(result != null) {
            System.out.println(filePath + " " + result.getCharset().name() + " " + result.getConfidence());
        } else {
            System.out.println(filePath + " null");
        }
    }
}
