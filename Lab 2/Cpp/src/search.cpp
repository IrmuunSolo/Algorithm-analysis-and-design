#include "search.h"

namespace alg
{

    static int bsRec(const std::vector<int> &a, int l, int r, int key)
    {
        if (l > r)
            return -1;
        int m = l + (r - l) / 2;
        if (a[m] == key)
            return m;
        else if (a[m] > key)
            return bsRec(a, l, m - 1, key);
        else
            return bsRec(a, m + 1, r, key);
    }

    int binarySearchRecursive(const std::vector<int> &a, int key)
    {
        if (a.empty())
            return -1;
        return bsRec(a, 0, (int)a.size() - 1, key);
    }

}
