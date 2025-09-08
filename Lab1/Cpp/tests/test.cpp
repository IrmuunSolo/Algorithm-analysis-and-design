#include <catch2/catch_test_macros.hpp>
#include "Reader.h"
#include <fstream>
#include <filesystem>
#include <string>

#define CATCH_CONFIG_MAIN

TEST_CASE("Reader::readFile normal file", "[Reader]")
{
    std::string fileName = "test_normal.txt";
    std::ofstream ofs(fileName);
    ofs << "Hello World\nC++ Programming";
    ofs.close();

    std::string content = Reader::readFile(fileName);

    REQUIRE(content.find("Hello World") != std::string::npos);
    REQUIRE(content.find("C++ Programming") != std::string::npos);
    REQUIRE(content.find("file is empty") == std::string::npos);

    std::filesystem::remove(fileName);
}

TEST_CASE("Reader::readFile empty file", "[Reader]")
{
    std::string fileName = "test_empty.txt";
    std::ofstream ofs(fileName);
    ofs.close();

    std::string content = Reader::readFile(fileName);

    REQUIRE(content == "file is empty");

    std::filesystem::remove(fileName);
}

TEST_CASE("Reader::readFile file with spaces only", "[Reader]")
{
    std::string fileName = "test_spaces.txt";
    std::ofstream ofs(fileName);
    ofs << "   \n\t\n    ";
    ofs.close();

    std::string content = Reader::readFile(fileName);

    REQUIRE(content == "file is empty");

    std::filesystem::remove(fileName);
}

TEST_CASE("Reader::readFile non-existent file", "[Reader]")
{
    REQUIRE_THROWS_AS(Reader::readFile("nonexistent_file.txt"), std::runtime_error);
}