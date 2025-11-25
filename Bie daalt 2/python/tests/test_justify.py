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

    def _words_preserved(self, lines, original_words):
        joined = " ".join(w.strip() for w in original_words)
        out_joined = " ".join(" ".join(line.strip().split()) for line in lines)
        self.assertEqual(joined, out_joined)

    def test_greedy_basic(self):
        words = SAMPLE_EN.split()
        w = 30
        lines = greedy_justify(words, w)
        self._common_checks(lines, w)
        self._words_preserved(lines, words)

    def test_dp_basic(self):
        words = SAMPLE_EN.split()
        w = 30
        lines = dp_justify(words, w)
        self._common_checks(lines, w)
        self._words_preserved(lines, words)

    def test_mongolian_unicode(self):
        words = SAMPLE_MN.split()
        w = 28
        lines_g = greedy_justify(words, w)
        lines_d = dp_justify(words, w)
        self._common_checks(lines_g, w)
        self._common_checks(lines_d, w)
        self.assertLessEqual(badness(lines_d, w), badness(lines_g, w))
        self._words_preserved(lines_d, words)

    def test_exact_fit_line(self):
        words = ["abcd", "efg"]  # 4 + 1 + 3 = 8
        w = 8
        for fn in (greedy_justify, dp_justify):
            lines = fn(words, w)
            self.assertEqual(["abcd efg"], [ln.rstrip() for ln in lines])

    def test_single_long_word(self):
        word = "x" * 40
        w = 30
        for fn in (greedy_justify, dp_justify):
            lines = fn([word], w)
            self.assertEqual(lines[0].strip(), word)

    def test_two_words_vs_single_word_penalty(self):
        words = ["aaaaa", "bbbbb", "cc"]
        w = 11
        lines_d = dp_justify(words, w)
        self.assertIn("aaaaa bbbbb", [ln.rstrip() for ln in lines_d])
        self._common_checks(lines_d, w)


if __name__ == "__main__":
    unittest.main()
