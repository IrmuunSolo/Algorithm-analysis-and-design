#include <catch2/catch_test_macros.hpp>
#include "../src/sorts.h"
#include "../src/search.h"
#include "../src/dataReader.h"
#include <vector>

static std::vector<std::vector<int>> datasets;

struct Loader
{
    Loader()
    {
        datasets = readAllIntLines("testdata.txt");
    }
};
static Loader loader; // Тест эхлэхээс өмнө өгөгдөл ачаална

TEST_CASE("Insertion sort from file")
{
    REQUIRE(datasets.size() >= 1);
    auto a = datasets[0];
    alg::insertionSort(a);
    std::vector<int> expected = {26, 31, 41, 41, 58, 59};
    REQUIRE(a == expected);
}

TEST_CASE("Merge sort from file")
{
    REQUIRE(datasets.size() >= 2);
    auto a = datasets[1];
    alg::mergeSort(a);
    std::vector<int> expected = {1, 2, 5, 5, 6, 9};
    REQUIRE(a == expected);
}

TEST_CASE("Max divide & conquer from file")
{
    REQUIRE(datasets.size() >= 3);
    auto a = datasets[2];
    int maxVal = alg::maxDivideConquer(a);
    REQUIRE(maxVal == 100);
}

TEST_CASE("Binary search recursive from file (merge sort)")
{
    REQUIRE(datasets.size() >= 4);
    auto a = datasets[3];
    alg::mergeSort(a);
    int idx = alg::binarySearchRecursive(a, 15);
    REQUIRE(idx != -1);
    REQUIRE(a[idx] == 15);

    REQUIRE(alg::binarySearchRecursive(a, 100) == -1);
}
