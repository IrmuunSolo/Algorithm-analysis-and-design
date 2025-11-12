import unittest
from text_justify import greedy_justify, dp_justify


def badness(lines, width):
    def line_cost(s):
        used = len(s.rstrip())
        if used > width:
            return 10**9
        return (width - used) ** 3
    # last line cost 0
    return sum(line_cost(s) for s in lines[:-1])


SAMPLE_EN = (
    "Dynamic programming optimizes text justification by minimizing a cost "
    "function over possible line breaks, while greedy fills lines locally."
)

SAMPLE_MN = (
    "Динамик программчлал нь мөрийн таслалтыг зардлын функцээр оновчилж, "
    "харин шунахай арга нь тухайн мөрийг локал байдлаар дүүргэдэг."
)


class TestJustify(unittest.TestCase):
    def _common_checks(self, lines, width):
        for i, ln in enumerate(lines):
            if i == len(lines) - 1:
                self.assertLessEqual(len(ln.rstrip()), width)
            else:
                self.assertEqual(len(ln), width)

    def test_greedy_basic(self):
        words = SAMPLE_EN.split()
        w = 30
        lines = greedy_justify(words, w)
        self._common_checks(lines, w)

    def test_dp_basic(self):
        words = SAMPLE_EN.split()
        w = 30
        lines = dp_justify(words, w)
        self._common_checks(lines, w)

    def test_mongolian_unicode(self):
        words = SAMPLE_MN.split()
        w = 28
        lines_g = greedy_justify(words, w)
        lines_d = dp_justify(words, w)
        self._common_checks(lines_g, w)
        self._common_checks(lines_d, w)
        self.assertLessEqual(badness(lines_d, w), badness(lines_g, w))


if __name__ == "__main__":
    unittest.main()

