package genome.compare.comparator.threads;

import bam.BAMParser;
import bam.BEDFeature;
import exception.GenomeException;
import exception.GenomeThreadException;
import genome.assembly.GenomeRegion;
import genome.compare.analyzis.GeneComparisonResultAnalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Khvatov
 */
public class FeatureThread implements Runnable {

    /**
     * Corresponding BED file feature.
     */
    private final BEDFeature feature;

    /**
     * First person's BAM file parser.
     */
    private final BAMParser firstBAMFile;

    /**
     * Second person's BAM file parser.
     */
    private final BAMParser secondBAMFile;

    /**
     * Object, where the results of the comparison
     * will be stored in random order.
     */
    private final GeneComparisonResultAnalyzer comparisonResults;

    /**
     * if this flag is true , then interim genome comparison results will be displayed,
     * else - only the main chromosome results will be obtained
     */
    private final boolean intermediateOutput;

    /**
     * Creates a feature thread using the following arguments.
     *
     * @param feature              Corresponding BED file feature.
     * @param firstParser          First person's BAM file parser.
     * @param secondParser         Second person's BAM file parser.
     * @param results              Object, where the results of the comparison
     *                             will be stored in random order.
     * @param intermediateOutput   if this flag is true , then interim genome comparison results will be displayed,
     *                             else - only the main chromosome results will be obtained
     */
    public FeatureThread(BEDFeature feature, BAMParser firstParser, BAMParser secondParser, GeneComparisonResultAnalyzer results, boolean intermediateOutput ) {
        this.feature = feature;
        this.firstBAMFile = firstParser;
        this.secondBAMFile = secondParser;
        this.comparisonResults = results;
        this.intermediateOutput = intermediateOutput;
    }

    /**
     * run() method of the interface {@link Runnable} implementation.
     * Processes each feature. First creates two threads that will
     * parse this feature from the sam file and assembly it into list of
     * {@link genome.assembly.GenomeRegion} objects. After that, for each
     * genome region starts {@link GenomeRegionThread}, that will
     * compare these regions and add the results to the result table.
     */
    @Override
    public void run() {
        try {
            /*
             * start AssemblyThread that will assembly the genome
             * of the first person according to this BED file feature
             * and make this thread to wait, until the genome construction is finished.
             */
            List<GenomeRegion> firstGenome = Collections.synchronizedList(new ArrayList<>());
            Thread firstPersonAssemblyThread = new Thread(new GenomeAssemblyThread(firstBAMFile, feature, firstGenome));
            firstPersonAssemblyThread.start();

            /*
             * start AssemblyThread that will assembly the genome
             * of the second person according to this BED file feature
             * and make this thread to wait, until the genome construction is finished.
             */
            List<GenomeRegion> secondGenome = Collections.synchronizedList(new ArrayList<>());
            Thread secondPersonAssemblyThread = new Thread(new GenomeAssemblyThread(secondBAMFile, feature, secondGenome));
            secondPersonAssemblyThread.start();

            // join the thread and make this thread to wait until they are finished
            for (Thread t : new Thread[]{firstPersonAssemblyThread, secondPersonAssemblyThread}) {
                t.join();
            }

            // check the results
            if (firstGenome.size() != secondGenome.size()) {
                //throw new GenomeException(this.getClass().getName(), "compareGenomes", "sizes of regions are different");
                return;
            }

            // here we consider that both genomes are assembled and are the same size

            // for all regions create GenomeRegionThreads
            for (int i = 0; i < firstGenome.size(); i++) {
                Thread compareThread = new Thread(new GenomeRegionThread(firstGenome.get(i), secondGenome.get(i), comparisonResults, intermediateOutput));
                compareThread.start();
                compareThread.join();
            }

           /* synchronized (comparisonResults) {
                if (comparisonResults.containsKey(feature.getGene())) {
                    comparisonResults.get(feature.getGene()).addAll(tempResult);
                } else {
                    comparisonResults.put(feature.getGene(), tempResult);
                }
            } */
        } catch (InterruptedException | GenomeException iex) {
            throw new GenomeThreadException(iex.getMessage());
        }
    }
}