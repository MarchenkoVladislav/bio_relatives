package genome.compare.analyzis;

import exception.GenomeException;
import genome.compare.comparator.GenomeRegionComparator;

/**
 * This class contains the results of the comparison of
 * two nucleotide sequences. Object of this class is a result
 * of comparison of two {@link genome.assembly.GenomeRegion}
 * made by {@link GenomeRegionComparator}.
 *
 * @author Sergey Khvatov
 * @author Vladislav Marchenko
 */
public class GeneComparisonResult {

    /**
     * Name of the chromosome
     * which contains this nucleotide sequence.
     */
    private String chrom;

    /**
     * The distance value that was calculated
     * by the {@link GenomeRegionComparator} methods.
     */
    private int difference;

    /**
     * Contains the length of the
     * longest nucleotide sequence
     * among the compared.
     */
    private int sequenceLen;

    /**
     * Contains the name of the gene,
     * that is located in the chromosome
     * in this region.
     */
    private String gene;

    /**
     * Default class constructor from name of the distance,
     * that was used to calculate the distance between two genomes,
     * the distance value and the length of the longest nucleotide sequence
     * between compared ones.
     *
     * @param chrom      Name of the chromosome.
     * @param difference Difference value.
     * @param len        Length of the nucl. seq.
     * @throws GenomeException if input values are lesser than 0 or the chromosome name is invalid.
     */
    public GeneComparisonResult(String chrom, String gene, int difference, int len) throws GenomeException {
        this.chrom = chrom;
        this.gene = gene;

        // check the diff value
        if (difference < 0) {
            throw new GenomeException(this.getClass().getName(), "GeneComparisonResult", "difference", "is lesser than 0");
        }
        this.difference = difference;

        // check the len value
        if (len < 0) {
            throw new GenomeException(this.getClass().getName(), "GeneComparisonResult", "len", "is lesser than 0");
        }
        sequenceLen = len;
    }

    /**
     * @return The difference distance between two genomes.
     */
    public int getDifference() {
        return difference;
    }

    /**
     * @return The length of the compared sequences.
     */
    public int getSequenceLen() {
        return sequenceLen;
    }

    /**
     * Get the name of the chromosome.
     *
     * @return Name of the chromosome.
     */
    public String getChromName() {
        return chrom;
    }

    /**
     * @return name of the gene that is stored in this region.
     */
    public String getGene() {
        return gene;
    }

    /**
     * Overridden method toString() which return String with single gene intermediate results
     *
     * @return String with single gene intermediate results
     */
    @Override
    public String toString() {
        if (sequenceLen != 0)
            return "Comparison result of gene " + gene + " from chromosome " + chrom + ": seqLength - " + sequenceLen + ", differences - " + difference + ", similarity percentage - " + (100d - ((double) difference / (double) sequenceLen) * 100d) + "%";
        return "Nucleotide sequence consists of only UNKNOWN_NUCLEOTIDES";
    }
}
