package com.test.sorted;


import com.alibaba.fastjson.JSONObject;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/12/3
 * @modifyDate: 10:44
 * @Description:
 */
public class HardSort {

    public static void main(String[] args) {
        int[] data = {5,8,3,1,7,6,9,11,10};
        HardSort hardSort = new HardSort();
        hardSort.shellSort(data);
        hardSort.shellSort1(data);

        hardSort.heapSort(data);

        hardSort.MergeSort(data);

    }


    /***********************************  希尔排序 *********************************/

    /**
     * 希尔排序 （交换法）
     * 例子: {1,4,2,7,9,8,3,6}  length = 8
     *  (1) gap = 4    (0,4 | 1,5 | 2,6 | 3,7)    [1,9] [4,8] [2,3] [7,6]  --->   [1,9] [4,8] [2,3] [6,7]
     *  (2) gap = 2    (2,0 | 3,1 | 4,2 | 5,3 | 6,4 | 7,5)   2,0  4,2 6,4 (1 2 4 6)  相当于 比较 {0,2,4,6}   同理 (3 7 8 9)  {1,3,5,7}
     *                     --->  (1 8 2 3 4 7 6 9)
     *  (3) gap = 1    (1,0| 2,1| 3,2 | ....) 相当于逐个 比较   获得最终结果  1 2 3 4 6 7 8 9
     * @param data
     */
    private void shellSort(int[] data) {
        //增量gap，并逐步缩小增量
        for (int gap = data.length/2;gap>0;gap/=2) {
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i <data.length;i++) {
                int j = i;
                while (j-gap>=0 && data[j]< data[j-gap]) {

                    data[j] = data[j] + data[j-gap];
                    data[j-gap] = data[j] - data[j-gap];
                    data[j] = data[j] - data[j-gap];

                    // 在比较一次 这个增量  比如 4,2(4跟2 比较的时候 调换了顺序 这时 比较 2,0  因为 这时 的 gap = 2)
                    j-=gap;
                }
            }
        }

        for (int i=0;i<data.length;i++) {
            System.out.print(data[i]+"  ");
        }
        System.out.println();
    }

    /**
     * 希尔排序 （移动法）
     * @param data
     */
    private void shellSort1(int[] data) {
        //增量gap，并逐步缩小增量
        for (int gap = data.length/2;gap>0;gap/=2) {
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i <data.length;i++) {
                int j = i;
                int temp = data[j];
                if(data[j]<data[j-gap]){
                    while(j-gap>=0 && temp<data[j-gap]){
                        //移动法
                        data[j] = data[j-gap];
                        j-=gap;
                    }
                    data[j] = temp;
                }
            }
        }

        for (int i=0;i<data.length;i++) {
            System.out.print(data[i]+"  ");
        }
        System.out.println();
    }

    /********************************* 堆排序 *********************************/


    /**
     * 堆 排序  {1,4,2,7,9,8,3,6}  第一个非叶子节点  data.length/2-1   index大于这个节点的都是 叶子节点
     * @param data
     */
    public void heapSort(int[] data) {
        //1.构建大顶堆
        for(int i=data.length/2-1;i>=0;i--){
            //从第一个非叶子结点从下至上，从右至左调整结构
            adjustHeap(data,i,data.length);
        }
        //2.调整堆结构+交换堆顶元素与末尾元素
        for(int j=data.length-1;j>0;j--){
            swap(data,0,j);//将堆顶元素与末尾元素进行交换
            adjustHeap(data,0,j);//重新对堆进行调整
        }

        for (int i=0;i<data.length;i++) {
            System.out.print(data[i]+"  ");
        }
        System.out.println();
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

    /**
     * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
     * @param data
     * @param i
     * @param length
     */
    private void adjustHeap(int[] data, int i, int length) {
        int temp = data[i];//先取出当前元素i
        for(int k=i*2+1;k<length;k=k*2+1){//从i结点的左子结点开始，也就是2i+1处开始  (k 每次递增 找的是当前k 的左子节点)
            if(k+1<length && data[k]<data[k+1]){//如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            if(data[k] >temp){//如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
                data[i] = data[k];
                i = k;
            }else{
                break;
            }
        }
        data[i] = temp;//将temp值放到最终的位置
    }

    /**********************************  归并排序  *********************************/

    /**  比如  4,5 1,3    45 13 都分别是经过一次排好序的
     *      然后在归并的时候  4 会和 1 比较  先放 1  j++   4 和 3比较  先放3 j++ 跳出第一个循环
     *      后边 循环
     *
     *       while(i<=mid){//将左边剩余元素填充进temp中
                temp[t++] = data[i++];
             }

            的时候  把 4 5 放进去
     *
     *     再考虑 4,5,1,6
     * 归并排序
     * @param data
     */
    public void MergeSort (int[] data){
        int []temp = new int[data.length];//在排序前，先建好一个长度等于原数组长度的临时数组，避免递归中频繁开辟空间
        MergeSort(data,0,data.length-1,temp);

        for (int i=0;i<data.length;i++) {
            System.out.print(data[i]+"  ");
        }
        System.out.println();
    }


    private void MergeSort(int[] data, int left, int right, int[] temp) {
        if(left<right){
            int mid = (left+right)/2;
            MergeSort(data,left,mid,temp);//左边归并排序，使得左子序列有序
            MergeSort(data,mid+1,right,temp);//右边归并排序，使得右子序列有序
            merge(data,left,mid,right,temp);//将两个有序子数组合并操作
        }
    }

    private void merge(int[] data, int left, int mid, int right, int[] temp) {
        int i = left;//左序列指针
        int j = mid+1;//右序列指针
        int t = 0;//临时数组指针
        while (i<=mid && j<=right){
            if(data[i]<=data[j]){
                temp[t++] = data[i++];
            }else {
                temp[t++] = data[j++];
            }
        }
        while(i<=mid){//将左边剩余元素填充进temp中
            temp[t++] = data[i++];
        }
        while(j<=right){//将右序列剩余元素填充进temp中
            temp[t++] = data[j++];
        }
        t = 0;
        //将temp中的元素全部拷贝到原数组中
        while(left <= right){
            data[left++] = temp[t++];
        }
    }

    /******************************  快速排序  **************************/

    /**
     * 快速排序
     * @param data
     */
    public void quickSort(int[] data,int left,int right) {
        if (left < right) {
            //获取枢纽值，并将其放在当前待处理序列末尾
            dealPivot(data, left, right);
            //枢纽值被放在序列末尾
            int pivot = right - 1;
            //左指针
            int i = left;
            //右指针
            int j = right - 1;
            while (true) {
                // 从左边找 大于 data[pivot] 的  找不到 就 i++
                while (data[++i] < data[pivot]) {
                }
                // 从右边找 没和左边相交 并且 小于 data[pivot] 的  找不到 就 j--
                while (j > left && data[--j] > data[pivot]) {
                }
                if (i < j) {
                    swap(data, i, j);
                } else {
                    // i >= j 时 即 相交了 交换data[pivot]  (即 交换data[right - 1]) 和 data[i]
                    break;
                }
            }
            if (i < right) {
                swap(data, i, right - 1);
            }
            quickSort(data, left, i - 1);
            quickSort(data, i + 1, right);
        }
    }

    /**
     * 处理枢纽值
     * @param data
     * @param left
     * @param right
     */
    private void dealPivot(int[] data, int left, int right) {
        int mid = (left + right) / 2;
        if (data[left] > data[mid]) {
            swap(data, left, mid);
        }
        if (data[left] > data[right]) {
            swap(data, left, right);
        }
        if (data[right] < data[mid]) {
            swap(data, right, mid);
        }
        swap(data, right - 1, mid);
    }

}
