#include <catch2/catch_test_macros.hpp>

#include "Reader.h"
#define CATCH_CONFIG_MAIN
#include <fstream>
#include <filesystem>
#include <string>

TEST_CASE("Reader::readFile normal file", "[Reader]")
{
    std::string fileName = "test_normal.txt";
    std::ofstream ofs(fileName);
    ofs << "Hello World\nC++ Programming";
    ofs.close();

    bool empty;
    std::string content = Reader::readFile(fileName, empty);

    REQUIRE_FALSE(empty);
    REQUIRE(content.find("Hello World") != std::string::npos);
    REQUIRE(content.find("C++ Programming") != std::string::npos);

    std::filesystem::remove(fileName);
}

TEST_CASE("Reader::readFile empty file", "[Reader]")
{
    std::string fileName = "test_empty.txt";
    std::ofstream ofs(fileName);
    ofs.close();

    bool empty;
    std::string content = Reader::readFile(fileName, empty);

    REQUIRE(empty);

    std::filesystem::remove(fileName);
}

TEST_CASE("Reader::readFile file with spaces only", "[Reader]")
{
    std::string fileName = "test_spaces.txt";
    std::ofstream ofs(fileName);
    ofs << "   \n\t\n    ";
    ofs.close();

    bool empty;
    std::string content = Reader::readFile(fileName, empty);

    REQUIRE(empty);

    std::filesystem::remove(fileName);
}

TEST_CASE("Reader::readFile non-existent file", "[Reader]")
{
    bool empty;
    REQUIRE_THROWS_AS(Reader::readFile("nonexistent_file.txt", empty), std::runtime_error);
}