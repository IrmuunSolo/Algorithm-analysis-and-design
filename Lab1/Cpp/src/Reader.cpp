#include "Reader.h"

std::string Reader::readFile(const std::string &fileName)
{
    std::ifstream file(fileName);
    if (!file.is_open())
    {
        throw std::runtime_error("Error to read file: " + fileName);
    }

    std::string content;
    std::string line;
    bool empty = true;

    while (std::getline(file, line))
    {
        if (!line.empty() && line.find_first_not_of(" \t\n\r") != std::string::npos)
        {
            empty = false;
        }
        content += line + "\n";
    }

    if (empty)
    {
        return "file is empty";
    }

    return content;
}