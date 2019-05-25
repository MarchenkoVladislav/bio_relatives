package util;

import htsjdk.samtools.SAMRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom collection for storage SAMRecords sorted by the start position
 *
 * @author Vladislav Marchenko
 */
public class LinkedSAMRecordList extends ArrayList<SAMRecord> {

    /**
     * Overridden method for adding a SAMRecord into sorted ArrayList by using binary search
     *
     * @param s SAMRecord, which we need to add
     * @return true if all is OK
     */
    @Override
    public boolean add(SAMRecord s) {
        // if this SAMRecord is the first then simply add it
        if (super.isEmpty()) {
            super.add(s);
            return true;
        }
        // if collection is not empty , then add SAMRecord by using standard binary search algorithm
        else {
            int firstIndex = 0;
            int lastIndex = super.size() - 1;
            while (firstIndex <= lastIndex) {
                int middleIndex = (firstIndex + lastIndex) / 2;
                if (SAMRecordComparator(s, super.get(middleIndex)) == 0) {
                    if (super.get(middleIndex).equals(s)) {
                        return false;
                    }
                    super.add(middleIndex, s);
                    return true;
                } else if (lastIndex == firstIndex) {
                    if (SAMRecordComparator(s, super.get(middleIndex)) > 0 && lastIndex == super.size() - 1) {
                        super.add(s);
                        return true;
                    } else if (SAMRecordComparator(s, super.get(middleIndex)) > 0) {
                        super.add(middleIndex + 1, s);
                        return true;
                    } else {
                        super.add(middleIndex, s);
                        return true;
                    }
                } else if (s.getStart() > super.get(middleIndex).getStart() || s.getEnd() > super.get(middleIndex).getEnd()) {
                    firstIndex = middleIndex + 1;
                } else if (s.getStart() < super.get(middleIndex).getStart() || s.getEnd() < super.get(middleIndex).getEnd()) {
                    lastIndex = middleIndex - 1;
                }
            }

        }
        return false;
    }

    /**
     * Adds all the elements from the other collection.
     *
     * @param collection Another collection to get the elements from.
     * @return True, if all elements were added, false otherwise.
     */
    @Override
    public boolean addAll(Collection<? extends SAMRecord> collection) {
        for (SAMRecord record : collection) {
            if (!add(record)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method, which returns a List of SAMRecords which contains the position
     *
     * @param position position, which SAMRecords should contain
     * @return List of SAMRecords which contains the position
     */
    public List<SAMRecord> get(long position) {
        List<SAMRecord> samRecords = new ArrayList<>();
        // minIndex - minimal index of SAMRecord in the super ArrayList which contain the position
        int minIndex = 0;
        int firstIndex = 0;
        int lastIndex = super.size() - 1;
        // find it by using binary search
        while (firstIndex <= lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            if (position < super.get(middleIndex).getStart()) {
                lastIndex = middleIndex - 1;
            } else if (position > super.get(middleIndex).getEnd()) {
                firstIndex = middleIndex + 1;
            } else if (inRange(position, super.get(middleIndex))) {
                if (middleIndex > 0 && inRange(position, super.get(middleIndex - 1))) {
                    lastIndex = middleIndex - 1;
                } else if ((middleIndex > 0 && !inRange(position, super.get(middleIndex - 1))) || (middleIndex == 0)) {
                    minIndex = middleIndex;
                    break;
                }
            }
        }
        // maxIndex - maximal index of SAMRecord in the super ArrayList which contain the position
        int maxIndex = super.size() - 1;
        firstIndex = 0;
        lastIndex = super.size() - 1;
        // find it by using binary search
        while (firstIndex <= lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            if (position < super.get(middleIndex).getStart()) {
                lastIndex = middleIndex - 1;
            } else if (position > super.get(middleIndex).getEnd()) {
                firstIndex = middleIndex + 1;
            } else if (inRange(position, super.get(middleIndex))) {
                if (middleIndex < super.size() - 1 && inRange(position, super.get(middleIndex + 1))) {
                    firstIndex = middleIndex + 1;
                } else if ((middleIndex < super.size() - 1 && !inRange(position, super.get(middleIndex + 1))) || (middleIndex == super.size() - 1)) {
                    maxIndex = middleIndex;
                    break;
                }
            }
        }
        for (int i = minIndex; i <= maxIndex; i++) {
            samRecords.add(super.get(i));
        }
        return samRecords;
    }

    /**
     * method which check that position there is in SAMRecord
     *
     * @param position  position, which we want to check
     * @param samRecord SAMRecord in which we check position
     * @return true if the position is in the SAMRecord
     */
    private static boolean inRange(long position, SAMRecord samRecord) {
        return position >= samRecord.getStart() && position <= samRecord.getEnd();
    }

    /**
     * comparator which compare positions of 2 SAMRecords
     *
     * @param fst the first SAMRecord
     * @param scd the second SAMRecord
     * @return sab of end positions if SAMRecords have similar start positions else return sab of start positions
     */
    private static int SAMRecordComparator(SAMRecord fst, SAMRecord scd) {
        if ((fst.getStart() - scd.getStart()) == 0) {
            return fst.getEnd() - scd.getEnd();
        } else {
            return fst.getStart() - scd.getStart();
        }
    }
}