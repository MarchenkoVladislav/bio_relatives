package genome.compare;


import genome.compare.analyzis.GeneComparisonResult;
import genome.compare.analyzis.GeneComparisonResultAnalyzer;
import genome.compare.comparator.GenomeComparator;
import genome.compare.comparator.threads.GenomeComparatorThread;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Tests for the {@link GenomeComparator} class.
 *
 * @author Vladislav Marchenko
 */
public class GenomeComparatorTest {
    /**
     * Path to the first test BAM file
     */
    private final static  String PATH_TO_MOM_BAM_1 = "src/test/resources/genome/compare/testMotherX.bam";

    /**
     * Path to the second test BAM file
     */
    private final static  String PATH_TO_SON_BAM_1 = "src/test/resources/genome/compare/testSon4.bam";

    /**
     * Path to the first test BAM file
     */
    private final static  String PATH_TO_MOM_BAM_2 = "src/test/resources/genome/compare/testMotherMT.bam";

    /**
     * Path to the second test BAM file
     */
    private final static  String PATH_TO_SON_BAM_2 = "src/test/resources/genome/compare/testSonMT.bam";

    /**
     * Path to the second test BAM file
     */
    private final static  String PATH_TO_DAD_BAM_2 = "src/test/resources/genome/compare/testDadMT.bam";

    /**
     * Path to the second test BAM file
     */
    private final static  String PATH_TO_DAD_BAM_1 = "src/test/resources/genome/compare/testDad4.bam";

    /**
     * Path to the second test BAM file
     */
    private final static  String PATH_TO_DAD_BAM_3 = "src/test/resources/genome/compare/china/chinaFatherTest1000.bam";

    /**
     * Path to the first test BAM file
     */
    private final static  String PATH_TO_MOM_BAM_3 = "src/test/resources/genome/compare/testMother4.bam";

    /**
     * Path to the first test BAM file
     */
    private final static  String PATH_TO_SON_BAM_3 = "src/test/resources/genome/compare/china/chinaSonTest1000.bam";

    /**
     * Path to the first test BED file
     */
    private final static  String PATH_TO_BED = "src/test/resources/genome/compare/correct.bed";

    @Ignore
    @Test
    public void GenomeComparisonOfNotParentAndChild() throws Exception {
        long startTime = System.currentTimeMillis();
        GenomeComparator comparator = new GenomeComparator(PATH_TO_SON_BAM_1, PATH_TO_DAD_BAM_1, PATH_TO_BED);
        GeneComparisonResultAnalyzer result = comparator.compareGenomes(true);
        System.out.println(result);
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }

    @Ignore
    @Test
    public void GenomeComparisonOfNotParentAndChildThreads() throws Exception {
        long startTime = System.currentTimeMillis();
        GenomeComparatorThread comparator = new GenomeComparatorThread(PATH_TO_SON_BAM_1, PATH_TO_DAD_BAM_1, PATH_TO_BED);
        GeneComparisonResultAnalyzer result = comparator.compareGenomes(true);
        System.out.println(result);
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }
}