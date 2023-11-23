package info.kgeorgiy.ja.mironov.arrayset;

import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {

    public List<T> elements;
    public Comparator<? super T> comparator;

    public ArraySet() {
        this.elements = List.of();
        this.comparator = null;
    }

    public ArraySet(Collection<? extends T> collection) {
        this.elements = List.copyOf(new TreeSet<>(collection));
        this.comparator = null;
    }

    public ArraySet(Comparator<? super T> comparator) {
        this.elements = List.of();
        this.comparator = comparator;
    }

    public ArraySet(Collection<? extends T> elements, Comparator<? super T> comparator) {
        Set<T> set = new TreeSet<>(comparator);
        set.addAll(elements);
        this.elements = List.copyOf(set);
        this.comparator = comparator;
    }

    private ArraySet(List<T> sortedElements, Comparator<? super T> comparator) {
        this.elements = sortedElements;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(elements, (T) Objects.requireNonNull(o), comparator) >= 0;
    }

    @Override
    public T lower(T t) {
        return elementAt(getLower(t, false));
    }

    @Override
    public T floor(T t) {
        return elementAt(getLower(t, true));
    }

    @Override
    public T ceiling(T t) {
        return elementAt(getHigher(t, true));
    }

    @Override
    public T higher(T t) {
        return elementAt(getHigher(t, false));
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new ArraySet<>(new DescendingList<>(elements), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    private NavigableSet<T> subSetImpl(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        int fromIndex = getHigher(fromElement, fromInclusive);
        int toIndex = getLower(toElement, toInclusive);
        return new ArraySet<>((fromIndex > toIndex ? Collections.emptyList() : elements.subList(fromIndex, toIndex + 1)), comparator);
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("the left border is larger than the right one");
        }
        return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        return elements.isEmpty() ? this : subSetImpl(first(), true, toElement, inclusive);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        return elements.isEmpty() ? this : subSetImpl(fromElement, inclusive, last(), true);
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public T first() {
        return getFirstOrLastElement(true);
    }

    @Override
    public T last() {
        return getFirstOrLastElement(false);
    }

    @Override
    public int size() {
        return elements.size();
    }

    private T getFirstOrLastElement(boolean first) {
        if (elements.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            return (first ? elements.get(0) : elements.get(size() - 1));
        }
    }

    private T elementAt(int index) {
        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        } else {
            return null;
        }
    }

    private int getIndex(T elem) {
        return Collections.binarySearch(elements, elem, comparator);
    }

    private int getLower(T elem, boolean inclusive) {
        int index = getIndex(elem);
        if (index < 0) {
            index = -index - 2;
        } else if (!inclusive) {
            index--;
        }
        return index;
    }

    private int getHigher(T elem, boolean inclusive) {
        int index = getIndex(elem);
        if (index < 0) {
            index = -index - 1;
        } else if (!inclusive) {
            index++;
        }
        return index;
    }

    private static class DescendingList<T> extends AbstractList<T> {

        private final List<T> elements;
        private final boolean reversed;

        DescendingList(List<T> elements) {
            if (elements.getClass() == DescendingList.class) {
                DescendingList<T> elem = (DescendingList<T>) elements;
                this.elements = elem.elements;
                this.reversed = !elem.reversed;
            } else {
                this.elements = elements;
                this.reversed = true;
            }
        }

        @Override
        public T get(int index) {
            return reversed ? elements.get(size() - index - 1) : elements.get(index);
        }

        @Override
        public int size() {
            return elements.size();
        }
    }
}