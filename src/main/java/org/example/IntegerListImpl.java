package org.example;

import org.example.exceptions.ElementNotFoundException;
import org.example.exceptions.NullIntegerException;
import org.example.exceptions.WrongIndexException;

import java.util.Arrays;
import java.util.Random;

public class IntegerListImpl implements IntegerList {

    private Integer[] intList;
    private int size;
    private Random random;


    public IntegerListImpl() {
        intList = new Integer[100_000];
        for (int i = 0; i < intList.length; i++) {
            random = new Random();
            intList[i] = random.nextInt(100000);
        }
    }

    public IntegerListImpl(int size) {
        random = new Random();
        intList = new Integer[size];
    }

    public void checkItem(Integer item) {
        if (item == null) {
            throw new NullIntegerException("Пустое значение");
        }
    }

    public void checkIndex(int index) {
        if (index < 0 || index >= intList.length) {
            throw new WrongIndexException("Неверный индекс");
        }
    }

//    public void checkSize() {
//        if (size == intList.length) {
//            throw new ArrayIsFullException("Массив переполнен");
//        }
//    } заменяем на:

    public void growIfNeeded() {
        if (size == intList.length) {
            grow();
        }
    }

    @Override
    public Integer add(Integer item) {
//        checkSize();
        growIfNeeded();
        checkItem(item);
        intList[size++] = item;
        return item;
    }

    @Override
    public Integer add(int index, Integer item) {
        checkIndex(index);
        checkItem(item);
//        checkSize();
        growIfNeeded();
        if (index == size) {
            intList[size++] = item;
            return item;
        }
        System.arraycopy(intList, index, intList, index + 1, size - index);
        intList[index] = item;
        size++;
        return item;
    }

    @Override
    public Integer set(int index, Integer item) {
        checkIndex(index);
        checkItem(item);
        intList[index] = item;
        return item;
    }

    @Override
    public Integer remove(Integer item) {
        checkItem(item);
        int index = indexOf(item);
        if (index == -1) {
            throw new ElementNotFoundException("Элемент массива не найден");
        }
        if (size != index) {
            System.arraycopy(intList, index + 1, intList, index, size - index);
        }
        size--;
        return item;
    }

    @Override
    public Integer remove(int index) {
        checkIndex(index);
        Integer item = intList[index];
        if (index != size) {
            System.arraycopy(intList, index + 1, intList, index, size - index);
        }
        size--;
        return item;
    }

    @Override
    public boolean contains(Integer item) {
        Integer[] intListCopy = Arrays.copyOf(intList, 100_000);
        sortSelection(intListCopy);
        return binarySearch(intListCopy, item);
    }

    @Override
    public int indexOf(Integer item) {
        for (int i = 0; i < intList.length; i++) {
            if (intList[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Integer item) {
        for (int i = size - 1; i > 0; i--) {
            if (intList[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Integer get(int index) {
        checkIndex(index);
        return intList[index];
    }

    @Override
    public boolean equals(IntegerList otherList) {
        return Arrays.equals(this.toArray(), otherList.toArray());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public Integer[] toArray() {
        return Arrays.copyOf(intList, size);
    }

    //

    public void comparisonOfSorts() {
        long start1 = System.currentTimeMillis();
        sortBubble(Arrays.copyOf(intList, 100_000));
        System.out.println("Пузырьковая: " + (System.currentTimeMillis() - start1));
        long start2 = System.currentTimeMillis();
        sortSelection(Arrays.copyOf(intList, 100_000));
        System.out.println("Сортировка выбором: " + (System.currentTimeMillis() - start2));
        long start3 = System.currentTimeMillis();
        sortInsertion(Arrays.copyOf(intList, 100_000));
        System.out.println("Сортировка вставкой: " + (System.currentTimeMillis() - start3));
    }

    private static void swapElements(Integer[] arr, int indexA, int indexB) {
        int tmp = arr[indexA];
        arr[indexA] = arr[indexB];
        arr[indexB] = tmp;
    }

    public static void sort(Integer[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    public static void sortBubble(Integer[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swapElements(arr, j, j + 1);
                }
            }
        }
    }

    public static void sortSelection(Integer[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minElementIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minElementIndex]) {
                    minElementIndex = j;
                }
            }
            swapElements(arr, i, minElementIndex);
        }
    }

    public static void sortInsertion(Integer[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            int j = i;
            while (j > 0 && arr[j - 1] >= temp) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = temp;
        }
    }

    public static void quickSort(Integer[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private static int partition(Integer[] arr, int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                swapElements(arr, i, j);
            }
        }

        swapElements(arr, i + 1, end);
        return i + 1;
    }

    public static boolean binarySearch(Integer[] arr, int item) {
        int min = 0;
        int max = arr.length - 1;

        while (min <= max) {
            int mid = (min + max) / 2;

            if (item == arr[mid]) {
                return true;
            }

            if (item < arr[mid]) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        return false;
    }

    private void grow() {
        intList = Arrays.copyOf(intList, size + size / 2);
    }
}
