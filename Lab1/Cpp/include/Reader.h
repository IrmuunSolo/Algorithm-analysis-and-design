#pragma once
#include <string>
#include <stdexcept>
#include <fstream>

class Reader
{
public:
    static std::string readFile(const std::string &fileName);
};