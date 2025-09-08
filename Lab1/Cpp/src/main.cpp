#include <iostream>
#include "Reader.h"

int main()
{
    std::cout << "Enter your file name: ";
    std::string fileName;
    std::getline(std::cin, fileName);

    try
    {
        std::string content = Reader::readFile(fileName);
        std::cout << "File content:\n\n" << content;
    }
    catch (const std::runtime_error &e)
    {
        std::cerr << e.what() << std::endl;
    }

    return 0;
}