package com.example.aabb;

import java.util.Arrays;

/** Dynamic-length array **/
public class DynamicArray {

    public int[] array;

    public DynamicArray(int initialSize){
        array = new int[initialSize];
    }

    public boolean overflow(int index){
        return index >= array.length;
    }

    public void resize(){
        array = Arrays.copyOf(array, array.length * 2);
    }
}
