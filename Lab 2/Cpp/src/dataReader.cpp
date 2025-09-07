#include "dataReader.h"
#include <fstream>
#include <sstream>
#include <stdexcept>

std::vector<std::vector<int>> readAllIntLines(const std::string& path) {
    std::vector<std::vector<int>> res;
    std::ifstream ifs(path);
    if (!ifs) throw std::runtime_error("cannot open file");
    std::string line;
    while (std::getline(ifs, line)) {
        std::istringstream iss(line);
        std::vector<int> v;
        int x;
        while (iss >> x) v.push_back(x);
        if (!v.empty()) res.push_back(std::move(v));
    }
    return res;
}