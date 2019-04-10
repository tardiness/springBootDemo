package com.test.sorted;

import java.util.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2019/4/8
 * @modifyDate: 15:31
 * @Description:
 */
public class HardSortNew {

    public void shellSort(int[] arr) {
        int len = arr.length;
        for (int gap=len/2;gap>0;gap=gap/2) {
            for (int i=gap;i<len;i++){
                int j = i;
                int cur = arr[i];
                while (j-gap>=0 && arr[j-gap]>cur){
                    arr[j] = arr[j-gap];
                    j = j-gap;
                }
                arr[j] = cur;
            }
        }
    }

    public int find(int[] arr,int a) {
        int l =0;
        int h = arr.length - 1;
        while (l<h) {
            int m = l + (h - 1)/2;
            if (arr[m] == a) {
                return m;
            } else if (arr[m] > a) {
                h = m - 1;
            } else {
                l = m + 1;
            }
        }
        return -1;
    }


    public void push() {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);
        int size = queue.size();
        while (size-- > 1) {
            queue.add(queue.poll());
        }

    }

    static class LruCache<K,V> extends LinkedHashMap<K,V> {

        private static final Integer MAX_SIZE = 3;

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > MAX_SIZE;
        }

        LruCache () {
            super(MAX_SIZE,0.75f,true);
        }

    }


    /**
     * 交换元素
     * @param data
     * @param i
     * @param j
     */
    private void swap(int[] data, int i, int j) {
        int temp=data[i];
        data[i] = data[j];
        data[j] = temp;
    }


    public void quickSort(int[] arr,int left,int right) {
        if (left < right) {
            int p = dealPivot(arr,left,right);
            int i = left;
            int j = p;
            while (true) {
                while (arr[++i] < arr[p]) {}
                while (j>i && arr[--j] > arr[p]) {}
                if (i < j) {
                    swap(arr,i,j);
                } else {
                    break;
                }
            }
            if (i < right) {
                swap(arr,i,right-1);
            }
            quickSort(arr,left,p-1);
            quickSort(arr,p+1,right);
        }
    }

    public void dui(int[] arr) {
        for (int i=arr.length/2-1;i>0;i--) {
            adjustHeap(arr,i,arr.length);
        }
        for (int j=arr.length-1;j>0;j--) {
            swap(arr,0,j);
            adjustHeap(arr,0,j);
        }
    }

    public void adjustHeap(int[] arr,int i,int length) {
        int temp = arr[i];
        for (int k=i*2+1;k<length;k=k*2+1) {
            if (k+1<length && arr[k]<arr[k+1]) {
                k++;
            }
            if (arr[k] > temp) {
                arr[i] = arr[k];
                i = k;
            } else {
                break;
            }
        }
        arr[i] = temp;
    }


    public void guibing(int[] arr) {
        int[] temp = new int[arr.length];
        mergeSort(arr,0,arr.length-1,temp);
    }

    public void mergeSort(int[] arr,int left,int right,int[] temp) {
        if (left <right) {
            int mid = (left+right)/2;
            mergeSort(arr, left, mid, temp);
            mergeSort(arr, mid+1, right, temp);
            merge(arr,left,mid,right,temp);
        }
    }

    public void merge(int[] arr,int left,int mid,int right,int[] temp){
        int i=0;
        int j = mid+1;
        int t=0;
        while (i<=mid && j <=right) {
            if (arr[i] <= arr[j]) {
                temp[t++] = arr[i++];
            } else {
                temp[t++] = arr[j++];
            }
        }
        while (i<=mid) {
            temp[t++] = arr[i++];
        }
        while (j<=right) {
            temp[t++] = arr[j++];
        }
        t=0;
        while (left<=right) {
            arr[left] = temp[t++];
        }
    }


    /**
     * 处理枢纽值
     * @param arr
     * @param left
     * @param right
     * @return
     */
    private int dealPivot(int[] arr, int left, int right) {
        int mid = (left+right)/2;
        if (arr[left]>arr[mid]) {
            swap(arr,left,mid);
        }
        if (arr[right] < arr[mid]) {
            swap(arr,mid,right);
        }
        if (arr[left]>arr[right]) {
            swap(arr,left,right);
        }
        //放到倒数第二位
        swap(arr,right-1,mid);
        return right-1;
    }


}
