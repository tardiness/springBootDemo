package com.test.sorted;


/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/12/3
 * @modifyDate: 9:52
 * @Description:
 */
public class SimpleSort {

    public static void main(String[] args) {
        int[] data = {5,8,3,1,7,6,9,11,10};
        SimpleSort simpleSort = new SimpleSort();
        simpleSort.bobSort(data);
        simpleSort.chooseSort(data);
        simpleSort.doInsertSort(data);
    }


    /**
     * 冒泡排序
     * @param data
     */
    private void bobSort(int[] data){
        boolean flag = true;
        for(int i=0;i<data.length-1 && flag;i++){//排序轮数
            flag = false;
            for(int j=0;j<data.length-1-i;j++){//比较次数
                if(data[j]>data[j+1]){
                    int temp = data[j+1];
                    data[j+1] = data[j];
                    data[j] = temp;
                    flag = true;
                }
            }
        }


        for (int i=0;i<data.length;i++) {
            System.out.print(data[i] + "  ");
        }
        System.out.println();
    }

    /**
     * 选择排序
     * @param data
     */
    private void chooseSort(int[] data) {
        for(int i=0; i<data.length-1; i++){
            int minIndex = i;
            for(int j=minIndex+1;j<data.length;j++){
                if(data[j]<data[minIndex]){
                    minIndex = j;
                }
            }
            int temp = data[i];
            data[i] = data[minIndex];
            data[minIndex] = temp;
        }
        for (int i=0;i<data.length;i++) {
            System.out.print(data[i] + "  ");
        }
        System.out.println();
    }

    /**
     * 插入排序
     * （2）例子：
     　　待比较数据：7, 6, 9, 8, 5,1

     　　第一轮：指针指向第二个元素6，假设6左面的元素为有序的，将6抽离出来，形成7,_,9,8,5,1，从7开始，6和7比较，发现7>6。将7右移，形成_,7,9,8,5,1，6插入到7前面的空位，结果：6,7,9,8,5,1

     　　第二轮：指针指向第三个元素9，此时其左面的元素6,7为有序的，将9抽离出来，形成6,7,_,8,5,1，从7开始，依次与9比较，发现9左侧的元素都比9小，于是无需移动，把9放到空位中，结果仍为：6,7,9,8,5,1

     　　第三轮：指针指向第四个元素8，此时其左面的元素6,7,9为有序的，将8抽离出来，形成6,7,9,_,5,1，从9开始，依次与8比较，发现8<9，将9向后移，形成6,7,_,9,5,1，8插入到空位中，结果为：6,7,8,9,5,1

     　　第四轮：指针指向第五个元素5，此时其左面的元素6,7,8,9为有序的，将5抽离出来，形成6,7,8,9,_,1，从9开始依次与5比较，发现5比其左侧所有元素都小，5左侧元素全部向右移动，形成_,6,7,8,9,1，将5放入空位，结果5,6,7,8,9,1。

     　　第五轮：同上，1被移到最左面，最后结果：1,5,6,7,8,9。
     * @param data
     */
    private void doInsertSort(int[] data) {
        for(int index = 1; index<data.length; index++){//外层向右的index，即作为比较对象的数据的index
            int temp = data[index];//用作比较的数据
            int leftIndex = index-1;
            while(leftIndex>=0 && data[leftIndex]>temp){//当比到最左边或者遇到比temp小的数据时，结束循环
                data[leftIndex+1] = data[leftIndex];
                leftIndex--;
            }
            data[leftIndex+1] = temp;//把temp放到空位上
        }

        for (int i=0;i<data.length;i++) {
            System.out.print(data[i] + "  ");
        }
        System.out.println();
    }
}
