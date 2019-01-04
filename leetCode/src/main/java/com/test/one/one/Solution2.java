package com.test.one.one;

class Solution2 {

    public static void main(String[] args) {
        int[] A = new int[]{1,2};
        int[] B = new int[]{3,4};

    }

    /*  其中 left_part = A.left_part + B.left_part   right_part = A.right_part + B.right_part
        1.len(left_part)=len(right_part)
        2.max(left_part)≤min(right_part)

        推导出
        1.i+j=m−i+n−j（或： m−i+n−j+1）
        2.B[j−1]≤A[i] 以及 A[i−1]≤B[j]

         */

    public double findMedianSortedArrays(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        if (m > n) { // to ensure m<=n
            int[] temp = A;
            A = B;
            B = temp;
            int tmp = m;
            m = n;
            n = tmp;
        }
        int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;
            if (i < iMax && B[j - 1] > A[i]) {
                // i 太小 所以必须从 i+1 到 iMax 之间找
                iMin = i + 1; // i is too small
            } else if (i > iMin && A[i - 1] > B[j]) {
                iMax = i - 1; // i is too big
            } else { // i is perfect
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    //奇数 直接返回
                    return maxLeft;
                }

                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }

                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }
}