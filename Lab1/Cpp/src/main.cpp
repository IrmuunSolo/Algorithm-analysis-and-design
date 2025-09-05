// // int main агуулсан .cpp Файлын байгаа хавтасруу зааж өгсний дараа Cntrl + Alt + N дарж ажиллуулна.

#include <iostream>
#include "Reader.h"

int main()
{
    std::cout << "Enter your file name: ";
    std::string fileName;
    std::getline(std::cin, fileName);

    bool empty;
    try
    {
        std::string content = Reader::readFile(fileName, empty);
        if (!empty)
        {
            std::cout << "File content:\n\n"
                      << content;
        }
        else
        {
            std::cerr << "File is empty!" << std::endl;
        }
    }
    catch (const std::runtime_error &e)
    {
        std::cerr << e.what() << std::endl;
    }

    return 0;
}