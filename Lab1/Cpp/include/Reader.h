#pragma once
#include <string>
#include <stdexcept>
#include <fstream>

// Header файл (класс, функцийн prototype)

class Reader
{
public:
    // Файл уншиж, агуулгыг буцаана
    // Хоосон файл бол empty=true болгож буцаана
    static std::string readFile(const std::string &fileName, bool &empty)
    {
        empty = true;
        std::string content;
        std::ifstream file(fileName);
        if (!file.is_open())
        {
            throw std::runtime_error("Error to read file: " + fileName);
        }

        std::string line;
        while (std::getline(file, line))
        {
            if (!line.empty() && line.find_first_not_of(" \t\n\r") != std::string::npos)
            {
                empty = false;
            }
            content += line + "\n";
        }

        return content;
    }
};