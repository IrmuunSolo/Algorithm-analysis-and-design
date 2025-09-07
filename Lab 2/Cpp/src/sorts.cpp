#include "sorts.h"
#include <stdexcept>

namespace alg
{

    void insertionSort(std::vector<int> &a)
    {
        for (size_t i = 1; i < a.size(); ++i)
        {
            int key = a[i];
            int j = (int)i - 1;
            while (j >= 0 && a[j] > key)
            {
                a[j + 1] = a[j];
                --j;
            }
            a[j + 1] = key;
        }
    }

    static void merge(std::vector<int> &a, int l, int m, int r)
    {
        int n1 = m - l + 1;
        int n2 = r - m;
        std::vector<int> L(n1), R(n2);
        for (int i = 0; i < n1; ++i)
            L[i] = a[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = a[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2)
        {
            if (L[i] <= R[j])
                a[k++] = L[i++];
            else
                a[k++] = R[j++];
        }
        while (i < n1)
            a[k++] = L[i++];
        while (j < n2)
            a[k++] = R[j++];
    }

    static void mergeSortRec(std::vector<int> &a, int l, int r)
    {
        if (l >= r)
            return;
        int m = l + (r - l) / 2;
        mergeSortRec(a, l, m);
        mergeSortRec(a, m + 1, r);
        merge(a, l, m, r);
    }

    void mergeSort(std::vector<int> &a)
    {
        if (a.size() <= 1)
            return;
        mergeSortRec(a, 0, (int)a.size() - 1);
    }

    static int maxRec(const std::vector<int> &a, int l, int r)
    {
        if (l == r)
            return a[l];
        int m = l + (r - l) / 2;
        int leftMax = maxRec(a, l, m);
        int rightMax = maxRec(a, m + 1, r);
        return leftMax > rightMax ? leftMax : rightMax;
    }

    int maxDivideConquer(const std::vector<int> &a)
    {
        if (a.empty())
            throw std::invalid_argument("empty array");
        return maxRec(a, 0, (int)a.size() - 1);
    }

}
